package cute.neko.night.utils.animation

import cute.neko.night.utils.time.TimeTracker

open class SimpleAnimation : Animation<Float, SimpleAnimation> {

    companion object {
        fun create(): SimpleAnimation {
            return SimpleAnimation()
        }
    }

    /**
     * 动画类型
     */
    var type: AnimationType = AnimationType.NONE
        private set

    /**
     * 目标开始值
     */
    var start: Float = 0f
        private set

    /**
     * 动画目标值
     */
    var target: Float = 0f
        private set

    /**
     * 动画持续时间
     */
    var duration: Float = 200f
        private set

    fun type(type: AnimationType) = apply {
        this.type = type
    }

    fun start(start: Float) = apply {
        this.start = start
    }

    fun target(target: Float) = apply {
        this.target = target
    }

    fun duration(duration: Float) = apply {
        this.duration = duration
    }

    private val tracker = TimeTracker()
    private var finished = false

    override fun animate(): Float {
        if (hasFinished()) {
            return target
        }

        val process = (tracker.elapsed / duration).coerceIn(0f, 1f)
        val result = start + (target - start) * type.apply(process)

        return result
    }

    override fun hasFinished(): Boolean {
        return tracker.elapsed >= duration || finished
    }

    override fun reset() = apply {
        tracker.reset()
        finished = false
    }

    override fun finish() = apply {
        finished = true
    }

    override fun toString(): String {
        return "target:$target start:$start duration:$duration finished:$finished"
    }
}