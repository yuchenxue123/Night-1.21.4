package cute.neko.night.utils.extensions

/**
 * @author yuchenxue
 * @date 2025/06/20
 */

fun <E : Enum<E>> E.next(): E = declaringJavaClass.enumConstants.run {
    get((ordinal + 1).mod(size))
}

fun <E : Enum<E>> E.previous(): E = declaringJavaClass.enumConstants.run {
    get((ordinal - 1).mod(size))
}

inline fun <T> Iterable<T>.sum(selector: (T) -> Float): Float {
    var sum = 0f
    for (element in this) {
        sum += selector(element)
    }
    return sum
}

public fun <T> Iterable<T>.reversedList(): Iterable<T> {
    val list = mutableListOf<T>()
    list.addAll(this)
    list.reverse()
    return list
}