package cute.neko.night.features.module.player.nofall.modes

import cute.neko.event.handler
import cute.neko.night.event.events.game.player.PlayerTickEvent
import cute.neko.night.utils.entity.hasFalldownDamage
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket

/**
 * @author yuchenxue
 * @date 2025/08/02
 */

object NoFallPacket : NoFallMode("Packet") {

    @Suppress("unused")
    private val onPlayerTick = handler<PlayerTickEvent> {

        if (player.hasFalldownDamage) {
            network.sendPacket(PlayerMoveC2SPacket.OnGroundOnly(true, false))
            player.onLanding()
        }
    }
}