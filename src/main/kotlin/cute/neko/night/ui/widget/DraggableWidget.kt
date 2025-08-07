package cute.neko.night.ui.widget

import cute.neko.night.ui.isHovered

/**
 * @author yuchenxue
 * @date 2025/08/07
 */

abstract class DraggableWidget(
    type: WidgetType,
    var x: Float,
    var y: Float,
    var width: Float,
    var height: Float,
) : AbstractWidget(type) {

    private var dragging = false
    private var dragX = .0
    private var dragY = .0

    open fun handleScreenRender(mouseX: Int, mouseY: Int, deltaTime: Float) {
        if (dragging) {
            x = (mouseX * mc.window.scaleFactor - dragX).toFloat()
            y = (mouseY * mc.window.scaleFactor - dragY).toFloat()
        }

    }

    open fun handleScreenMouseClick(mouseX: Double, mouseY: Double, button: Int) {
        if (isHovered(x, y, width, height, mouseX, mouseY)) {
            if (button == 0) {
                dragging = true
                dragX = mouseX * mc.window.scaleFactor - x
                dragY = mouseY * mc.window.scaleFactor - y
            }
        }
    }

    open fun handleScreenMouseRelease(mouseX: Double, mouseY: Double, button: Int) {
        dragging = false
    }
}