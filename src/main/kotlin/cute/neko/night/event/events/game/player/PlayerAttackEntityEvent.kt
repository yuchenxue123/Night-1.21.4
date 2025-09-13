package cute.neko.night.event.events.game.player

import cute.neko.night.event.CancellableEvent
import net.minecraft.entity.Entity

class PlayerAttackEntityEvent(val entity: Entity) : CancellableEvent()