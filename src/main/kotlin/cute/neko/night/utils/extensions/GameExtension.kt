package cute.neko.night.utils.extensions

import net.minecraft.client.network.ClientPlayNetworkHandler
import net.minecraft.network.packet.Packet
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket

fun ClientPlayNetworkHandler.sendPacketNoEvent(packet: Packet<*>) {
    connection.send(packet, null)
}

fun ClientPlayNetworkHandler.sendPackets(vararg packets: Packet<*>) {
    packets.forEach { sendPacket(it) }
}

fun EntityVelocityUpdateS2CPacket.isFallDamage(): Boolean {
    return velocityX == 0 && velocityZ == 0 && velocityY < 0
}