package cute.neko.night.ui.widget

import cute.neko.night.features.setting.type.mode.SubMode

enum class WidgetType(override val modeName: String) : SubMode {
    ARRAYLIST("Arraylist"),
    TARGET("TargetHUD"),
    DYNAMIC_ISLAND("DynamicIsland")
}