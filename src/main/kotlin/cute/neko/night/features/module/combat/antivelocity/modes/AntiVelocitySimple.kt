package cute.neko.night.features.module.combat.antivelocity.modes

import cute.neko.event.handler
import cute.neko.night.event.PacketEventState
import cute.neko.night.event.events.game.network.PacketEvent
import cute.neko.night.event.events.game.player.PlayerAttackEntityEvent
import cute.neko.night.event.events.game.player.PlayerVelocityEvent
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket

/**
 * @author yuchenxue
 * @date 2025/07/09
 */

object AntiVelocitySimple : AntiVelocityMode("Simple") {

    private var hasReceivedVelocity = false

    @Suppress("unused")
    private val onAttackEntity = handler<PlayerAttackEntityEvent> {
        if (player.isSprinting) {
            player.velocity.x *= 0.6f
            player.velocity.z *= 0.6f
        }
    }

    @Suppress("unused")
    private val onPlayerVelocity = handler<PlayerVelocityEvent> {
        if (hasReceivedVelocity) {
            if (player.isOnGround && !mc.options.jumpKey.isPressed) {
                player.jump()
            }
        }

        if (hasReceivedVelocity && player.hurtTime == 0) {
            hasReceivedVelocity = false
        }
    }

    private val onPacket = handler<PacketEvent> { event ->
        if (event.state != PacketEventState.RECEIVE) {
            return@handler
        }

        val packet = event.packet

        if (packet is EntityVelocityUpdateS2CPacket && packet.entityId == player.id) {
            hasReceivedVelocity = true
        }
    }
}