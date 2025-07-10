package cute.neko.injection.mixins.render;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import cute.neko.night.features.module.render.ModuleRotations;
import cute.neko.night.utils.client.GlobalKt;
import cute.neko.night.utils.rotation.RotationManager;
import cute.neko.night.utils.rotation.data.Rotation;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Pair;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

/**
 * @author yuchenxue
 * @date 2025/05/11
 */

@Mixin(LivingEntityRenderer.class)
public class MixinLivingEntityRenderer<T extends LivingEntity, S extends LivingEntityRenderState, M extends EntityModel<? super S>> {

    @Unique
    private Pair<Rotation, Rotation> getRenderRotation(ModuleRotations.BodyPart bodyPart) {
        if (ModuleRotations.INSTANCE.isActive(bodyPart)) {
            var rotation = RotationManager.INSTANCE.getCurrentRotation();
            var prevRotation = RotationManager.INSTANCE.getPreviousRotation();

            if (rotation != null && prevRotation != null) {
                return new Pair<>(prevRotation, rotation);
            }
        }

        return null;
    }

    @ModifyExpressionValue(
            method = "updateRenderState(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;F)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/entity/LivingEntityRenderer;clampBodyYaw(Lnet/minecraft/entity/LivingEntity;FF)F"
            )
    )
    private float hookBodyYaw(float original, LivingEntity entity, S state, float tickDelta) {
        if (entity != MinecraftClient.getInstance().player) {
            return original;
        }

        var overwriteRotation = getRenderRotation(ModuleRotations.BodyPart.BODY);
        if (overwriteRotation != null) {
            return interpolateAngleDegrees(tickDelta, overwriteRotation.getLeft().getYaw(), overwriteRotation.getRight().getYaw());
        }

        return original;
    }

    @ModifyExpressionValue(
            method = "updateRenderState(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;F)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/util/math/MathHelper;lerpAngleDegrees(FFF)F"
            )
    )
    private float hookHeadYaw(float original, LivingEntity entity, S state, float tickDelta) {
        if (entity != MinecraftClient.getInstance().player) {
            return original;
        }

        var overwriteRotation = getRenderRotation(ModuleRotations.BodyPart.HEAD);
        if (overwriteRotation != null) {
            return interpolateAngleDegrees(tickDelta, overwriteRotation.getLeft().getYaw(), overwriteRotation.getRight().getYaw());
        }

        return original;
    }

    @ModifyExpressionValue(
            method = "updateRenderState(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;F)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/LivingEntity;getLerpedPitch(F)F"
            )
    )
    private float hookHeadPitch(float original, LivingEntity entity, S state, float tickDelta) {
        if (entity != MinecraftClient.getInstance().player) {
            return original;
        }

        var overwriteRotation = getRenderRotation(ModuleRotations.BodyPart.HEAD);
        if (overwriteRotation != null) {
            return interpolateAngleDegrees(tickDelta, overwriteRotation.getLeft().getPitch(), overwriteRotation.getRight().getPitch());
        }

        return original;
    }

    @Unique
    private float interpolateAngleDegrees(float tickDelta, float previousAngle, float currentAngle) {
        float delta = currentAngle - previousAngle;

        if (Math.abs(delta) < 1e-7) {
            return currentAngle;
        }

        return MathHelper.lerpAngleDegrees(tickDelta, previousAngle, currentAngle);
    }
}
