package cute.neko.night.features.module.render

import cute.neko.night.features.module.ClientModule
import cute.neko.night.features.module.ModuleCategory
import cute.neko.night.ui.widget.WidgetType

/**
 * @author yuchenxue
 * @date 2025/06/01
 */

object ModuleInterface : ClientModule(
    "Interface",
    ModuleCategory.RENDER
) {
    private val hideVignette by boolean("HideVignette", true)

    val widgets = enum("Widgets", arrayOf(WidgetType.ARRAYLIST))

    // array list
    val sideline by boolean("Sideline", false) { widgets.isActive(WidgetType.ARRAYLIST) }
    val arraySuffix by boolean("Suffix", false) { widgets.isActive(WidgetType.ARRAYLIST) }

    val shouldHideVignette: Boolean
        get() = this.running && hideVignette
}