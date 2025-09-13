package cute.neko.night.features.setting.type.mode

import cute.neko.night.features.setting.MutableSetting

abstract class AbstractModeSetting<T : Any>(
    name: String,
    valueIn: T,
    visibility: () -> Boolean,
) : MutableSetting<T>(name, valueIn, visibility) {

    /**
     * Get the list of modes
     */
    abstract val modes: Array<String>

    /**
     * Get current as string
     */
    abstract fun getAsString(): String

    /**
     * Set value by string
     */
    abstract fun setByString(value: String)
}