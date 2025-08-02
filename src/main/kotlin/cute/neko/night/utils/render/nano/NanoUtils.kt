package cute.neko.night.utils.render.nano

import com.mojang.blaze3d.platform.GlConst
import com.mojang.blaze3d.systems.RenderSystem
import cute.neko.night.utils.extensions.*
import cute.neko.night.utils.interfaces.Accessor
import net.minecraft.client.render.BufferRenderer
import org.joml.Vector2f
import org.lwjgl.nanovg.NVGColor
import org.lwjgl.nanovg.NanoVG
import org.lwjgl.nanovg.NanoVGGL3
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL13
import org.lwjgl.opengl.GL33
import java.awt.Color

/**
 * @author yuchenxue
 * @date 2025/05/04
 */

object NanoUtils : Accessor {

    var nvg: Long = 0L

    fun create() {
        nvg = NanoVGGL3.nvgCreate(NanoVGGL3.NVG_ANTIALIAS or NanoVGGL3.NVG_STENCIL_STROKES)

        if (nvg != 0L) {
            NanoFontManager
        }
    }

    private val share = NVGColor.calloc()

    fun free() {
        share.free()
    }

    /**
     * Draw with frame
     * @param block draw function
     */
    fun draw(block: () -> Unit) {
        RenderSystem.pixelStore(GlConst.GL_UNPACK_ROW_LENGTH, 0)
        RenderSystem.pixelStore(GlConst.GL_UNPACK_SKIP_PIXELS, 0)
        RenderSystem.pixelStore(GlConst.GL_UNPACK_SKIP_ROWS, 0)
        RenderSystem.pixelStore(GlConst.GL_UNPACK_ALIGNMENT, 4)
        RenderSystem.clearColor(0f, 0f, 0f, 0f)

        beginFrame()

        block.invoke()

        endFrame()

        BufferRenderer.reset()
        GL33.glBindSampler(0, 0)
        RenderSystem.disableBlend()
        GL11.glDisable(GL11.GL_BLEND)
        RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE)
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE)
        RenderSystem.blendEquation(GL33.GL_FUNC_ADD)
        GL33.glBlendEquation(GL33.GL_FUNC_ADD)
        RenderSystem.colorMask(true, true, true, true)
        GL11.glColorMask(true, true, true, true)
        RenderSystem.depthMask(true)
        GL11.glDepthMask(true)
        RenderSystem.disableScissor()
        GL11.glDisable(GL11.GL_SCISSOR_TEST)
        GL11.glDisable(GL11.GL_STENCIL_TEST)
        RenderSystem.disableDepthTest()
        GL11.glDisable(GL11.GL_DEPTH_TEST)
        GL13.glActiveTexture(GL13.GL_TEXTURE0)
        RenderSystem.activeTexture(GL13.GL_TEXTURE0)
        RenderSystem.disableCull()
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
        fillColor(color)

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
        fillColor(color)

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
        fillColor(color)

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

        strokeColor(color)
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

        strokeColor(color)
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

        fillColor(color)
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

        fillColor(color)
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
        NanoVG.nvgBeginPath(nvg)
        NanoVG.nvgRoundedRect(nvg, x, y, width, height, radius)
        NanoVG.nvgStrokeWidth(nvg, strokeWidth)

        strokeColor(color)
        NanoVG.nvgStroke(nvg)
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
        save()

        NanoVG.nvgIntersectScissor(nvg, x, y, width, height)

        block.invoke()

        restore()
    }

    fun fillColor(red: Float, green: Float, blue: Float, alpha: Float) {
        share.r(red).g(green).b(blue).a(alpha)
        NanoVG.nvgFillColor(nvg, share)
    }

    fun fillColor(color: Color) {
        val (r, g, b, a) = color.float()
        share.r(r).g(g).b(b).a(a)
        NanoVG.nvgFillColor(nvg, share)
    }

    fun fillColor(red: Int, green: Int, blue: Int, alpha: Int) {
        val color = Color(red, green, blue, alpha)
        fillColor(color)
    }

    fun strokeColor(red: Float, green: Float, blue: Float, alpha: Float) {
        share.r(red).g(green).b(blue).a(alpha)
        NanoVG.nvgStrokeColor(nvg, share)
    }

    fun strokeColor(color: Color) {
        val (r, g, b, a) = color.float()
        share.r(r).g(g).b(b).a(a)
        NanoVG.nvgStrokeColor(nvg, share)
    }

    fun strokeColor(red: Int, green: Int, blue: Int, alpha: Int) {
        val color = Color(red, green, blue, alpha)
        strokeColor(color)
    }

    fun beginFrame() {
        val width = mc.window.width.toFloat()
        val height = mc.window.height.toFloat()

        NanoVG.nvgBeginFrame(nvg, width, height, 1f)
    }

    fun endFrame() {
        NanoVG.nvgEndFrame(nvg)
    }

    fun alpha(alpha: Float) {
        NanoVG.nvgGlobalAlpha(nvg, alpha)
    }

    fun save() {
        NanoVG.nvgSave(nvg)
    }

    fun restore() {
        NanoVG.nvgRestore(nvg)
    }
}