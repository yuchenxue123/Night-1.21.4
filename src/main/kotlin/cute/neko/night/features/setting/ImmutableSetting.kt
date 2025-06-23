package cute.neko.night.features.setting

/**
 * @author yuchenxue
 * @date 2025/05/04
 */

open class ImmutableSetting<T : Any>(
    override val name: String,
    valueIn: T,
    visibility: () -> Boolean
) : AbstractSetting<T>(visibility) {
    override val value: T = valueIn
    override val default: T = valueIn
    override fun get(): T = value
}