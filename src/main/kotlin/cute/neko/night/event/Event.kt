package cute.neko.night.event

interface Event {
    /* This is a marker interface, no properties or methods are needed here. */
}

abstract class CancellableEvent : Event {

    /**
     * Indicates whether the event has been cancelled.
     * If true, the event's action will be prevented.
     * This property is read-only from outside the class.
     */
    var cancelled: Boolean = false
        private set

    /**
     * Cancel the event.
     */
    fun cancel() {
        cancelled = true
    }
}

enum class EventState {
    PRE, POST
}

enum class PacketType {
    SEND, RECEIVE,
}

