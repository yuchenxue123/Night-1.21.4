package cute.neko.night.event.events.game.player

import cute.neko.event.CancellableEvent
import net.minecraft.entity.Entity

/**
 * @author yuchenxue
 * @date 2025/07/09
 */

class PlayerAttackEntityEvent(val entity: Entity) : CancellableEvent()