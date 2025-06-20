package cute.neko.night.utils.extensions

/**
 * @author yuchenxue
 * @date 2025/05/04
 */

fun <E : Enum<E>> E.next(): E = declaringJavaClass.enumConstants.run {
    get((ordinal + 1).mod(size))
}

fun <E : Enum<E>> E.previous(): E = declaringJavaClass.enumConstants.run {
    get((ordinal - 1).mod(size))
}