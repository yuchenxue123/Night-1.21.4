package cute.neko.night.setting.type.number

/**
 * @author yuchenxue
 * @date 2025/05/04
 */

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
}