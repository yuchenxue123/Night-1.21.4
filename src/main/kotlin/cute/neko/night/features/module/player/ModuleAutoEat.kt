package cute.neko.night.features.module.player

import cute.neko.event.handler
import cute.neko.night.event.events.game.player.PlayerTickEvent
import cute.neko.night.features.module.ClientModule
import cute.neko.night.features.module.ModuleCategory
import cute.neko.night.utils.misc.option.BooleanOption
import net.minecraft.component.DataComponentTypes
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket
import net.minecraft.util.Hand

/**
 * @author yuchenxue
 * @date 2025/08/11
 */

object ModuleAutoEat : ClientModule(
    "AutoEat",
    ModuleCategory.PLAYER
) {

    private val hunger by int("Hunger", 15, 0..20)

    private val eating = BooleanOption(false)

    override fun disable() {
        eating.reset()
    }

    @Suppress("unused")
    private val onPlayerTick = handler<PlayerTickEvent> {

        if (player.hungerManager.foodLevel <= hunger && !eating.get()) {
            eating.set(true)
        }

        if (player.hungerManager.foodLevel <= hunger && eating.get()) {
            (0..8).map {
                it to player.inventory.getStack(it)
            }.find {
                it.second.item.components.get(DataComponentTypes.FOOD) != null
            }?.let { (slot, stack) ->
                val pre = player.inventory.selectedSlot

                network.sendPacket(UpdateSelectedSlotC2SPacket(slot))
                interactionManager.interactItem(player, Hand.MAIN_HAND)
                repeat(35) {
                    network.sendPacket(PlayerMoveC2SPacket.OnGroundOnly(player.isOnGround, player.horizontalCollision))
                }
                network.sendPacket(UpdateSelectedSlotC2SPacket(pre))

                eating.set(false)
            }
        }
    }
}