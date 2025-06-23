package cute.neko.night.utils.client

/**
 * @author yuchenxue
 * @date 2025/06/23
 */

object Timer {
    private var timerSpeed = 1f

    fun set(speed :Float) {
        timerSpeed = speed
    }

    fun reset() {
        timerSpeed = 1f
    }

    fun get(): Float = timerSpeed
}