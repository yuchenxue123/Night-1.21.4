package cute.neko.night.event.events.game.player

import cute.neko.night.event.CancellableEvent

class PlayerJumpEvent(
    var velocity: Float
) : CancellableEvent()