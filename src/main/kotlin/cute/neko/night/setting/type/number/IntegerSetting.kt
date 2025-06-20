package cute.neko.night.setting.type.number

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
}