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

    constructor(rect: Rect) : this(rect.x(), rect.y(), rect.width(), rect.height())

    protected val rect: Rect = Rect.of(x, y, width, height)

    fun position(x: Float = rect.x(), y: Float = rect.y()) = apply {
        rect.position(x, y)
    }

    fun size(width: Float, height: Float) = apply {
        rect.size(width, height)
    }

    fun x() = rect.x()

    fun y() = rect.y()

    fun width() = rect.width()

    fun height() = rect.height()
}