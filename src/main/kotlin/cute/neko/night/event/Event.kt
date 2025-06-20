package cute.neko.night.event

open class Event

open class CancellableEvent : Event() {

    var isCancelled: Boolean = false
        private set

    fun cancelEvent() {
        isCancelled = true
    }
}