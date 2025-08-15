package cute.neko.night.event.events.game.player

import cute.neko.night.event.CancellableEvent

/**
 * @author yuchenxue
 * @date 2025/05/10
 */

class PlayerJumpEvent(
    var velocity: Float
) : CancellableEvent()