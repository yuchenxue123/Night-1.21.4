package cute.neko.night.utils.extensions

import java.awt.Color

/**
 * @author yuchenxue
 * @date 2025/06/20
 */

val Color.redF: Float
    get() = red / 255f

val Color.greenF: Float
    get() = green / 255f

val Color.blueF: Float
    get() = blue / 255f

val Color.alphaF: Float
    get() = alpha / 255f


fun <E : Enum<E>> E.next(): E = declaringJavaClass.enumConstants.run {
    get((ordinal + 1).mod(size))
}

fun <E : Enum<E>> E.previous(): E = declaringJavaClass.enumConstants.run {
    get((ordinal - 1).mod(size))
}

inline fun <T> Iterable<T>.sum(selector: (T) -> Float): Float {
    val iterator = iterator()
    if (!iterator.hasNext()) throw NoSuchElementException()
    var sum = selector(iterator.next())
    while (iterator.hasNext()) {
        sum += selector(iterator.next())
    }
    return sum
}