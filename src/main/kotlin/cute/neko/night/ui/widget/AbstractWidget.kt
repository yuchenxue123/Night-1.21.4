package cute.neko.night.ui.widget

import cute.neko.night.features.module.render.ModuleInterface
import cute.neko.night.ui.interfaces.Drawable
import cute.neko.night.utils.interfaces.Nameable

/**
 * @author yuchenxue
 * @date 2025/06/01
 */

abstract class AbstractWidget(
    val type: WidgetType,
    var x: Float,
    var y: Float,
    var width: Float,
    var height: Float,
) : Drawable, Nameable {

    override val name: String
        get() = type.modeName

    val condition: Boolean
        get() = ModuleInterface.widgets.isActive(type)

    open fun onWindowResize(width: Float, height: Float) {}
}