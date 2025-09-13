package cute.neko.night.ui.screen.click.styles.drop.component

import cute.neko.night.ui.interfaces.Screen
import cute.neko.night.ui.isHovered
import cute.neko.night.utils.misc.option.BooleanOption
import net.minecraft.client.gui.DrawContext

abstract class Bar(
    x: Float,
    y: Float,
    width: Float,
    height: Float,
) : RectComponent(x, y, width, height), Screen {

    abstract val bars: Bars

    val focus = BooleanOption(false)

    private val draggable = BooleanOption(false)
    private var dragX = .0
    private var dragY = .0

    override fun render(context: DrawContext, mouseX: Int, mouseY: Int, delta: Float) {
        if (draggable.get()) {
            rect.position(
                (mouseX * scale - dragX).toFloat(),
                (mouseY * scale - dragY).toFloat()
            )
        }
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int) {
        val hovered = isHovered(rect, mouseX, mouseY)

        if (hovered) {
            focus.set(true)
        }

        if (hovered && button == 0) {
            draggable.set(true)

            // 置顶
            bars.top(this)

            dragX = mouseX * scale - rect.x()
            dragY = mouseY * scale - rect.y()
        }
    }

    override fun mouseReleased(mouseX: Double, mouseY: Double, button: Int) {
        focus.set(false)

        if (button == 0) {
            draggable.set(false)
        }
    }
}