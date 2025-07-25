package cute.neko.night.utils.misc

/**
 * @author yuchenxue
 * @date 2025/05/11
 */

object RandomUtils {
    fun int(base: Int, range: Int): Int {
        return (base - range) + (range * 2 * Math.random()).toInt()
    }

    fun float(base: Float, range: Float): Float {
        return (base - range) + (range * 2 * Math.random()).toFloat()
    }

    fun boolean(): Boolean {
        return Math.random() < 0.5
    }
}

