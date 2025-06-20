package cute.neko.night.event

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
    val ignoresCondition: Boolean
)

inline fun <reified T: Event> EventListener.handle(
    ignoreCondition: Boolean = false,
    noinline handler: Handler<T>
) {
    EventManager.registerEventHook(T::class.java, EventHook(this, handler, ignoreCondition))
}