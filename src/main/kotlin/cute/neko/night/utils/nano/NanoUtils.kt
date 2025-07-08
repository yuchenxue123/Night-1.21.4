package cute.neko.night.utils.nano

import cute.neko.night.utils.interfaces.Accessor
import cute.neko.night.utils.nano.NanoManager.nvg
import org.joml.Vector2f
import org.lwjgl.nanovg.NanoVG
import java.awt.Color


/**
 * @author yuchenxue
 * @date 2025/05/04
 */

object NanoUtils : Accessor {

    /**
     * Draw with frame
     * @param block draw function
     */
    fun draw(block: () -> Unit) {
        NanoManager.beginFrame()

        block.invoke()

        NanoManager.endFrame()
    }

    /**
     * Draw a rect
     * @param x top-left x position
     * @param y top-left y position
     * @param width rect width
     * @param height rect height
     * @param color rect color
     */
    fun drawRect(x: Float, y: Float, width: Float, height: Float, color: Color) {
        NanoVG.nvgBeginPath(nvg)

        NanoVG.nvgRect(nvg, x, y, width, height)
        NanoManager.fillColor(color)

        NanoVG.nvgFill(nvg)
    }

    /**
     * Draw a rounded rect
     * @param x top-left x position
     * @param y top-left y position
     * @param width rect width
     * @param height rect height
     * @param radius radius
     * @param color rect color
     */
    fun drawRoundRect(x: Float, y: Float, width: Float, height: Float, radius: Float, color: Color) {
        NanoVG.nvgBeginPath(nvg)

        NanoVG.nvgRoundedRect(nvg, x, y, width, height, radius)
        NanoManager.fillColor(color)

        NanoVG.nvgFill(nvg)
    }

    /**
     * Draw a circle
     * @param x circle center x position
     * @param y circle center y position
     * @param radius radius
     * @param color circle color
     */
    fun drawCircle(x: Float, y: Float, radius: Float, color: Color) {
        NanoVG.nvgBeginPath(nvg)

        NanoVG.nvgCircle(nvg, x, y, radius)
        NanoManager.fillColor(color)

        NanoVG.nvgFill(nvg)
    }

    /**
     * Draw a line
     * @param x line start x
     * @param y line start y
     * @param x2 line end x
     * @param y2 line end y
     * @param width line width
     * @param color line color
     */
    fun drawLine(x: Float, y: Float, x2: Float, y2: Float, width: Float, color: Color) {
        NanoVG.nvgBeginPath(nvg)

        NanoVG.nvgMoveTo(nvg, x, y)
        NanoVG.nvgLineTo(nvg, x2, y2)

        NanoManager.strokeColor(color)
        NanoVG.nvgStrokeWidth(nvg, width)
        NanoVG.nvgStroke(nvg)
    }

    fun drawVerticalLine(x: Float, y: Float, long: Float, width: Float, color: Color) {
        drawLine(x, y, x, y + long, width, color)
    }

    fun drawHorizontalLine(x: Float, y: Float, long: Float, width: Float, color: Color) {
        drawLine(x, y, x + long, y, width, color)
    }

    /**
     * 绘制多变形
     */
    fun drawPolyline(
        x: Float,
        y: Float,
        points: Array<Vector2f>,
        width: Float,
        color: Color,
        closed: Boolean = false,
    ) {
        if (points.isEmpty()) {
            return
        }

        NanoVG.nvgBeginPath(nvg)

        NanoVG.nvgMoveTo(nvg, x, y)

        points.forEach { p ->
            NanoVG.nvgLineTo(nvg, p.x, p.y)
        }

        if (closed) {
            NanoVG.nvgClosePath(nvg)
        }

        NanoManager.strokeColor(color)
        NanoVG.nvgStrokeWidth(nvg, width)
        NanoVG.nvgStroke(nvg)
    }

    /**
     * 绘制填充的多边形
     */
    fun drawPolygon(
        x: Float,
        y: Float,
        points: Array<Vector2f>,
        color: Color,
    ) {
        if (points.isEmpty()) {
            return
        }

        NanoVG.nvgBeginPath(nvg)

        NanoVG.nvgMoveTo(nvg, x, y)

        points.forEach { p ->
            NanoVG.nvgLineTo(nvg, p.x, p.y)
        }

        NanoVG.nvgClosePath(nvg)

        NanoManager.fillColor(color)
        NanoVG.nvgFill(nvg)
    }

    fun drawIsland(x: Float, y: Float, width: Float, height: Float, color: Color, radius: Float = 12f) {

        // center
        NanoVG.nvgBeginPath(nvg)

        NanoVG.nvgMoveTo(nvg, x, y)

        NanoVG.nvgArcTo(nvg, x - radius, y, x - radius, y + radius, radius)
        NanoVG.nvgLineTo(nvg, x - radius, y + height - radius)

        NanoVG.nvgArcTo(nvg, x - radius, y + height, x + radius, y + height, radius)
        NanoVG.nvgLineTo(nvg, x + width, y + height)

        NanoVG.nvgArcTo(nvg, x + width + radius, y + height, x + width + radius, y + height - radius, radius)
        NanoVG.nvgLineTo(nvg, x + width + radius, y + radius)

        NanoVG.nvgArcTo(nvg, x + width + radius, y, x + width, y, radius)

        NanoVG.nvgClosePath(nvg)

        NanoManager.fillColor(color)
        NanoVG.nvgFill(nvg)
    }

    fun drawOutlineRoundedRect(
        x: Float,
        y: Float,
        width: Float,
        height: Float,
        radius: Float,
        strokeWidth: Float,
        color: Color,
    ) {
        NanoManager.beginFrame()
        NanoVG.nvgBeginPath(nvg)
        NanoVG.nvgRoundedRect(nvg, x, y, width, height, radius)
        NanoVG.nvgStrokeWidth(nvg, strokeWidth)

        NanoManager.strokeColor(color)
        NanoVG.nvgStroke(nvg)
        NanoManager.endFrame()
    }

    fun drawShadow(x: Float, y: Float, width: Float, height: Float, radius: Float, strength: Int) {
        for (f in strength downTo 1) {
            drawOutlineRoundedRect(
                x - (f / 2),
                y - (f / 2),
                width + f,
                height + f,
                radius + 2,
                f.toFloat(),
                Color(0, 0, 0, (strength - f + 1) * 2 - 1)
            )
        }
    }

    fun scissor(x: Float, y: Float, width: Float, height: Float, block: () -> Unit) {
        NanoVG.nvgSave(nvg)

        NanoVG.nvgIntersectScissor(nvg, x, y, width, height)

        block.invoke()

        NanoVG.nvgRestore(nvg)
    }
}