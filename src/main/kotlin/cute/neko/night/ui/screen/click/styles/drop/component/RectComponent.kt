package cute.neko.night.ui.screen.click.styles.drop.component

import cute.neko.night.utils.extensions.math.Rect

/**
 * @author yuchenxue
 * @date 2025/08/08
 */

open class RectComponent(
    x: Float,
    y: Float,
    width: Float,
    height: Float,
) {
    protected val rect: Rect = Rect.of(x, y, width, height)

    fun position(x: Float = rect.x(), y: Float = rect.y()) {
        rect.position(x, y)
    }

    fun x() = rect.x()

    fun y() = rect.y()

    fun width() = rect.width()

    fun height() = rect.height()
}