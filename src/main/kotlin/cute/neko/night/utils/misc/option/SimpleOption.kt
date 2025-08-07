package cute.neko.night.utils.misc.option

/**
 * @author yuchenxue
 * @date 2025/08/05
 */

open class SimpleOption<T>(
    val default: T
) {

    private var value: T = default

    fun set(value: T) {
        this.value = value
    }

    fun get(): T = value
}