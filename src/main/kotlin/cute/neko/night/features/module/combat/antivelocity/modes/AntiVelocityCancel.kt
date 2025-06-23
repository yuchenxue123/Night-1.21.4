package cute.neko.night.features.module.combat.antivelocity.modes

import cute.neko.night.event.events.game.network.PacketEvent
import cute.neko.night.event.handle
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket

/**
 * @author yuchenxue
 * @date 2025/05/10
 */

object AntiVelocityCancel : AntiVelocityMode("Cancel") {

    private val onPacketReceive = handle<PacketEvent.Receive> { event ->
        val packet = event.packet

        if (packet is EntityVelocityUpdateS2CPacket && packet.entityId == player.id) {
            event.cancelEvent()
        }
    }
}