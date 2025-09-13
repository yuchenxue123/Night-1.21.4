package cute.neko.night.utils.misc.option

import cute.neko.night.utils.animation.ColorAnimation
import java.awt.Color

class ColorOption(color: Color = Color.WHITE) : SimpleOption<Color>(color) {
    fun animate(animation: ColorAnimation) = apply {
        set(animation.animate())
    }
}