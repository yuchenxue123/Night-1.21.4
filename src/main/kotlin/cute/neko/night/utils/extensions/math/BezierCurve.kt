package cute.neko.night.utils.extensions.math

import org.joml.Vector2f

/**
 * @author yuchenxue
 * @date 2024/11/16 - 12:51
 */

class BezierCurve(
    val start: Vector2f,
    val first: Vector2f,
    val second: Vector2f,
    val end: Vector2f,
) {
    constructor(
        first: Vector2f,
        second: Vector2f
    ) : this(Vector2f(0f, 0f), first, second, Vector2f(1f, 1f))

    fun cubicBezier(time: Float): Vector2f {
        val tt = time * time
        val ttt = time * time * time
        val u = 1 - time
        val uu = u * u
        val uuu = u * u * u

        val px = uuu * start.x + 3 * uu * time * first.x + 3 * u * tt * second.x + ttt * end.x
        val py = uuu * start.y + 3 * uu * time * first.y + 3 * u * tt * second.y + ttt * end.y

        return Vector2f(px, py)
    }
}