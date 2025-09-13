package cute.neko.night.utils.extensions.math

class Rect(
    private var x: Float,
    private var y: Float,
    private var width: Float,
    private var height: Float,
) {

    companion object {
        val ZERO = Rect(0f, 0f, 0f, 0f)

        fun of(x: Float, y: Float, width: Float, height: Float): Rect {
            return Rect(x, y, width, height)
        }
    }

    fun position(x: Float = x(), y: Float = y()) = apply {
        this.x = x
        this.y = y
    }

    fun size(width: Float = width(), height: Float = height()) = apply {
        this.width = width
        this.height = height
    }

    fun x() = this.x

    fun y() = this.y

    fun width() = this.width

    fun height() = this.height
}