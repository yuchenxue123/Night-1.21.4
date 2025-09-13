package cute.neko.night.features.setting

import kotlin.reflect.KProperty

typealias Consumer<T> = (old: T, new: T) -> T

open class MutableSetting<T : Any>(
    override val name: String,
    valueIn: T,
    visibility: () -> Boolean,
) : AbstractSetting<T>(visibility) {
    override val default: T = valueIn

    override var value: T = valueIn
        set(value) {
            if (field == value) return

            val old = field
            var new = value

            consumers.forEach {
                new = it.invoke(old, new)
            }
            field = new

            listeners.forEach { it.invoke(old, field) }
        }

    protected val consumers = mutableListOf<Consumer<T>>()

    // mutable value setter
    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        this.value = value
    }

    // reset value
    fun reset() {
        value = default
    }

    override fun get(): T = value

    open fun set(value: T) {
        this.value = value
    }
}