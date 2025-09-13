package cute.neko.night.features.setting

import cute.neko.night.utils.interfaces.Nameable
import kotlin.reflect.KProperty

typealias ValueListener<T> = (old: T, new: T) -> Unit

abstract class AbstractSetting<T : Any>(
    visibility: () -> Boolean
) : Nameable {
    abstract val value: T
    abstract val default: T

    val visible: Boolean
        get() = visibilities.all { it.invoke() }

    private val visibilities = mutableSetOf<() -> Boolean>().apply {
        add(visibility)
    }

    fun visibility(block: () -> Boolean) = apply {
        visibilities.add(block)
    }

    operator fun getValue(thisRef: Any?, property: KProperty<*>): T = value

    abstract fun get(): T

    protected val listeners: MutableList<ValueListener<T>> = mutableListOf()

    fun listener(listener: (old: T, new: T) -> Unit) = apply {
        listeners.add(listener)
    }
}