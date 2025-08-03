package cute.neko.night.utils.entity

import cute.neko.event.EventListener
import cute.neko.event.handler
import cute.neko.night.event.PacketEventState
import cute.neko.night.event.Priorities
import cute.neko.night.event.events.game.network.PacketEvent
import cute.neko.night.utils.extensions.sendPacketNoEvent
import cute.neko.night.utils.interfaces.Accessor
import net.minecraft.network.packet.Packet
import net.minecraft.network.packet.c2s.handshake.HandshakeC2SPacket
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket
import net.minecraft.network.packet.c2s.play.CommandExecutionC2SPacket
import net.minecraft.network.packet.c2s.query.QueryPingC2SPacket
import net.minecraft.network.packet.c2s.query.QueryRequestC2SPacket
import java.util.concurrent.LinkedBlockingQueue

/**
 * @author yuchenxue
 * @date 2025/07/02
 */

object Blink : EventListener, Accessor {

    private val packets = LinkedBlockingQueue<Packet<*>>()

    var blinking = false

    fun start() {
        blinking = true
    }

    fun stop() {
        blinking = false
        release()
    }

    @Suppress("unused")
    private val onPacket = handler<PacketEvent>(priority = Priorities.FINAL) { event ->
        if (event.state != PacketEventState.SEND) {
            return@handler
        }

        if (event.cancelled || !blinking) {
            return@handler
        }

        val packet = event.packet

        when (packet) {

            is HandshakeC2SPacket, is QueryRequestC2SPacket, is QueryPingC2SPacket -> {
                return@handler
            }

            is ChatMessageC2SPacket, is CommandExecutionC2SPacket -> {
                return@handler
            }
        }

        event.cancel()
        packets.add(packet)
    }

    fun release() {
        if (packets.isEmpty()) return
        packets.forEach { network.sendPacketNoEvent(it) }
        packets.clear()
    }
}