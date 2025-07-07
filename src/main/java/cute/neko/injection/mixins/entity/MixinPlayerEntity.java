package cute.neko.injection.mixins.entity;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.mojang.datafixers.util.Either;
import cute.neko.injection.addition.PlayerEntityAddition;
import cute.neko.night.event.EventManager;
import cute.neko.night.event.events.game.player.PlayerTickEvent;
import cute.neko.night.features.module.combat.ModuleKeepSprint;
import cute.neko.night.utils.rotation.RotationManager;
import cute.neko.night.utils.rotation.features.MovementCorrection;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Unit;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author yuchenxue
 * @date 2025/05/10
 */

@Mixin(PlayerEntity.class)
public abstract class MixinPlayerEntity extends MixinLivingEntity implements PlayerEntityAddition {

    @Unique
    private int fallTicks;

    @Inject(method = "tickMovement", at = @At(value = "HEAD"))
    private void hookTickMovement(CallbackInfo callbackInfo) {
        if ((Object) this != MinecraftClient.getInstance().player) {
            return;
        }

        if (isOnGround()) {
            fallTicks = 0;
        } else {
            fallTicks++;
        }

        EventManager.INSTANCE.callEvent(new PlayerTickEvent());
    }

    @ModifyExpressionValue(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;getYaw()F"))
    private float hookFixRotation(float original) {
        if ((Object) this != MinecraftClient.getInstance().player) {
            return original;
        }

        var rotationManager = RotationManager.INSTANCE;
        var rotation = rotationManager.getCurrentRotation();
        var activeRequest = rotationManager.getActiveRequest();

        if (activeRequest == null || activeRequest.getCorrection() == MovementCorrection.NONE || rotation == null) {
            return original;
        }

        return rotation.getYaw();
    }

    @Redirect(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/Vec3d;multiply(DDD)Lnet/minecraft/util/math/Vec3d;"))
    private Vec3d keepSprintHook(Vec3d instance, double x, double y, double z) {
        if ((Object) this == MinecraftClient.getInstance().player) {
            if (ModuleKeepSprint.INSTANCE.getRunning()) {
                x = z = ModuleKeepSprint.INSTANCE.getMotion();
            }
        }

        return instance.multiply(x, y, z);
    }

    @WrapWithCondition(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;setSprinting(Z)V", ordinal = 0))
    private boolean keepSprintHook(PlayerEntity instance, boolean b) {
        if ((Object) this != MinecraftClient.getInstance().player) {
            return true;
        }

        return !ModuleKeepSprint.INSTANCE.getRunning();
    }

    @Override
    public int neko$getFallTicks() {
        return fallTicks;
    }
}
