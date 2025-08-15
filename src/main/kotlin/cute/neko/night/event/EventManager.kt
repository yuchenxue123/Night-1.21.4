package cute.neko.event

import cute.neko.night.event.Event
import cute.neko.night.event.EventHook
import cute.neko.night.event.EventListener

object EventManager {

    private val registry = mutableMapOf<Class<out Event>, MutableList<EventHook<in Event>>>()

    /**
     * Register EventHook
     */
    @Suppress("UNCHECKED_CAST")
    fun <T : Event> registerEventHook(eventClass: Class<out Event>, eventHook: EventHook<T>): EventHook<T> {
        if (!registry.containsKey(eventClass)) registry[eventClass] = ArrayList()

        val handlers = registry[eventClass] ?: error("The event '${eventClass.name}' is not registered in Events.kt::ALL_EVENT_CLASSES.")

        val hook = eventHook as EventHook<in Event>

        if (!handlers.contains(hook)) {
            handlers.add(hook)

            handlers.sortedByDescending { it.priority.value }
        }

        return eventHook
    }

    /**
     * Unregisters a handler.
     */
    @Suppress("UNCHECKED_CAST")
    fun <T : Event> unregisterEventHook(eventClass: Class<out Event>, eventHook: EventHook<T>) {
        registry[eventClass]?.remove(eventHook as EventHook<in Event>)
    }


    /**
     * Unregister listener
     */
    fun unregisterEventHandler(eventHandler: EventListener) {
        registry.values.forEach {
            it.removeIf { hook -> hook.handlerClass == eventHandler }
        }
    }

    /**
     * Call event to listeners
     */
    fun  <T: Event> callEvent(event: T): T {
        val targets = registry[event.javaClass] ?: return event

        for (eventHook in targets) {

            if (!eventHook.handlerClass.running) continue

            runCatching {
                eventHook.handler(event)
            }.onFailure {
                println("Exception while executing handler. -> <${event::class.java.name}> -> ${it.cause} -> ${it.stackTraceToString()}")
            }
        }

        return event
    }
}
