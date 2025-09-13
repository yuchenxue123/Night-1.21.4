package cute.neko.night.event.events.game.player

import cute.neko.night.event.Event
import net.minecraft.util.math.Vec3d

class PlayerVelocityEvent(
    val movementInput: Vec3d,
    val speed: Float,
    val yaw: Float,
    var velocity: Vec3d
) : Event