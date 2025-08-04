package cute.neko.night.utils.render.font

import cute.neko.night.utils.extensions.color.alpha
import net.minecraft.text.CharacterVisitor
import net.minecraft.text.Style
import java.awt.Color

/**
 * @author yuchenxue
 * @date 2025/08/04
 */

class DrawVisitor(
    private val font: NanoFont,
    private var x: Float,
    private val y: Float,
    private val color: Color,
    private val size: Float
) : CharacterVisitor {

    override fun accept(index: Int, style: Style, codePoint: Int): Boolean {
        val color = style.color?.let {
            Color(it.rgb).alpha(color.alpha)
        } ?: color

        val text = String(Character.toChars(codePoint))

        x = font.drawText(text, x, y, color, size)

        return true
    }

    fun result(): Float = x
}