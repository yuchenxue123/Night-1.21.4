package cute.neko.night.utils.extensions

import net.minecraft.client.network.ClientPlayNetworkHandler
import net.minecraft.network.packet.Packet
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket

/**
 * @author yuchenxue
 * @date 2025/07/02
 */

fun ClientPlayNetworkHandler.sendPacketNoEvent(packet: Packet<*>) {
    connection.send(packet, null)
}

fun EntityVelocityUpdateS2CPacket.isFallDamage(): Boolean {
    return velocityX == 0 && velocityZ == 0 && velocityY < 0
}