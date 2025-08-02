package cute.neko.night.ui.screen.click

import cute.neko.night.ui.screen.click.styles.Style
import cute.neko.night.ui.screen.click.styles.normal.StyleNormal
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.Screen
import net.minecraft.text.Text
import org.lwjgl.glfw.GLFW

/**
 * @author yuchenxue
 * @date 2025/05/05
 */

class ClickScreen(private val style: Style = StyleNormal) : Screen(Text.of("ClickScreen")) {

    init {
        style.open()
    }

    override fun render(context: DrawContext, mouseX: Int, mouseY: Int, delta: Float) {
        this.applyBlur()

        if (style.hasClosed()) {
            this.client?.setScreen(null)
        }

        style.render(context, mouseX, mouseY, delta)
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean {
        style.mouseClicked(mouseX, mouseY, button)

        super.mouseClicked(mouseX, mouseY, button)

        return false
    }

    override fun mouseReleased(mouseX: Double, mouseY: Double, button: Int): Boolean {
        style.mouseReleased(mouseX, mouseY, button)

        return false
    }

    override fun mouseScrolled(
        mouseX: Double,
        mouseY: Double,
        horizontalAmount: Double,
        verticalAmount: Double
    ): Boolean {
        style.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount)

        return false
    }

    override fun keyPressed(keyCode: Int, scanCode: Int, modifiers: Int): Boolean {
        style.keyPressed(keyCode, scanCode, modifiers)

        return when(keyCode) {
            GLFW.GLFW_KEY_ESCAPE -> {
                style.close()
                true
            }

            else -> false
        }
    }

    override fun shouldPause(): Boolean = false
}