package cute.neko.night.utils.extensions

/**
 * @author yuchenxue
 * @date 2025/05/06
 */

inline fun <T> Iterable<T>.sum(selector: (T) -> Float): Float {
    val iterator = iterator()
    if (!iterator.hasNext()) throw NoSuchElementException()
    var sum = selector(iterator.next())
    while (iterator.hasNext()) {
        sum += selector(iterator.next())
    }
    return sum
}