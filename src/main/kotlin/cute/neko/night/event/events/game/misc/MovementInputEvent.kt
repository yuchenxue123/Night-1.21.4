package cute.neko.night.event.events.game.misc

import cute.neko.night.event.Event
import cute.neko.night.utils.movement.DirectionalInput

/**
 * @author yuchenxue
 * @date 2025/06/04
 */

class MovementInputEvent(
    var directionalInput: DirectionalInput,
    var jump: Boolean,
    var sneak: Boolean
) : Event()