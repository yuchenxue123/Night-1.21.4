package cute.neko.night.event.events.game.player

import cute.neko.night.event.CancellableEvent
import cute.neko.night.event.EventState

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
    val state: EventState
) : CancellableEvent()