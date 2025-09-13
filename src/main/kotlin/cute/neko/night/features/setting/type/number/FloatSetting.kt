package cute.neko.night.features.setting.type.number

import cute.neko.night.utils.extensions.decimals
import cute.neko.night.utils.extensions.step

class FloatSetting(
    name: String,
    value: Float,
    range: ClosedFloatingPointRange<Float>,
    step: Float,
    visibility: () -> Boolean = { true }
) : AbstractNumberSetting<Float>(name, value, range, step, visibility) {
    init {
        consumers.add { _, new ->
            new.coerceIn(range)
        }
    }

    override val min: Float get() = range.start
    override val max: Float get() = range.endInclusive

    override val process: Float
        get() = ((get() - min) / (max - min)).coerceIn(0f, 1f)

    override fun setProcess(process: Float) {
        val newValue = (min + (max - min) * process).step(step).decimals(2)
        set(newValue)
    }
}