package cute.neko.night.ui.screen.click.styles.normal.setting.mode

import cute.neko.night.ui.interfaces.Screen
import cute.neko.night.ui.isHovered
import cute.neko.night.utils.animation.AnimationType
import cute.neko.night.utils.animation.ColorAnimation
import cute.neko.night.utils.render.nano.NanoUtils
import cute.neko.night.utils.render.nano.NanoFontManager
import net.minecraft.client.gui.DrawContext
import java.awt.Color

/**
 * @author yuchenxue
 * @date 2025/05/06
 */

class ModeButton(val modeName: String) : Screen {
    private val font = NanoFontManager.GENSHIN_15

    private var renderX = 0f
    private var renderY = 0f

    private val radius = 8f
    private val padding = 10f

    val width = font.width(modeName) + padding * 2
    val height = font.height(modeName) + padding * 2

    // state
    private var state = false
    private var handler: (ModeButton) -> Boolean = { false }
    private var callback: (String) -> Unit = {}

    // color
    private var color = Color(40, 40, 40, 40)
    private val animation = ColorAnimation(AnimationType.LINEAR).target(color).finish()

    override fun render(context: DrawContext, mouseX: Int, mouseY: Int, delta: Float) {
        update(handler.invoke(this))

        color = animation.animate()

        NanoUtils.drawRoundRect(
            renderX,
            renderY,
            width,
            height,
            radius,
            color
        )

        font.drawText(
            modeName,
            renderX + (width - font.width(modeName)) / 2,
            renderY + (height - font.height(modeName)) / 2,
            Color(200, 200, 200, 255)
        )
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int) {
        val hovered = isHovered(renderX, renderY, width, height, mouseX, mouseY)

        if (hovered && button == 0) {
            callback.invoke(modeName)
        }
    }

    private fun update(newState: Boolean) {
        if (newState == state) return

        when {
            newState -> {
                animation.start(color).target(Color(200, 200, 200, 80)).reset()
            }

            else -> {
                animation.start(color).target(Color(40, 40, 40, 40)).reset()
            }
        }

        state = newState
    }

    fun position(x: Float, y: Float) = apply {
        this.renderX = x
        this.renderY = y
    }

    fun handler(handler: (ModeButton) -> Boolean) = apply {
        this.handler = handler
    }

    fun callback(callback: (String) -> Unit) = apply {
        this.callback = callback
    }
}