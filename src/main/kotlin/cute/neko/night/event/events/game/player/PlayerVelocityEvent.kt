package cute.neko.night.event.events.game.player

import cute.neko.event.Event
import net.minecraft.util.math.Vec3d

/**
 * @author yuchenxue
 * @date 2025/05/10
 */

class PlayerVelocityEvent(
    val movementInput: Vec3d,
    val speed: Float,
    val yaw: Float,
    var velocity: Vec3d
) : Event