package cute.neko.night.utils.player.inventory

import cute.neko.night.utils.interfaces.Accessor
import net.minecraft.item.BlockItem
import net.minecraft.item.Item

/**
 * @author yuchenxue
 * @date 2025/05/23
 */

object Slots : Accessor {

    private var preSlot = -1

    fun isBlock(): Boolean {
        return isItem(BlockItem::class.java)
    }

    fun <T : Item> isItem(target: Class<T>): Boolean {
        val player = mc.player ?: return false
        val item = player.inventory.getStack(player.inventory.selectedSlot)

        return item.javaClass == target
    }

    fun select(slot: Int) {
        val player = mc.player ?: return

        if (slot == -1 || slot == player.inventory.selectedSlot) return

        preSlot = player.inventory.selectedSlot

        player.inventory.selectedSlot = slot
    }

    fun reset() {
        select(preSlot)
    }

    fun findBlockSlot(): Int {
        return findItemSlot(BlockItem::class.java)
    }

    fun <T : Item> findItemSlot(target: Class<T>): Int {
        val player = mc.player ?: return -1

        for (i in 0..8) {
            val item = player.inventory.getStack(i).item

            if (item.javaClass == target) {
                return i
            }
        }

        return -1
    }
}