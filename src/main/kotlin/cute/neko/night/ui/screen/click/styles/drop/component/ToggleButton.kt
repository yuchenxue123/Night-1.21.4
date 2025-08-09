package cute.neko.night.ui.screen.click.styles.drop.component

import cute.neko.night.ui.interfaces.Screen
import cute.neko.night.ui.isHovered
import cute.neko.night.utils.animation.AnimationType
import cute.neko.night.utils.animation.ColorAnimation
import cute.neko.night.utils.animation.SimpleAnimation
import cute.neko.night.utils.misc.option.BooleanOption
import cute.neko.night.utils.misc.option.ColorOption
import cute.neko.night.utils.misc.option.FloatOption
import cute.neko.night.utils.misc.option.SimpleOption
import cute.neko.night.utils.render.nano.NanoUtils
import net.minecraft.client.gui.DrawContext
import org.joml.Vector2f
import java.awt.Color

/**
 * @author yuchenxue
 * @date 2025/08/09
 */

class ToggleButton(
    x: Float,
    y: Float,
    width: Float,
    height: Float,
) : RectComponent(x, y, width, height), Screen {

    companion object {
        fun create(): ToggleButton {
            return ToggleButton(0f, 0f, 0f, 0f)
        }
    }

    private val padding: Vector2f = Vector2f(0f, 0f)

    fun padding(x: Float, y: Float) = apply {
        padding.set(x, y)
    }

    private val receiver = SimpleOption { false }
    private val callback = SimpleOption { _: Boolean -> }

    fun receive(receiver: () -> Boolean) = apply {
        this.receiver.set(receiver)
    }

    fun callback(callback: (Boolean) -> Unit) = apply {
        this.callback.set(callback)
    }

    private val state = BooleanOption(false)

    private val x_offset = FloatOption(0f)
    private val x_animation = SimpleAnimation.create()
        .type(AnimationType.QUAD_OUT)
        .finish()

    private val color = ColorOption(Color(30, 30, 30))
    private val color_animation = ColorAnimation(AnimationType.QUAD_OUT)
        .target(color.default)
        .finish()

    private val radius: Float
        get() = (height() - padding.y() * 2) / 2

    private val x_offset_max: Float
        get() = width() - padding.x() * 2 - radius * 2

    override fun render(context: DrawContext, mouseX: Int, mouseY: Int, delta: Float) {
        update()

        x_offset.animate(x_animation)
        color.animate(color_animation)

        NanoUtils.drawRoundRect(
            x(),
            y(),
            width(),
            height(),
            height() / 2,
            color.get()
        )

        NanoUtils.drawCircle(
            x() + padding.x() + radius + x_offset.get(),
            y() + padding.y() + radius,
            radius,
            Color(255, 255, 255)
        )
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int) {
        val hovered = isHovered(rect, mouseX, mouseY)

        if (hovered && button == 0) {
            callback.get().invoke(!receiver.get().invoke())
        }
    }

    private fun update() {
        val new = receiver.get().invoke()

        if (state.get() == new) {
            return
        }

        when (new) {
            true -> {
                x_animation
                    .start(x_offset.get())
                    .target(x_offset_max)
                    .reset()

                color_animation
                    .start(color.get())
                    .target(Color(0, 140, 255))
                    .reset()
            }

            false -> {
                x_animation
                    .start(x_offset.get())
                    .target(x_offset.default)
                    .reset()

                color_animation
                    .start(color.get())
                    .target(color.default)
                    .reset()
            }
        }

        state.set(new)
    }
}