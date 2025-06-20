package cute.neko.night.module.render

import cute.neko.night.module.ClientModule
import cute.neko.night.module.ModuleCategory
import cute.neko.night.setting.config.types.choice.Choice
import cute.neko.night.setting.config.types.choice.ChoicesConfigurable
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.registry.tag.ItemTags
import net.minecraft.util.Arm
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.RotationAxis

/**
 * @author yuchenxue
 * @date 2025/06/17
 */

object ModuleAnimation : ClientModule(
    "Animation",
    ModuleCategory.RENDER
) {

    val blockAnimationChoice = choices("BlockingAnimation", arrayOf(OneSevenAnimation))

    fun blockCondition(): Boolean {
        val player = mc.player ?: return false

//        val raytrace = player.raycast(5.0, 0f, false) ?: return false

        val mainHandStack = player.mainHandStack
        val activeItemStack = player.activeItem

        return mc.options.useKey.isPressed
                && mainHandStack.item.defaultStack.isIn(ItemTags.SWORDS)
                && mainHandStack.item == activeItemStack.item
                && this.running
    }

    abstract class AnimationChoice(name: String) : Choice(name) {

        override val controller: ChoicesConfigurable<*>
            get() = blockAnimationChoice

        protected fun applySwingOffset(matrices: MatrixStack, arm: Arm, swingProgress: Float) {
            val armSide = if (arm == Arm.RIGHT) 1 else -1
            val f = MathHelper.sin(swingProgress * swingProgress * Math.PI.toFloat())
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(armSide.toFloat() * (45.0f + f * -20.0f)))
            val g = MathHelper.sin(MathHelper.sqrt(swingProgress) * Math.PI.toFloat())
            matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(armSide.toFloat() * g * -20.0f))
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(g * -80.0f))
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(armSide.toFloat() * -45.0f))
        }

        abstract fun transform(matrices: MatrixStack, arm: Arm, equipProgress: Float, swingProgress: Float)
    }

    object OneSevenAnimation : AnimationChoice("1.7") {

        private val translateY by float("Y", 0.1f, 0.05f..0.3f)
        private val swingProgressScale by float("SwingScale", 0.9f, 0.1f..1.0f)

        override fun transform(matrices: MatrixStack, arm: Arm, equipProgress: Float, swingProgress: Float) {
            matrices.translate(if (arm == Arm.RIGHT) -0.1f else 0.1f, translateY, 0.0f)
            applySwingOffset(matrices, arm, swingProgress * swingProgressScale)
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-102.25f))
            matrices.multiply(
                (if (arm == Arm.RIGHT) RotationAxis.POSITIVE_Y else RotationAxis.NEGATIVE_Y)
                    .rotationDegrees(13.365f)
            )
            matrices.multiply(
                (if (arm == Arm.RIGHT) RotationAxis.POSITIVE_Z else RotationAxis.NEGATIVE_Z)
                    .rotationDegrees(78.05f)
            )
        }

    }
}