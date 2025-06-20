package cute.neko.night.setting.type.number

import cute.neko.night.setting.MutableSetting

/**
 * @author yuchenxue
 * @date 2025/05/04
 */

abstract class AbstractNumberSetting<T>(
    name: String,
    value: T,
    val range: ClosedRange<T>,
    val step: T,
    visibility: () -> Boolean
) : MutableSetting<T>(name, value, visibility) where T : Number, T : Comparable<T> {
    abstract val min: T
    abstract val max: T
}