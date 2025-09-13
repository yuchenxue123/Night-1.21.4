package cute.neko.night.features.module.combat.antivelocity.modes

import cute.neko.night.event.handler
import cute.neko.night.event.PacketType
import cute.neko.night.event.events.game.network.PacketEvent
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket

object AntiVelocityCancel : AntiVelocityMode("Cancel") {

    @Suppress("unused")
    private val onPacket = handler<PacketEvent> { event ->
        if (event.type != PacketType.RECEIVE) {
            return@handler
        }

        val packet = event.packet

        if (packet is EntityVelocityUpdateS2CPacket && packet.entityId == player.id) {
            event.cancel()
        }
    }
}