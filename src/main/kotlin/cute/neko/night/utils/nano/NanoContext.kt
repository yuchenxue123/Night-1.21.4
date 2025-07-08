package cute.neko.night.utils.nano

import cute.neko.night.utils.client.mc
import cute.neko.night.utils.extensions.*
import org.lwjgl.nanovg.NVGColor
import org.lwjgl.nanovg.NanoVG
import java.awt.Color

/**
 * @author yuchenxue
 * @date 2025/07/07
 */

class NanoContext(private val nvg: Long) {

    private val share = NVGColor.calloc()

    /**
     * 绘制一个矩形
     *
     * @param x 起始 x 坐标
     * @param y 起始 y 坐标
     * @param width 矩形宽度
     * @param height 矩形高度
     */
    fun drawRect(x: Float, y: Float, width: Float, height: Float, color: Color) {
        NanoVG.nvgBeginPath(nvg)

        NanoVG.nvgRect(nvg, x, y, width, height)
        fillColor(color)

        NanoVG.nvgFill(nvg)
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

    fun save() {
        NanoVG.nvgSave(nvg)
    }

    fun restore() {
        NanoVG.nvgRestore(nvg)
    }

    fun beginFrame() {
        val width = mc.window.framebufferWidth.toFloat()
        val height = mc.window.framebufferHeight.toFloat()
        val scale = mc.window.scaleFactor.toFloat()

        NanoVG.nvgBeginFrame(nvg, width, height, scale)
    }

    fun endFrame() {
        NanoVG.nvgEndFrame(nvg)
    }

    fun alpha(alpha: Float) {
        NanoVG.nvgGlobalAlpha(nvg, alpha)
    }
}