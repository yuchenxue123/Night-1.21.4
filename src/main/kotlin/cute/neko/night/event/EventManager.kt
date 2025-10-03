package cute.neko.night.event

import cute.neko.night.event.events.client.ModuleToggleEvent
import cute.neko.night.event.events.game.client.GameShutdownEvent
import cute.neko.night.event.events.game.client.GameInitializeEvent
import cute.neko.night.event.events.game.client.GameTickEvent
import cute.neko.night.event.events.game.misc.ChatSendEvent
import cute.neko.night.event.events.game.misc.KeyboardEvent
import cute.neko.night.event.events.game.misc.MovementInputEvent
import cute.neko.night.event.events.game.misc.WorldEvent
import cute.neko.night.event.events.game.network.PacketEvent
import cute.neko.night.event.events.game.player.*
import cute.neko.night.event.events.game.render.ScreenRenderEvent
import cute.neko.night.event.events.game.render.WorldRenderEvent
import java.util.concurrent.CopyOnWriteArrayList
import kotlin.reflect.KClass

val ALL_EVENT_CLASSES: Array<KClass<out Event>> = arrayOf(
    // client
    ModuleToggleEvent::class,

    // game
    GameInitializeEvent::class,
    GameShutdownEvent::class,
    GameTickEvent::class,

    ChatSendEvent::class,
    KeyboardEvent::class,
    MovementInputEvent::class,
    WorldEvent::class,

    PacketEvent::class,

    PlayerAfterJumpEvent::class,
    PlayerAttackEntityEvent::class,
    PlayerJumpEvent::class,
    PlayerMotionEvent::class,
    PlayerMovementTickEvent::class,
    PlayerTickEvent::class,
    PlayerUseMultiplier::class,
    PlayerVelocityEvent::class,

    ScreenRenderEvent::class,
    WorldRenderEvent::class,
)

object EventManager {

    private val registry: Map<Class<out Event>, CopyOnWriteArrayList<EventHook<in Event>>> =
        ALL_EVENT_CLASSES.associate { it.java to CopyOnWriteArrayList() }

    /**
     * Register EventHook
     */
    @Suppress("UNCHECKED_CAST")
    fun <T : Event> registerEventHook(eventClass: Class<out Event>, eventHook: EventHook<T>): EventHook<T> {
        val handlers = registry[eventClass] ?: error("The event '${eventClass.name}' is not registered in EventManager.kt::ALL_EVENT_CLASSES.")

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
