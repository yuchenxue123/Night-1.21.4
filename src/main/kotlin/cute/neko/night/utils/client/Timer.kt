package cute.neko.night.utils.client

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