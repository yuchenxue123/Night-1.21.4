package cute.neko.night.utils.misc.option

/**
 * @author yuchenxue
 * @date 2025/08/05
 */

open class SimpleOption<T : Any>(
    private val default: T
) : Option<T> {

    private var value: T = default

    override fun set(value: T) {
        this.value = value
    }

    override fun get(): T = value

    override fun reset() {
        set(default)
    }

    override fun default(): T = default
}