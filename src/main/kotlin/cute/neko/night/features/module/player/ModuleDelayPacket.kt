package cute.neko.night.features.module.player

import cute.neko.night.event.PacketType
import cute.neko.night.event.handler
import cute.neko.night.event.events.game.network.PacketEvent
import cute.neko.night.features.module.ClientModule
import cute.neko.night.features.module.ModuleCategory
import cute.neko.night.utils.entity.Blink
import net.minecraft.network.packet.s2c.common.DisconnectS2CPacket

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

    @Suppress("unused")
    private val onPacket = handler<PacketEvent> { event ->
        if (event.type != PacketType.RECEIVE) {
            return@handler
        }

        if (event.packet is DisconnectS2CPacket) {
            toggle()
        }
    }
}