package cute.neko.night.utils.misc.option

class NullableOption<T : Any>(
    private val default: T? = null,
) : Option<T?> {

    private var value: T? = default

    override fun get(): T? = value

    override fun set(value: T?) {
        this.value = value
    }

    override fun reset() {
        this.value = default
    }

    fun safeUse(block: (T) -> Unit) {
        get()?.let(block)
    }

    override fun default(): T? = default
}