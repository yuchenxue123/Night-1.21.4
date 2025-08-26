package cute.neko.night.event.events.game.player

import cute.neko.night.event.CancellableEvent


/**
 * @author yuchenxue
 * @date 2025/07/02
 */

class PlayerUseMultiplier(
    var forward: Float,
    var sideways: Float
) : CancellableEvent()