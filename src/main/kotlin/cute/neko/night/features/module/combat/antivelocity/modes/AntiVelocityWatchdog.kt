package cute.neko.night.features.module.combat.antivelocity.modes

import cute.neko.night.event.EventState
import cute.neko.night.event.events.game.network.PacketEvent
import cute.neko.night.event.events.game.player.PlayerTickEvent
import cute.neko.night.event.handle
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket

/**
 * @author yuchenxue
 * @date 2025/06/03
 */

object AntiVelocityWatchdog : AntiVelocityMode("Watchdog") {

    private var absorbedVelocity = false

    @Suppress("unused")
    private val onPlayerTick = handle<PlayerTickEvent> {
        if (player.isOnGround) {
            absorbedVelocity = true
        }
    }

    private val onPacket = handle<PacketEvent> { event ->
        if (event.state != EventState.RECEIVE) {
            return@handle
        }

        val packet = event.packet

        if (packet is EntityVelocityUpdateS2CPacket && packet.entityId == player.id) {
            if (!player.isOnGround) {
                if (!absorbedVelocity) {
                    event.cancelEvent()
                    absorbedVelocity = true

                    return@handle
                }
            }

            packet.velocityX = (player.velocity.x * 8000).toInt()
            packet.velocityZ = (player.velocity.z * 8000).toInt()
        }
    }
}