package cute.neko.night.features.module.player.nofall.modes

import cute.neko.night.event.events.game.network.PacketEvent
import cute.neko.night.event.handler
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket

object NoFallNoGround : NoFallMode("NoGround") {

    @Suppress("unused")
    private val onPacket = handler<PacketEvent> { event ->
        val packet = event.packet

        if (packet is PlayerMoveC2SPacket) {
            packet.onGround = false
        }
    }
}