package cute.neko.night.event.priorities

interface EventPriority {

    /**
     * Represents the priority level of an event,
     * larger value typically indicates higher priority.
     */
    val value: Int

    companion object {
        fun valueOf(value: Int): EventPriority {
            return Implement(value)
        }
    }

    private class Implement(override val value: Int) : EventPriority
}