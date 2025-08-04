package cute.neko.night.utils.render.font

import cute.neko.night.utils.interfaces.Accessor
import cute.neko.night.utils.render.nano.NanoUtils
import cute.neko.night.utils.render.nano.NanoUtils.nvg
import net.minecraft.text.Text
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

    fun drawText(text: Text, x: Float, y: Float, color: Color, size: Float = this.size): Float {
        if (font == -1) {
            return x
        }

        val visitor = DrawVisitor(this, x, y, color, size)
        text.asOrderedText().accept(visitor)

        return visitor.result()
    }

    fun drawText(text: String, x: Float, y: Float, color: Color, size: Float = this.size): Float {
        if (font == -1) {
            return x
        }

        NanoVG.nvgFontSize(nvg, size)
        NanoVG.nvgFontFace(nvg, name)
        NanoVG.nvgTextAlign(nvg, NanoVG.NVG_ALIGN_LEFT or NanoVG.NVG_ALIGN_TOP)

        NanoUtils.fillColor(color)

       return NanoVG.nvgText(nvg, x, y, text)
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

    fun width(text: Text, size: Float = this.size): Float {
        if (font == -1) {
            return 0f
        }

        val sb = StringBuilder()

        text.asOrderedText().accept { index, style, codePoint ->

            sb.append(Char(codePoint))

            true
        }

        return width(sb.toString(), size)
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

    fun height(text: Text, size: Float = this.size): Float {
        if (font == -1) {
            return 0f
        }

        var height = 0f

        text.asOrderedText().accept { index, style, codePoint ->

            val h = height(String(Character.toChars(codePoint)), size)

            if (h > height) {
                height = h
            }

            true
        }

        return height
    }
}