package cute.neko.night.features.module.player

import cute.neko.night.event.handler
import cute.neko.night.event.events.game.player.PlayerTickEvent
import cute.neko.night.features.module.ClientModule
import cute.neko.night.features.module.ModuleCategory
import cute.neko.night.utils.misc.option.BooleanOption
import cute.neko.night.utils.player.inventory.Slots
import net.minecraft.component.DataComponentTypes
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket
import net.minecraft.util.Hand

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

        if (player.hungerManager.foodLevel > hunger) {
            eating.set(false)
        }

        if (player.hungerManager.foodLevel <= hunger && eating.get()) {
            (0..8).map {
                it to player.inventory.getStack(it)
            }.find {
                it.second.item.components.get(DataComponentTypes.FOOD) != null
            }?.let { (slot, stack) ->
                val pre = player.inventory.selectedSlot

                Slots.select(slot)

//                network.sendPacket(UpdateSelectedSlotC2SPacket(slot))
//                interactionManager.sendSequencedPacket(world) { sequence ->
//                    PlayerInteractItemC2SPacket(
//                        Hand.MAIN_HAND,
//                        sequence,
//                        player.yaw,
//                        player.pitch
//                    )
//                }
                interactionManager.interactItem(player, Hand.MAIN_HAND)
                repeat(35) {
                    network.sendPacket(PlayerMoveC2SPacket.OnGroundOnly(player.isOnGround, player.horizontalCollision))
                }
//                network.sendPacket(UpdateSelectedSlotC2SPacket(pre))

                Slots.select(pre)

                eating.set(false)
            }
        }
    }
}