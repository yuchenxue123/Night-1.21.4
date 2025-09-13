package cute.neko.night.utils.extensions.color

import java.awt.Color

data class Color4f(
    val red: Float,
    val green: Float,
    val blue: Float,
    val alpha: Float
) {
    fun toColor(): Color {
        return Color(red, green, blue, alpha)
    }
}