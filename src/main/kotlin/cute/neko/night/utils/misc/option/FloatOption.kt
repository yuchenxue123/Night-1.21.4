package cute.neko.night.utils.misc.option

import cute.neko.night.utils.animation.SimpleAnimation

class FloatOption(default: Float) : SimpleOption<Float>(default) {

    fun animate(animation: SimpleAnimation) = apply {
        set(animation.animate())
    }
}