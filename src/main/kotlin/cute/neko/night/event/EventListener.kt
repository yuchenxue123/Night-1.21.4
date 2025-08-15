package cute.neko.night.event

import cute.neko.event.EventManager
import cute.neko.night.event.events.game.client.GameTickEvent
import cute.neko.night.event.priorities.EventPriority
import cute.neko.night.event.priorities.Priorities

interface EventListener {

    /**
     * Checks if the event listener is currently active and should process events.
     * By default, it defers to its parent listener's running status if a parent exists,
     * otherwise, it's considered running.
     */
    val running: Boolean
        get() = parent()?.running ?: true

    /**
     * Parent listener of this listener,
     * default is null, indicating no parent.
     */
    fun parent(): EventListener? = null


    /**
     * Unregisters this event listener from the EventManager.
     */
    fun unregister() {
        EventManager.unregisterEventHandler(this)
    }
}

typealias Handler<T> = (event: T) -> Unit

class EventHook<T : Event>(
    val handlerClass: EventListener,
    val handler: Handler<T>,
    val priority: EventPriority,
)

inline fun <reified T : Event> EventListener.handler(
    priority: EventPriority = Priorities.DEFAULT,
    noinline handler: Handler<T>
): EventHook<T> {
    return EventManager.registerEventHook(T::class.java,
        EventHook(this, handler, priority)
    )
}

inline fun <reified T : Event> EventListener.until(
    priority: EventPriority = Priorities.DEFAULT,
    crossinline handler: (event: T) -> Boolean
): EventHook<T> {
    lateinit var eventHook: EventHook<T>
    eventHook = EventHook(this, {
        if (!this.running || handler.invoke(it)) {
            EventManager.unregisterEventHook(T::class.java, eventHook)
        }
    }, priority)
    return EventManager.registerEventHook(T::class.java, eventHook)
}

inline fun <reified T : Event> EventListener.once(
    priority: EventPriority = Priorities.DEFAULT,
    crossinline handler: Handler<T>
): EventHook<T> {
    return until(priority) { event ->
        handler.invoke(event)
        true
    }
}

inline fun <reified T : Event> EventListener.sequenceHandler(
    priority: EventPriority = Priorities.DEFAULT,
    crossinline eventHandler: SuspendableEventHandler<T>
) {
    handler<T>(priority) { event ->
        Sequence(this) { eventHandler.invoke(this@Sequence, event) }
    }
}

@Suppress("AssignedValueIsNeverRead")
fun EventListener.tickHandler(eventHandler: SuspendableHandler) {
    var sequence: TickSequence? = TickSequence(this, eventHandler)

    SequenceManager.handler<GameTickEvent> {
        if (this.running) {
            if (sequence == null) {
                sequence = TickSequence(this, eventHandler)
            }
        } else if (sequence != null) {
            sequence?.cancel()
            sequence = null
        }
    }
}


