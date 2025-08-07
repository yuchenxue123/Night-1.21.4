package cute.neko.night.utils.misc.option

import cute.neko.night.utils.animation.SimpleAnimation

/**
 * @author yuchenxue
 * @date 2025/08/05
 */

class FloatOption(default: Float) : SimpleOption<Float>(default) {

    fun animate(animation: SimpleAnimation) = apply {
        set(animation.animate())
    }
}