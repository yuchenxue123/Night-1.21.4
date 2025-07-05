package cute.neko.night.event

import cute.neko.night.utils.kotlin.Priority

interface EventListener {
    val running: Boolean
        get() = parent()?.running ?: true

    fun parent(): EventListener? = null

    fun unregister() {
        EventManager.unregisterEventHandler(this)
    }
}

typealias Handler<T> = (event: T) -> Unit

class EventHook<T : Event>(
    val handlerClass: EventListener,
    val handler: Handler<T>,
    val always: Boolean,
    val priority: Int,
)

inline fun <reified T: Event> EventListener.handle(
    always: Boolean = false,
    priority: Int = Priority.NORMAL,
    noinline handler: Handler<T>
) {
    EventManager.registerEventHook(T::class.java, EventHook(this, handler, always, priority))
}