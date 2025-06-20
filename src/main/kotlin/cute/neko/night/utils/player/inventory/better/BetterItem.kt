package cute.neko.night.utils.player.inventory.better

import net.minecraft.screen.slot.Slot

/**
 * @author yuchenxue
 * @date 2025/06/20
 */

abstract class BetterItem {

    abstract fun scan(slots: List<Slot>): Slot
}