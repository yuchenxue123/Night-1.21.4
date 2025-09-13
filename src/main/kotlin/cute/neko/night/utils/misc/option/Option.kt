package cute.neko.night.utils.misc.option

interface Option<T> {

    fun get(): T

    fun set(value: T)

    fun reset()

    fun default(): T

}