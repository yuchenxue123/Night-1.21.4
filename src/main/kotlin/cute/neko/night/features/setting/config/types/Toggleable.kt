package cute.neko.night.features.setting.config.types

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