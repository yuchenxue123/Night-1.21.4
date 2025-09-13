package cute.neko.night.ui.screen.click.styles.normal

import cute.neko.night.ui.screen.click.styles.Style
import cute.neko.night.utils.animation.AnimationType
import cute.neko.night.utils.animation.SimpleAnimation
import cute.neko.night.utils.render.nano.NanoUtils
import net.minecraft.client.gui.DrawContext

object StyleNormal : Style() {

    const val SIDE_PANEL_WIDTH = 180f
    const val MIDDLE_PANEL_WIDTH = 180f
    const val MAIN_PANEL_WIDTH = 640f
    const val PANEL_SPACE = 10f

    const val PANEL_HEIGHT = 640f
    const val PANEL_RADIUS = 16f

    val renderX: Float
        get() = (window.width.toFloat()
                - SIDE_PANEL_WIDTH - PANEL_SPACE
                - MIDDLE_PANEL_WIDTH - PANEL_SPACE
                - MAIN_PANEL_WIDTH) / 2

    val renderY: Float
        get() = (window.height.toFloat() - PANEL_HEIGHT) / 2

    private var alpha = 0.5f
    private val animation = SimpleAnimation.create().type(AnimationType.QUAD_OUT).target(alpha).finish()

    override fun open() {
        super.open()
        animation.start(alpha).target(1f).reset()
    }

    override fun close() {
        super.close()
        animation.start(alpha).target(0.5f).reset()
    }

    override fun shouldClose(): Boolean {
        return super.shouldClose() && animation.hasFinished()
    }

    override fun render(context: DrawContext, mouseX: Int, mouseY: Int, delta: Float) {
        alpha = animation.animate()

        NanoUtils.draw {
            NanoUtils.save()

            NanoUtils.alpha(alpha)

            NormalSide.render(context, mouseX, mouseY, delta)
            NormalMiddle.render(context, mouseX, mouseY, delta)
            NormalMain.render(context, mouseX, mouseY, delta)

            NanoUtils.restore()
        }
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int) {
        NormalSide.mouseClicked(mouseX, mouseY, button)
        NormalMiddle.mouseClicked(mouseX, mouseY, button)
        NormalMain.mouseClicked(mouseX, mouseY, button)
    }

    override fun mouseReleased(mouseX: Double, mouseY: Double, button: Int) {
        NormalSide.mouseReleased(mouseX, mouseY, button)
        NormalMiddle.mouseReleased(mouseX, mouseY, button)
        NormalMain.mouseReleased(mouseX, mouseY, button)
    }

    override fun mouseScrolled(mouseX: Double, mouseY: Double, horizontal: Double, vertical: Double) {
        NormalSide.mouseScrolled(mouseX, mouseY, horizontal, vertical)
        NormalMiddle.mouseScrolled(mouseX, mouseY, horizontal, vertical)
        NormalMain.mouseScrolled(mouseX, mouseY, horizontal, vertical)
    }

    override fun keyPressed(keyCode: Int, scanCode: Int, modifiers: Int) {
        NormalSide.keyPressed(keyCode, scanCode, modifiers)
        NormalMiddle.keyPressed(keyCode, scanCode, modifiers)
        NormalMain.keyPressed(keyCode, scanCode, modifiers)
    }

    override fun keyReleased(keyCode: Int, scanCode: Int, modifiers: Int) {
        NormalSide.keyReleased(keyCode, scanCode, modifiers)
        NormalMiddle.keyReleased(keyCode, scanCode, modifiers)
        NormalMain.keyReleased(keyCode, scanCode, modifiers)
    }
}