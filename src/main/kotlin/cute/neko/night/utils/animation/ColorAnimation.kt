package cute.neko.night.utils.animation

import java.awt.Color

class ColorAnimation(
    type: AnimationType,
) : Animation<Color, ColorAnimation> {
    private var start: Color = Color.BLACK
    private var target: Color = Color.WHITE
    private var duration: Float = 200f

    private val red = SimpleAnimation.create()
        .type(type)
        .duration(duration)
        .start(start.red.toFloat()).target(target.red.toFloat())

    private val green = SimpleAnimation.create()
        .type(type)
        .duration(duration)
        .start(start.green.toFloat())
        .target(target.green.toFloat())

    private val blue = SimpleAnimation.create()
        .type(type)
        .duration(duration)
        .start(start.blue.toFloat())
        .target(target.blue.toFloat())

    private val alpha = SimpleAnimation.create()
        .type(type)
        .duration(duration)
        .start(start.alpha.toFloat())
        .target(target.alpha.toFloat())

    private var finished = false

    override fun animate(): Color {
        if (hasFinished()) {
            return target
        }

        return Color(
            red.animate().toInt(),
            green.animate().toInt(),
            blue.animate().toInt(),
            alpha.animate().toInt()
        )
    }

    fun start(start: Color) = apply {
        this.start = start
        update()
    }

    fun target(target: Color) = apply {
        this.target = target
        update()
    }

    fun duration(duration: Float) = apply {
        this.duration = duration
        updateDuration()
    }

    private fun update() {
        red.start(start.red.toFloat()).target(target.red.toFloat()).reset()
        green.start(start.green.toFloat()).target(target.green.toFloat()).reset()
        blue.start(start.blue.toFloat()).target(target.blue.toFloat()).reset()
        alpha.start(start.alpha.toFloat()).target(target.alpha.toFloat()).reset()
    }

    private fun updateDuration() {
        red.duration(duration)
        green.duration(duration)
        blue.duration(duration)
        alpha.duration(duration)
    }

    override fun reset() = apply {
        red.reset()
        green.reset()
        blue.reset()
        alpha.reset()
        finished = false
    }

    override fun hasFinished(): Boolean {
        return (red.hasFinished() && green.hasFinished() && blue.hasFinished() && alpha.hasFinished()) || finished
    }

    override fun finish() = apply {
        finished = true
    }
}