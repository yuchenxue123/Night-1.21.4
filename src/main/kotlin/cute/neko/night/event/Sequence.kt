package cute.neko.night.event

import cute.neko.night.event.events.game.client.GameTickEvent
import cute.neko.night.event.priorities.EventPriority
import cute.neko.night.event.priorities.Priorities
import kotlinx.coroutines.*
import java.util.concurrent.CopyOnWriteArrayList
import java.util.function.BooleanSupplier
import java.util.function.IntSupplier
import kotlin.coroutines.Continuation
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 * @author yuchenxue
 * @date 2025/08/15
 */

typealias SuspendableEventHandler<T> = suspend Sequence.(T) -> Unit
typealias SuspendableHandler = suspend Sequence.() -> Unit

object SequenceManager : EventListener {

    internal val sequences = CopyOnWriteArrayList<Sequence>()

    @Suppress("unused")
    private val onGameTick = handler<GameTickEvent> {
        for (sequence in sequences) {
            if (!sequence.owner.running) {
                sequence.cancel()
                continue
            }

            sequence.tick()
        }
    }

    fun cancelAllSequences(owner: EventListener) {
        sequences.removeIf { sequence ->
            if (sequence.owner == owner) {
                sequence.cancel()
                true
            } else {
                false
            }
        }
    }
}


open class Sequence(val owner: EventListener, val handler: SuspendableHandler) {
    var coroutine: Job
        private set

    private var continuation: Continuation<Unit>? = null
    private var elapsedTicks = 0
    private var totalTicks = IntSupplier { 0 }
    private var cancellationTask: Runnable? = null

    open fun cancel() {
        coroutine.cancel()
        cancellationTask?.run()
        SequenceManager.sequences -= this@Sequence
    }

    init {
        @OptIn(DelicateCoroutinesApi::class)
        this.coroutine = GlobalScope.launch(Dispatchers.Unconfined) {
            SequenceManager.sequences += this@Sequence
            run()
            SequenceManager.sequences -= this@Sequence
        }
    }

    internal open suspend fun run() {
        if (owner.running) {
            runCatching {
                handler.invoke(this@Sequence)
            }.onFailure {
                // sooner
            }
        }
    }

    internal fun tick() {
        if (++this.elapsedTicks >= this.totalTicks.asInt) {
            val continuation = this.continuation ?: return
            this.continuation = null
            continuation.resume(Unit)
        }
    }

    fun onCancellation(task: Runnable) {
        cancellationTask = task
    }

    suspend fun waitUntil(case: BooleanSupplier): Int {
        var ticks = 0
        while (!case.asBoolean) {
            sync()
            ticks++
        }
        return ticks
    }

    suspend fun waitConditional(ticks: Int, breakLoop: BooleanSupplier = BooleanSupplier { false }): Boolean {
        // Don't wait if ticks is 0
        if (ticks == 0) {
            return !breakLoop.asBoolean
        }

        wait { if (breakLoop.asBoolean) 0 else ticks }

        return elapsedTicks >= ticks
    }

    suspend fun waitTicks(ticks: Int) {
        // Don't wait if ticks is 0
        if (ticks == 0) {
            return
        }

        this.wait { ticks }
    }

    // Only in minecraft.
    suspend fun waitSeconds(seconds: Int) {
        if (seconds == 0) {
            return
        }

        this.wait { seconds * 20 }
    }

    private suspend fun wait(ticksToWait: IntSupplier) {
        elapsedTicks = 0
        totalTicks = ticksToWait

        suspendCoroutine { continuation = it }
    }

    internal suspend fun sync() = wait { 0 }

    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun <T> waitFor(deferred: Deferred<T>): T {
        waitUntil(deferred::isCompleted)
        return deferred.getCompleted()
    }

    suspend fun <T> waitFor(
        context: CoroutineContext,
        block: suspend CoroutineScope.() -> T
    ): T = waitFor(CoroutineScope(coroutine + context).async(context, block = block))

    suspend inline fun <reified E : Event> waitNext(
        priority: EventPriority = Priorities.DEFAULT,
        crossinline handler: Handler<E>
    ): E {
        val deferred = CompletableDeferred<E>(parent = coroutine)
        owner.once<E>(priority) {
            handler(it)
            deferred.complete(it)
        }
        return waitFor(deferred)
    }
}

class TickSequence(owner: EventListener, handler: SuspendableHandler) : Sequence(owner, handler) {
    private var running = true

    override suspend fun run() {
        sync()

        while (running && owner.running) {
            super.run()
            sync()
        }
    }

    override fun cancel() {
        running = false
    }

}