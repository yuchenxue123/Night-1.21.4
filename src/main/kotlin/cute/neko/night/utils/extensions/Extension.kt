package cute.neko.night.utils.extensions

import org.joml.Vector4f
import java.awt.Color

/**
 * @author yuchenxue
 * @date 2025/06/20
 */

fun Color.float() = Vector4f(
    (red / 255f).coerceIn(0f, 1f),
    (green / 255f).coerceIn(0f, 1f),
    (blue / 255f).coerceIn(0f, 1f),
    (alpha / 255f).coerceIn(0f, 1f)
)

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