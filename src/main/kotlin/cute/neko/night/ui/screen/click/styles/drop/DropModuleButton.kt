package cute.neko.night.ui.screen.click.styles.drop

import cute.neko.night.features.module.ClientModule
import cute.neko.night.ui.interfaces.Screen
import cute.neko.night.ui.isHovered
import cute.neko.night.ui.screen.click.styles.drop.Constants.COMPONENT_HEIGHT
import cute.neko.night.ui.screen.click.styles.drop.Constants.MODULE_TEXT_LEFT_SPACE
import cute.neko.night.ui.screen.click.styles.drop.component.RectComponent
import cute.neko.night.utils.animation.AnimationType
import cute.neko.night.utils.animation.ColorAnimation
import cute.neko.night.utils.misc.option.BooleanOption
import cute.neko.night.utils.misc.option.ColorOption
import cute.neko.night.utils.render.nano.NanoFontManager
import net.minecraft.client.gui.DrawContext
import java.awt.Color

/**
 * @author yuchenxue
 * @date 2025/08/08
 */

class DropModuleButton(
    val module: ClientModule,
    x: Float,
    y: Float,
    width: Float,
    height: Float,
) : RectComponent(x, y, width, height), Screen {

    private val font = NanoFontManager.GENSHIN

    private val open = BooleanOption(false)

    private val state = BooleanOption(false)
    private val color = ColorOption(Color(255, 255, 255))
    private val color_animation = ColorAnimation(AnimationType.LINEAR)
        .target(Color.WHITE)
        .finish()

    override fun render(context: DrawContext, mouseX: Int, mouseY: Int, delta: Float) {

        // update color
        update()

        color.animate(color_animation)

        val text = module.name
        font.drawText(
            text,
            x() + MODULE_TEXT_LEFT_SPACE,
            y() + (height() - font.height(text)) / 2,
            color.get()
        )
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int) {
        val hovered = isHovered(rect, mouseX, mouseY)

        if (hovered) {
            when (button) {
                0 -> {
                    if (!module.locked) {
                        module.toggle()
                    }
                }

                1 -> {
                    open.toggle()
                }
            }
        }
    }

    private fun update() {
        if (module.state != state.get()) {
            state.set(module.state)

            when (state.get()) {
                true -> {
                    color_animation
                        .start(color.get())
                        .target(Color(0, 255, 255))
                        .reset()
                }

                false -> {
                    color_animation
                        .start(color.get())
                        .target(Color(255, 255, 255))
                        .reset()
                }
            }
        }
    }

    fun getHeight(): Float {
        return COMPONENT_HEIGHT
    }
}