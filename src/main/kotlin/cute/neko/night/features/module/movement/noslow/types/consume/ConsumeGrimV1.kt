package cute.neko.night.features.module.movement.noslow.types.consume

import cute.neko.event.handler
import cute.neko.night.event.events.game.player.PlayerTickEvent
import cute.neko.night.event.events.game.player.PlayerUseMultiplier
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket
import net.minecraft.util.Hand



/**
 * @author yuchenxue
 * @date 2025/07/25
 */

object ConsumeGrimV1 : ConsumeMode("GrimV1") {

    private var ticks = 0

    @Suppress("unused")
    private val onPlayerTick = handler<PlayerTickEvent> {
        if (player.isUsingItem) {
            ticks++
        } else {
            ticks = 0
        }
    }

    @Suppress("unused")
    private val onUse = handler<PlayerUseMultiplier> { event ->
        if (player.hungerManager.isNotFull) {
            return@handler
        }

        if (ticks > 3 && ticks < 7) {

            val main = player.getActiveHand() === Hand.MAIN_HAND

            interactionManager.sendSequencedPacket(world) { id ->
                PlayerInteractItemC2SPacket(
                    if (main) Hand.OFF_HAND else Hand.MAIN_HAND,
                    id,
                    player.yaw,
                    player.pitch,
                )
            }
        }
        if (ticks > 5) {
            event.forward = 1f
            event.sideways = 1f
        }
    }
}