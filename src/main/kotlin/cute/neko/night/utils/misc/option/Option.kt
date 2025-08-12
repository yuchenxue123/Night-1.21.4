package cute.neko.night.utils.misc.option

/**
 * @author yuchenxue
 * @date 2025/08/12
 */

interface Option<T> {

    fun get(): T

    fun set(value: T)

    fun reset()

    fun default(): T

}