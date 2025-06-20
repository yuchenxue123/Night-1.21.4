package cute.neko.night.utils.nano

import cute.neko.night.utils.extensions.alphaF
import cute.neko.night.utils.extensions.blueF
import cute.neko.night.utils.extensions.greenF
import cute.neko.night.utils.extensions.redF
import cute.neko.night.utils.interfaces.Accessor
import cute.neko.night.utils.nano.font.NanoFont
import cute.neko.night.utils.nano.font.NanoFontManager
import org.lwjgl.nanovg.NVGColor
import org.lwjgl.nanovg.NanoVG
import org.lwjgl.nanovg.NanoVGGL3
import java.awt.Color

/**
 * @author yuchenxue
 * @date 2025/06/17
 */

object NanoManager : Accessor {

    var nvg: Long = 0L

    fun create() {
        nvg = NanoVGGL3.nvgCreate(NanoVGGL3.NVG_ANTIALIAS or NanoVGGL3.NVG_STENCIL_STROKES)

        if (nvg != 0L) {
            NanoFontManager.fonts.forEach(NanoFont::init)
        }
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

    fun fillColor(color: Color) {
        val nvgColor = NanoVG.nvgRGBAf(color.redF, color.greenF, color.blueF, color.alphaF, NVGColor.create())

        NanoVG.nvgFillColor(nvg, nvgColor)
    }

    fun strokeColor(color: Color) {
        val nvgColor = NanoVG.nvgRGBAf(color.redF, color.greenF, color.blueF, color.alphaF, NVGColor.create())

        NanoVG.nvgStrokeColor(nvg, nvgColor)
    }

    fun save() {
        NanoVG.nvgSave(nvg)
    }

    fun restore() {
        NanoVG.nvgRestore(nvg)
    }

    fun globalAlpha(alpha: Float) {
        NanoVG.nvgGlobalAlpha(nvg, alpha)
    }
}