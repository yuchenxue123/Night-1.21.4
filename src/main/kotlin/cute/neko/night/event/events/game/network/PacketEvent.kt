package cute.neko.night.event.events.game.network

import cute.neko.night.event.CancellableEvent
import cute.neko.night.event.PacketType
import net.minecraft.network.packet.Packet

class PacketEvent(
    val packet: Packet<*>,
    val type: PacketType
) : CancellableEvent()