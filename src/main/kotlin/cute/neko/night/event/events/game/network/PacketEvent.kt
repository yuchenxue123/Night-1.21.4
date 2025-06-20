package cute.neko.night.event.events.game.network

import cute.neko.night.event.CancellableEvent
import net.minecraft.network.packet.Packet

/**
 * @author yuchenxue
 * @date 2025/05/04
 */

sealed class PacketEvent(val packet: Packet<*>) : CancellableEvent() {

    class Send(packet: Packet<*>) : PacketEvent(packet)

    class Receive(packet: Packet<*>) : PacketEvent(packet)
}