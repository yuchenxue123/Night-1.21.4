package cute.neko.night.ui.widget

import cute.neko.night.setting.type.mode.SubMode

/**
 * @author yuchenxue
 * @date 2025/06/01
 */

enum class WidgetType(override val modeName: String) : SubMode {
    ARRAYLIST("Arraylist"),
    TARGET("Target"),
    NOTIFICATION("Notification"),
    DYNAMIC_ISLAND("DynamicIsland")
}