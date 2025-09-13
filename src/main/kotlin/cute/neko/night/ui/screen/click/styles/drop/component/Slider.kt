package cute.neko.night.ui.screen.click.styles.drop.component

import cute.neko.night.ui.interfaces.Screen
import cute.neko.night.ui.isHovered
import cute.neko.night.utils.animation.AnimationType
import cute.neko.night.utils.animation.SimpleAnimation
import cute.neko.night.utils.misc.option.BooleanOption
import cute.neko.night.utils.misc.option.FloatOption
import cute.neko.night.utils.misc.option.SimpleOption
import cute.neko.night.utils.render.nano.NanoUtils
import net.minecraft.client.gui.DrawContext
import java.awt.Color

class Slider(
    x: Float,
    y: Float,
    width: Float,
    height: Float,
) : RectComponent(x, y, width, height), Screen {

    companion object {
        fun create(): Slider {
            return Slider(0f, 0f, 0f, 0f)
        }
    }

    // click check box expand
    private val padding = 4f

    private val padding_up = SimpleOption(0f)
    private val padding_down = SimpleOption(0f)

    fun padding(up: Float = 0f, down: Float = 0f) = apply {
        padding_up.set(up)
        padding_down.set(down)
    }

    private val receiver = SimpleOption { 0f }
    private val callback = SimpleOption { _: Float -> }

    private val process = FloatOption(0f)
    private val process_animation = SimpleAnimation.create()
        .type(AnimationType.QUAD_OUT)
        .target(process.default())
        .finish()

    private val dragging = BooleanOption(false)

    fun receive(receiver: () -> Float) = apply {
        this.receiver.set(receiver)

        update()
    }

    fun callback(callback: (process: Float) -> Unit) = apply {
        this.callback.set(callback)
    }

    override fun render(context: DrawContext, mouseX: Int, mouseY: Int, delta: Float) {

        process.animate(process_animation)

        if (dragging.get()) {
            val dx = (mouseX * scale - x()).coerceIn(0f, width())

            callback.get().invoke((dx / width()).coerceIn(0f, 1f))

            update()
        }

        NanoUtils.drawRoundRect(
            x(), y() + padding_up.get(),
            width(), height(),
            height() / 2f,
            Color(30, 30, 30),
        )

        NanoUtils.drawRoundRect(
            x(), y() + padding_up.get(),
            width() * process.get(),
            height(),
            height() / 2f,
            Color(0, 140, 255),
        )

        NanoUtils.drawCircle(
            x() + width() * process.get(),
            y() + padding_up.get() + height() / 2f,
            height() / 2f + 1f,
            Color(255, 255, 255),
        )
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int) {
        val hovered = isHovered(
            x() - padding,
            y() + padding_up.get() - padding,
            width() + padding * 2f,
            height() + padding * 2f,
            mouseX, mouseY
        )

        if (hovered && button == 0) {
            dragging.set(true)
        }
    }

    override fun mouseReleased(mouseX: Double, mouseY: Double, button: Int) {
        if (button == 0) {
            dragging.set(false)
        }
    }

    private fun update() {
        val process = receiver.get().invoke()

        if (process != process_animation.target) {
            process_animation
                .start(this.process.get())
                .target(process)
                .reset()
        }
    }

    fun getHeight(): Float = height() + padding_up.get() + padding_down.get()
}