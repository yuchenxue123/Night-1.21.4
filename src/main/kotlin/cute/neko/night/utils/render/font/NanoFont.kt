package cute.neko.night.utils.render.font

import cute.neko.night.utils.interfaces.Accessor
import cute.neko.night.utils.render.nano.NanoUtils
import cute.neko.night.utils.render.nano.NanoUtils.nvg
import org.lwjgl.nanovg.NanoVG
import java.awt.Color

/**
 * @author yuchenxue
 * @date 2025/05/04
 */

class NanoFont(
    private val name: String,
    private val font: Int = -1,
    private val size: Float = 18f,
) : Accessor {

    fun drawText(text: String, x: Float, y: Float, color: Color, size: Float = this.size, ) {
        if (font == -1) {
            return
        }

        NanoVG.nvgFontSize(nvg, size)
        NanoVG.nvgFontFace(nvg, name)
        NanoVG.nvgTextAlign(nvg, NanoVG.NVG_ALIGN_LEFT or NanoVG.NVG_ALIGN_TOP)

        NanoUtils.fillColor(color)

        NanoVG.nvgText(nvg, x, y, text)
    }

    fun width(text: String, size: Float = this.size): Float {
        if (font == -1) {
            return 0f
        }

        val bounds = FloatArray(4)

        NanoVG.nvgFontSize(nvg, size)
        NanoVG.nvgFontFace(nvg, name)
        NanoVG.nvgTextAlign(nvg, NanoVG.NVG_ALIGN_LEFT or NanoVG.NVG_ALIGN_TOP)

        NanoVG.nvgTextBounds(nvg, 0f, 0f, text, bounds)

        return bounds[2] - bounds[0]
    }

    fun height(text: String, size: Float = this.size): Float {
        if (font == -1) {
            return 0f
        }

        val bounds = FloatArray(4)

        NanoVG.nvgFontSize(nvg, size)
        NanoVG.nvgFontFace(nvg, name)
        NanoVG.nvgTextAlign(nvg, NanoVG.NVG_ALIGN_LEFT or NanoVG.NVG_ALIGN_TOP)

        NanoVG.nvgTextBounds(nvg, 0f, 0f, text, bounds)

        return bounds[3] - bounds[1]
    }
}