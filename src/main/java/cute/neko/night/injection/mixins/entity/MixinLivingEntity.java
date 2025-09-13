package cute.neko.night.injection.mixins.entity;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import cute.neko.night.event.EventManager;
import cute.neko.night.event.events.game.player.PlayerAfterJumpEvent;
import cute.neko.night.event.events.game.player.PlayerJumpEvent;
import cute.neko.night.features.module.movement.ModuleNoJumpDelay;
import cute.neko.night.features.module.movement.ModuleNoPush;
import cute.neko.night.utils.rotation.RotationManager;
import cute.neko.night.utils.rotation.features.MovementCorrection;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity extends MixinEntity {
    @Shadow
    public abstract float getJumpVelocity();

    @Shadow
    public int jumpingCooldown;

    @Unique
    private PlayerJumpEvent jumpEvent;

    @Inject(method = "jump", at = @At(value = "HEAD"), cancellable = true)
    private void hookJumpEvent(CallbackInfo ci) {
        if ((Object) this != MinecraftClient.getInstance().player) {
            return;
        }

        jumpEvent = new PlayerJumpEvent(getJumpVelocity());
        EventManager.INSTANCE.callEvent(jumpEvent);
        if (jumpEvent.getCancelled()) {
            ci.cancel();
        }
    }

    @ModifyExpressionValue(
            method = "jump",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/LivingEntity;getJumpVelocity()F"
            )
    )
    private float hookJumpEvent(float original) {
        if (((Object) this) != MinecraftClient.getInstance().player) {
            return original;
        }

        return jumpEvent.getVelocity();
    }

    @Inject(method = "jump", at = @At(value = "RETURN"))
    private void hookAfterJumpEvent(CallbackInfo ci) {
        if ((Object) this != MinecraftClient.getInstance().player) {
            return;
        }

        EventManager.INSTANCE.callEvent(new PlayerAfterJumpEvent());
    }

    @ModifyExpressionValue(
            method = "jump",
            at = @At(
                    value = "NEW",
                    target = "(DDD)Lnet/minecraft/util/math/Vec3d;"
            )
    )
    private Vec3d hookFixRotation(Vec3d original) {
        var rotation = RotationManager.INSTANCE.getCurrentRotation();
        var rotationRequest = RotationManager.INSTANCE.getActiveRequest();

        if ((Object) this != MinecraftClient.getInstance().player) {
            return original;
        }

        if (rotationRequest == null || rotationRequest.getCorrection() == MovementCorrection.NONE || rotation == null) {
            return original;
        }

        float yaw = rotation.getYaw() * 0.017453292F;

        return new Vec3d(-MathHelper.sin(yaw) * 0.2F, 0.0, MathHelper.cos(yaw) * 0.2F);
    }

    @Inject(method = "pushAwayFrom", at = @At(value = "HEAD"), cancellable = true)
    private void hookNoPush(CallbackInfo ci) {
        if ((Object) this != MinecraftClient.getInstance().player) {
            return;
        }

        if (ModuleNoPush.INSTANCE.getRunning()) {
            ci.cancel();
        }
    }

    @Inject(method = "tickMovement", at = @At(value = "HEAD"))
    private void hookNoJumpDelay(CallbackInfo ci) {
        if ((Object) this != MinecraftClient.getInstance().player) {
            return;
        }

        if (ModuleNoJumpDelay.INSTANCE.getRunning()) {
            jumpingCooldown = 0;
        }
    }
}
