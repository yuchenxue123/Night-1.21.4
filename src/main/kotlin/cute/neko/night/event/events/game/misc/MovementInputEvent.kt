package cute.neko.night.event.events.game.misc

import cute.neko.night.event.Event
import cute.neko.night.utils.movement.DirectionalInput

class MovementInputEvent(
    var directionalInput: DirectionalInput,
    var jump: Boolean,
    var sneak: Boolean
) : Event