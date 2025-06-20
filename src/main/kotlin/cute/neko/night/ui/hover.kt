package cute.neko.night.ui

import cute.neko.night.utils.client.mc

/**
 * @author yuchenxue
 * @date 2025/05/05
 */

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