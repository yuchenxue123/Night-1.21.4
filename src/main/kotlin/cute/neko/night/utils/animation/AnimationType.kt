package cute.neko.night.utils.animation

import cute.neko.night.features.setting.type.mode.SubMode
import org.joml.Vector2f
import cute.neko.night.utils.extensions.math.BezierCurve

enum class AnimationType(
    override val modeName: String,
    private val function: (time: Float) -> Float
) : SubMode {
    NONE("None",
        { 1f }
    ),

    LINEAR("Linear",
        { time -> time }
    ),

    QUAD_IN("QuadIn",
        { time -> time * time }
    ),

    QUAD_OUT("QuadOut",
        { time -> 1.0f - (1.0f - time) * (1.0f - time) }
    ),

    BACK_IN(
        "BackIn",
        { time ->
            val bezier = BezierCurve(
                Vector2f(0f, 0.1f),
                Vector2f(0.5f, -0.7f),
            )
            bezier.cubicBezier(time).y
        }
    ),

    BACK_OUT(
        "BackOut",
        { time ->
            val bezier = BezierCurve(
                Vector2f(0.5f, 1.7f),
                Vector2f(1f, 0.9f),
            )
            bezier.cubicBezier(time).y
        }
    )

    ;

    fun apply(time: Float): Float {
        return function.invoke(time)
    }
}
