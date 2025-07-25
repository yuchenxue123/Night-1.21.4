package cute.neko.night.event.events.game.player

import cute.neko.event.CancellableEvent
import cute.neko.event.LifecycleEventState

/**
 * @author yuchenxue
 * @date 2025/05/10
 */

class PlayerMotionEvent(
    var x: Double,
    var y: Double,
    var z: Double,
    var yaw: Float,
    var pitch: Float,
    var onGround: Boolean,
    val state: LifecycleEventState
) : CancellableEvent()