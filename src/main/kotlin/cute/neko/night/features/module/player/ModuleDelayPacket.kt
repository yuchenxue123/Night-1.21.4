package cute.neko.night.features.module.player

import cute.neko.event.handler
import cute.neko.night.event.PacketEventState
import cute.neko.night.event.events.game.network.PacketEvent
import cute.neko.night.features.module.ClientModule
import cute.neko.night.features.module.ModuleCategory
import cute.neko.night.utils.entity.Blink
import net.minecraft.network.packet.s2c.common.DisconnectS2CPacket

/**
 * @author yuchenxue
 * @date 2025/07/02
 */

object ModuleDelayPacket : ClientModule(
    "DelayPacket",
    ModuleCategory.PLAYER,
) {

    override fun enable() {
        Blink.start()
    }

    override fun disable() {
        Blink.stop()
    }

    private val onPacket = handler<PacketEvent> { event ->
        if (event.state != PacketEventState.RECEIVE) {
            return@handler
        }

        if (event.packet is DisconnectS2CPacket) {
            toggle()
        }
    }
}