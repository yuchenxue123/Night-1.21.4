package cute.neko.night.utils.extensions.color

import java.awt.Color

/**
 * @author yuchenxue
 * @date 2025/08/04
 */

fun Color.toColor4f(): Color4f = Color4f(
    (red / 255f).coerceIn(0f, 1f),
    (green / 255f).coerceIn(0f, 1f),
    (blue / 255f).coerceIn(0f, 1f),
    (alpha / 255f).coerceIn(0f, 1f)
)

fun Color.red(red: Int): Color = Color(red, green, blue, alpha)
fun Color.green(green: Int): Color = Color(red, green, blue, alpha)
fun Color.blue(blue: Int): Color = Color(red, green, blue, alpha)
fun Color.alpha(alpha: Int): Color = Color(red, green, blue, alpha)