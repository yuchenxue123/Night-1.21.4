package cute.neko.night.features.setting.config.types

/**
 * @author yuchenxue
 * @date 2025/08/15
 */

interface Toggleable : ToggleListener {
    override fun onToggled(state: Boolean) {
        if (state) {
            enable()
        } else {
            disable()
        }
    }

    fun enable() {}

    fun disable() {}
}