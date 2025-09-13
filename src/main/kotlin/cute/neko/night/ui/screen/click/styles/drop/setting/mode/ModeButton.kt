package cute.neko.night.ui.screen.click.styles.drop.setting.mode

import cute.neko.night.ui.interfaces.Screen
import cute.neko.night.ui.isHovered
import cute.neko.night.ui.screen.click.styles.drop.component.RectComponent
import cute.neko.night.utils.animation.AnimationType
import cute.neko.night.utils.animation.ColorAnimation
import cute.neko.night.utils.misc.option.BooleanOption
import cute.neko.night.utils.misc.option.ColorOption
import cute.neko.night.utils.misc.option.SimpleOption
import cute.neko.night.utils.render.nano.NanoFontManager
import net.minecraft.client.gui.DrawContext
import java.awt.Color

class ModeButton(
    private val mode: String,
    x: Float,
    y: Float,
    width: Float,
    height: Float,
) : RectComponent(x, y, width, height), Screen {

    companion object {
        fun create(mode: String): ModeButton {
            return ModeButton(mode, 0f, 0f, 0f, 0f)
        }
    }

    private val font = NanoFontManager.GENSHIN_15

    private val receiver = SimpleOption { _: String -> false }
    private val callback = SimpleOption { _: String -> }

    fun receive(receiver: (mode: String) -> Boolean) = apply {
        this.receiver.set(receiver)
    }

    fun callback(callback: (mode: String) -> Unit) = apply {
        this.callback.set(callback)
    }

    private val state = BooleanOption(false)

    private val color = ColorOption(Color(255, 255, 255))
    private val color_animation = ColorAnimation(AnimationType.QUAD_OUT)
        .target(color.default())
        .finish()

    override fun render(context: DrawContext, mouseX: Int, mouseY: Int, delta: Float) {
        update()

        color.animate(color_animation)


        val text = mode

        font.drawText(
            text,
            x() + (width() - font.width(text)) / 2f,
            y() + (height() - font.height(text)) / 2f,
            color.get(),
        )
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int) {
        val hovered = isHovered(rect, mouseX, mouseY)

        if (hovered && button == 0) {
            callback.get().invoke(mode)
        }
    }

    private fun update() {
        val new = receiver.get().invoke(mode)

        if (new != state.get()) {
            when (new) {
                true -> {
                    color_animation
                        .start(color.get())
                        .target(Color(0, 140, 255))
                        .reset()
                }

                false -> {
                    color_animation
                        .start(color.get())
                        .target(color.default())
                        .reset()
                }
            }

            state.set(new)
        }
    }
}