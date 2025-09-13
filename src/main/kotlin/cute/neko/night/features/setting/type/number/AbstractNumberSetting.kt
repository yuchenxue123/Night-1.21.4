package cute.neko.night.features.setting.type.number

import cute.neko.night.features.setting.MutableSetting

abstract class AbstractNumberSetting<T>(
    name: String,
    value: T,
    val range: ClosedRange<T>,
    val step: T,
    visibility: () -> Boolean
) : MutableSetting<T>(name, value, visibility) where T : Number, T : Comparable<T> {
    abstract val min: T
    abstract val max: T

    abstract val process: Float

    abstract fun setProcess(process: Float)
}