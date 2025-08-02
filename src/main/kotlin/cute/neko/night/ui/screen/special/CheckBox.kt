package cute.neko.night.ui.screen.special

import cute.neko.night.ui.interfaces.Screen
import cute.neko.night.ui.isHovered
import cute.neko.night.utils.animation.AnimationType
import cute.neko.night.utils.animation.SimpleAnimation
import cute.neko.night.utils.render.nano.NanoUtils
import net.minecraft.client.gui.DrawContext
import java.awt.Color

/**
 * @author yuchenxue
 * @date 2025/05/06
 */

class CheckBox : Screen {

    companion object {
        fun create(): CheckBox {
            return CheckBox()
        }
    }

    private var renderX = 0f
    private var renderY = 0f

    var height = 40f
        private set
    var width = 80f
        private set

    private var padding = 6f
    private val radius: Float
        get() = (height - padding * 2) / 2

    private var addition = padding
    private val animation = SimpleAnimation.create().type(AnimationType.QUAD_OUT).duration(200f).target(addition).finish()

    // state
    private var state = false
    private var receive: () -> Boolean = { false }
    private var callback: (Boolean) -> Unit = {}

    override fun render(context: DrawContext, mouseX: Int, mouseY: Int, delta: Float) {
        update(receive.invoke())

        addition = animation.animate()

        val color = 40 + (100 * (addition / (width - padding * 2 - radius * 2))).toInt()

        NanoUtils.drawRoundRect(
            renderX,
            renderY,
            width,
            height,
            height / 2,
            Color(color, color, color, 80)
        )

        NanoUtils.drawCircle(
            renderX + addition + radius,
            renderY + height / 2,
            radius,
            Color(180, 180, 180, 120)
        )
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int) {
        val hovered = isHovered(renderX, renderY, width, height, mouseX, mouseY)

        if (hovered && button == 0) {
            callback.invoke(!receive.invoke())
        }
    }

    private fun update(newState: Boolean) {
        if (newState == state) return

        when {
            newState -> {
                animation.start(addition).target(width - padding - radius * 2).reset()
            }

            else -> {
                animation.start(addition).target(padding).reset()
            }
        }

        state = newState
    }

    fun position(x: Float, y: Float) = apply {
        this.renderX = x
        this.renderY = y
    }

    fun size(
        width: Float,
        height: Float,
        padding: Float
    ) = apply {
        this.width = width
        this.height = height
        this.padding = padding
    }

    fun receive(handler: () -> Boolean) = apply {
        this.receive = handler
    }

    fun callback(callback: (Boolean) -> Unit) = apply {
        this.callback = callback
    }
}