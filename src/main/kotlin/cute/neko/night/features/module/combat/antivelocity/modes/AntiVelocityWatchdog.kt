package cute.neko.night.features.module.combat.antivelocity.modes

import cute.neko.night.event.handler
import cute.neko.night.event.PacketType
import cute.neko.night.event.events.game.network.PacketEvent
import cute.neko.night.event.events.game.player.PlayerTickEvent
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket

/**
 * @author yuchenxue
 * @date 2025/06/03
 */

object AntiVelocityWatchdog : AntiVelocityMode("Watchdog") {

    private var absorbedVelocity = false

    @Suppress("unused")
    private val onPlayerTick = handler<PlayerTickEvent> {
        if (player.isOnGround) {
            absorbedVelocity = true
        }
    }

    @Suppress("unused")
    private val onPacket = handler<PacketEvent> { event ->
        if (event.type != PacketType.RECEIVE) {
            return@handler
        }

        val packet = event.packet

        if (packet is EntityVelocityUpdateS2CPacket && packet.entityId == player.id) {
            if (!player.isOnGround) {
                if (!absorbedVelocity) {
                    event.cancel()
                    absorbedVelocity = true

                    return@handler
                }
            }

            packet.velocityX = (player.velocity.x * 8000).toInt()
            packet.velocityZ = (player.velocity.z * 8000).toInt()
        }
    }
}