package cute.neko.injection.mixins.entity;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import cute.neko.night.event.EventManager;
import cute.neko.night.event.events.game.player.PlayerVelocityEvent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

/**
 * @author yuchenxue
 * @date 2025/05/10
 */

@Mixin(Entity.class)
public abstract class MixinEntity {
    @Shadow
    public abstract boolean isOnGround();

    @Shadow
    public abstract float getPitch();

    @Shadow
    public abstract float getYaw();

    @Shadow
    public abstract double getZ();

    @Shadow
    public abstract double getY();

    @Shadow
    public abstract double getX();

    @Shadow public float fallDistance;

    @ModifyExpressionValue(
            method = "updateVelocity",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/Entity;movementInputToVelocity(Lnet/minecraft/util/math/Vec3d;FF)Lnet/minecraft/util/math/Vec3d;"
            )
    )
    public Vec3d hookVelocity(Vec3d original, @Local(argsOnly = true) Vec3d movementInput, @Local(argsOnly = true) float speed, @Local(argsOnly = true) float yaw) {
        if ((Object) this != MinecraftClient.getInstance().player) {
            return original;
        }

        var event = new PlayerVelocityEvent(movementInput, speed, yaw, original);
        EventManager.INSTANCE.callEvent(event);
        return event.getVelocity();
    }

    @ModifyExpressionValue(method = "move", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;isControlledByPlayer()Z"))
    private boolean fixFallDistanceCalculation(boolean original) {
        if ((Object) this == MinecraftClient.getInstance().player) {
            return false;
        }

        return original;
    }
}
