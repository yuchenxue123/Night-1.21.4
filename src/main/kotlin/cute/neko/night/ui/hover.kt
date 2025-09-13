package cute.neko.night.ui

import cute.neko.night.utils.client.mc
import cute.neko.night.utils.extensions.math.Rect

fun isHovered(
    renderX: Float,
    renderY: Float,
    width: Float,
    height: Float,
    mouseX: Double,
    mouseY: Double
): Boolean {
    val mx = mouseX * mc.window.scaleFactor
    val my = mouseY * mc.window.scaleFactor

    return mx >= renderX && mx <= renderX + width && my >= renderY && my <= renderY + height
}

fun isHovered(
    rect: Rect,
    mouseX: Double,
    mouseY: Double
) = isHovered(rect.x(), rect.y(), rect.width(), rect.height(), mouseX, mouseY)