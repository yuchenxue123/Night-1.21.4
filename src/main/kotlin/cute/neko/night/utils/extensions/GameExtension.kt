package cute.neko.night.utils.extensions

import net.minecraft.client.network.ClientPlayNetworkHandler
import net.minecraft.network.packet.Packet

/**
 * @author yuchenxue
 * @date 2025/07/02
 */

fun ClientPlayNetworkHandler.sendPacketNoEvent(packet: Packet<*>) {
    connection.send(packet, null)
}