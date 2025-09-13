package cute.neko.night.ui.widget

import cute.neko.night.ui.isHovered
import cute.neko.night.utils.render.nano.NanoFontManager
import cute.neko.night.utils.render.nano.NanoUtils
import java.awt.Color

abstract class DraggableWidget(
    type: WidgetType,
    var x: Float,
    var y: Float,
    var width: Float,
    var height: Float,
) : AbstractWidget(type) {

    private val font = NanoFontManager.GENSHIN_12

    private val scale : Double
        get() = mc.window.scaleFactor

    private val BOARD_PADDING = 4f

    private var dragging = false
    private var dragX = .0
    private var dragY = .0

    open fun handleScreenRender(mouseX: Int, mouseY: Int, deltaTime: Float) {
        if (dragging) {
            x = (mouseX * scale - dragX).toFloat()
            y = (mouseY * scale - dragY).toFloat()
        }

        NanoUtils.drawOutlineRect(
            x - BOARD_PADDING,
            y - BOARD_PADDING,
            width + BOARD_PADDING * 2f,
            height + BOARD_PADDING * 2f,
            1f,
            Color(255, 255, 255, 255)
        )

        val text = type.modeName
        font.drawText(
            text,
            x,
            y - BOARD_PADDING - 4f - font.height(text),
            Color(255, 255, 255)
        )
    }

    open fun handleScreenMouseClick(mouseX: Double, mouseY: Double, button: Int) {
        val hovered = isHovered(
            x - BOARD_PADDING,
            y - BOARD_PADDING,
            width + BOARD_PADDING * 2f,
            height + BOARD_PADDING * 2f,
            mouseX, mouseY
        )

        if (hovered && button == 0) {
            dragging = true
            dragX = mouseX * scale - x
            dragY = mouseY * scale - y
        }
    }

    open fun handleScreenMouseRelease(mouseX: Double, mouseY: Double, button: Int) {
        if (dragging && button == 0) {
            dragging = false
        }

    }
}