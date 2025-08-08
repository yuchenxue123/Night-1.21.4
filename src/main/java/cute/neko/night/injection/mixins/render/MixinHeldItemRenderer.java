package cute.neko.night.injection.mixins.render;

import cute.neko.night.features.module.render.ModuleAnimation;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.math.RotationAxis;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author yuchenxue
 * @date 2025/06/17
 */

@Mixin(HeldItemRenderer.class)
public class MixinHeldItemRenderer {

    @Inject(
            method = "renderFirstPersonItem",
            slice = @Slice(
                    from = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getUseAction()Lnet/minecraft/item/consume/UseAction;")
            ),
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/item/HeldItemRenderer;applyEquipOffset(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/util/Arm;F)V",
                    ordinal = 2,
                    shift = At.Shift.AFTER
            )
    )
    private void transformLegacyBlockAnimations(
            AbstractClientPlayerEntity player, float tickDelta, float pitch,
            Hand hand, float swingProgress, ItemStack item, float equipProgress,
            MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light,
            CallbackInfo ci
    ) {
        var shouldAnimate = ModuleAnimation.INSTANCE.blockCondition();

        if (shouldAnimate && item.getItem().getDefaultStack().isIn(ItemTags.SWORDS)) {
            final Arm arm = (hand == Hand.MAIN_HAND) ? player.getMainArm() : player.getMainArm().getOpposite();

            if (ModuleAnimation.INSTANCE.getRunning()) {
                var activeChoice = ModuleAnimation.INSTANCE.getBlockAnimationChoice().getActive();

                activeChoice.transform(matrices, arm, equipProgress, swingProgress);
                return;
            }

            // Default animation
            ModuleAnimation.OneSevenAnimation.INSTANCE.transform(matrices, arm, equipProgress, swingProgress);
        }
    }

    @Unique
    private void applyTransformations(MatrixStack matrices, float translateX, float translateY, float translateZ, float rotateX, float rotateY, float rotateZ) {
        matrices.translate(translateX, translateY, translateZ);
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(rotateX));
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(rotateY));
        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(rotateZ));
    }
}
