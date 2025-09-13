package cute.neko.night.event.events.game.player

import cute.neko.night.event.CancellableEvent

class PlayerUseMultiplier(
    var forward: Float,
    var sideways: Float
) : CancellableEvent()