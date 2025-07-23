package cute.neko.night.features.module.combat.antivelocity.modes

import cute.neko.night.event.EventState
import cute.neko.night.event.events.game.network.PacketEvent
import cute.neko.night.event.events.game.player.PlayerAttackEntityEvent
import cute.neko.night.event.events.game.player.PlayerVelocityEvent
import cute.neko.night.event.handle
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket

/**
 * @author yuchenxue
 * @date 2025/07/09
 */

object AntiVelocitySimple : AntiVelocityMode("Simple") {

    private var hasReceivedVelocity = false

    private val onAttackEntity = handle<PlayerAttackEntityEvent> {
        if (player.isSprinting) {
            player.velocity.x *= 0.6f
            player.velocity.z *= 0.6f
        }
    }

    private val onPlayerVelocity = handle<PlayerVelocityEvent> {
        if (hasReceivedVelocity) {
            if (player.isOnGround && !mc.options.jumpKey.isPressed) {
                player.jump()
            }
        }

        if (hasReceivedVelocity && player.hurtTime == 0) {
            hasReceivedVelocity = false
        }
    }

    private val onPacket = handle<PacketEvent> { event ->
        if (event.state != EventState.RECEIVE) {
            return@handle
        }

        val packet = event.packet

        if (packet is EntityVelocityUpdateS2CPacket && packet.entityId == player.id) {
            hasReceivedVelocity = true
        }
    }
}