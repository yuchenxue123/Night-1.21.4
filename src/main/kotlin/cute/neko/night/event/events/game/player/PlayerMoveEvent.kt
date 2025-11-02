package cute.neko.night.event.events.game.player

import cute.neko.night.event.Event
import net.minecraft.entity.MovementType
import net.minecraft.util.math.Vec3d

class PlayerMoveEvent(val type: MovementType, var movement: Vec3d) : Event