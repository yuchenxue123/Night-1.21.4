package cute.neko.night.utils.nano.font

import cute.neko.night.utils.interfaces.Accessor
import cute.neko.night.utils.nano.NanoManager
import cute.neko.night.utils.nano.NanoManager.nvg
import net.minecraft.client.gui.DrawContext
import org.lwjgl.nanovg.NanoVG
import java.awt.Color

/**
 * @author yuchenxue
 * @date 2025/05/04
 */

/**
 * NanoVG is better
 */
class NanoFont(
    private val data: FontData,
    private val size: Float,
) : Accessor {
    private val name = data.name
    private var font: Int = -1

    fun init() {
        val buffer = data.buffer ?: return
        font = NanoVG.nvgCreateFontMem(nvg, name, buffer, true)
    }

    fun render(
        context: DrawContext,
        text: String,
        x: Float, y: Float,
        color: Color,
        size: Float = this.size,
    ) {
        if (font == -1) {
            return
        }

        NanoVG.nvgFontSize(nvg, size)
        NanoVG.nvgFontFace(nvg, name)
        NanoVG.nvgTextAlign(nvg, NanoVG.NVG_ALIGN_LEFT or NanoVG.NVG_ALIGN_TOP)

        NanoManager.fillColor(color)

        NanoVG.nvgText(nvg, x, y, text)
    }

    fun render(
        text: String,
        x: Float, y: Float,
        color: Color,
        size: Float = this.size,
    ) {
        if (font == -1) {
            return
        }

        NanoVG.nvgFontSize(nvg, size)
        NanoVG.nvgFontFace(nvg, name)
        NanoVG.nvgTextAlign(nvg, NanoVG.NVG_ALIGN_LEFT or NanoVG.NVG_ALIGN_TOP)

        NanoManager.fillColor(color)

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