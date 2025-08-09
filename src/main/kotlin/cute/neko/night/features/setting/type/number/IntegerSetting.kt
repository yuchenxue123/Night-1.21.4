package cute.neko.night.features.setting.type.number

import cute.neko.night.utils.extensions.step

/**
 * @author yuchenxue
 * @date 2025/05/04
 */

class IntegerSetting(
    name: String,
    value: Int,
    range: IntRange,
    step: Int,
    visibility: () -> Boolean = { true }
) : AbstractNumberSetting<Int>(name, value, range, step, visibility) {
    init {
        consumers.add { _, new ->
            new.coerceIn(range)
        }
    }

    override val min: Int get() = range.start
    override val max: Int get() = range.endInclusive

    override val process: Float
        get() = ((get() - min).toFloat() / (max - min)).coerceIn(0f, 1f)

    override fun setProcess(process: Float) {
        val newValue = (min + (max - min) * process).step(step.toFloat()).toInt()
        set(newValue)
    }
}