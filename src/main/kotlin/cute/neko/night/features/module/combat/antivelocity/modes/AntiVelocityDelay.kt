package cute.neko.night.features.module.combat.antivelocity.modes

import cute.neko.night.event.EventState
import cute.neko.night.event.events.game.misc.SwitchWorldEvent
import cute.neko.night.event.events.game.network.PacketEvent
import cute.neko.night.event.events.game.player.PlayerTickEvent
import cute.neko.night.event.handle
import cute.neko.night.features.module.combat.killaura.ModuleKillAura
import cute.neko.night.features.module.combat.killaura.features.KillAuraTargetTracker
import net.minecraft.network.listener.ClientPlayPacketListener
import net.minecraft.network.packet.Packet
import net.minecraft.network.packet.s2c.common.CommonPingS2CPacket
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket
import java.util.concurrent.ConcurrentLinkedQueue

/**
 * @author yuchenxue
 * @date 2025/07/09
 */

object AntiVelocityDelay : AntiVelocityMode("Delay") {

    private val delayTime by int("Delay", 1000, 100..5000, 100)

    private val onlyHasTarget by boolean("OnlyHasTarget", true)

    private val packets = ConcurrentLinkedQueue<TimePacket>()

    override fun disable() {
        release(false)
    }

    private val onPlayerTick = handle<PlayerTickEvent> {
        release(true)
    }

    private val onPacketReceive = handle<PacketEvent> { event ->
        if (event.state != EventState.RECEIVE) {
            return@handle
        }

        if (onlyHasTarget && (!ModuleKillAura.running || KillAuraTargetTracker.findTarget() == null)) {
            return@handle
        }

        when (val packet = event.packet) {
            is CommonPingS2CPacket -> {
                event.cancelEvent()
                packets.add(TimePacket(packet, System.currentTimeMillis()))
            }

            is EntityVelocityUpdateS2CPacket -> {
                if (packet.entityId == player.id) {
                    event.cancelEvent()
                    packets.add(TimePacket(packet, System.currentTimeMillis()))
                }
            }
        }
    }

    private val onSwitchWorld = handle<SwitchWorldEvent> {
        packets.clear()
    }

    @Suppress("UNCHECKED_CAST")
    private fun release(delay: Boolean) {
        packets.removeAll { timePacket ->
            if (!delay || System.currentTimeMillis() - timePacket.time > delayTime) {
                (timePacket.packet as Packet<ClientPlayPacketListener>).apply(mc.networkHandler)
                true
            } else {
                false
            }
        }
    }

    data class TimePacket(val packet: Packet<*>, val time: Long)
}