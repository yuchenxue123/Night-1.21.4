package cute.neko.night.utils.animation

import org.joml.Vector2f
import cute.neko.night.utils.extensions.math.BezierCurve

/**
 * @author yuchenxue
 * @date 2025/02/20
 */

enum class AnimationType(private val function: (time: Float) -> Float) {
    NONE({ 1f }),
    LINEAR({ time -> time }),
    QUAD_IN({ time -> time * time }),
    QUAD_OUT({ time -> 1.0f - (1.0f - time) * (1.0f - time) }),
    BACK_IN({ time ->
        val bezier = BezierCurve(
            Vector2f(0f, 0.1f),
            Vector2f(0.5f, -0.7f),
        )
        bezier.cubicBezier(time).y
    }),
    BACK_OUT({ time ->
        val bezier = BezierCurve(
            Vector2f(0.5f, 1.7f),
            Vector2f(1f, 0.9f),
        )
        bezier.cubicBezier(time).y
    })
    ;

    fun apply(time: Float): Float {
        return function.invoke(time)
    }
}
