package cute.neko.night.setting.type.mode

import cute.neko.night.setting.MutableSetting

/**
 * @author yuchenxue
 * @date 2025/05/05
 */

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