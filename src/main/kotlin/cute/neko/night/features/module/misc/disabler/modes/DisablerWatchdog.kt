package cute.neko.night.features.module.misc.disabler.modes

import cute.neko.night.event.events.game.misc.MovementInputEvent
import cute.neko.night.event.events.game.network.PacketEvent
import cute.neko.night.event.events.game.player.PlayerAfterJumpEvent
import cute.neko.night.event.events.game.player.PlayerMotionEvent
import cute.neko.night.event.handle
import cute.neko.night.features.module.misc.disabler.ModuleDisabler
import cute.neko.night.features.setting.config.types.ToggleConfigurable
import cute.neko.night.utils.extensions.airTicks
import cute.neko.night.utils.rotation.RotationManager
import cute.neko.night.utils.rotation.data.Rotation
import cute.neko.night.utils.rotation.data.RotationRequest
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket
import net.minecraft.network.packet.s2c.play.PlayerRespawnS2CPacket

/**
 * @author yuchenxue
 * @date 2025/06/04
 */

object DisablerWatchdog : ToggleConfigurable("Watchdog", false, ModuleDisabler) {

    private var flags = 0
    private var execute = false
    private var jump = false
    var disabled = false

    override fun disable() {
        disabled = false
        RotationManager.remove(this)
    }

    @Suppress("unused")
    private val onMotionPre = handle<PlayerMotionEvent.Pre> { event ->

        if (player.isOnGround && !disabled) {
            jump = true
        }

        if (!disabled && execute && player.airTicks >= 10) {
            if (player.airTicks % 2 == 0) {

                RotationManager.request(
                    RotationRequest(
                        this,
                        Rotation((player.yaw - 10f + (Math.random() - 0.5f) * 3f).toFloat(), player.pitch)
                    )
                )

                event.x += 0.095
            }
            player.setVelocity(.0, .0, .0)
        }
    }

    @Suppress("unused")
    private val onMovementInput = handle<MovementInputEvent> { event ->
        if (jump) {
            event.jump = true
        }
    }

    @Suppress("unused")
    private val onAfterJump = handle<PlayerAfterJumpEvent> {
        if (!jump) return@handle
        jump = false
        execute = true
    }

    @Suppress("unused")
    private val onPacketReceive = handle<PacketEvent.Receive> { event ->
        val packet = event.packet

        when (packet) {
            is PlayerRespawnS2CPacket -> {
                disabled = false
            }

            is PlayerPositionLookS2CPacket -> {
                if (++flags >= 20) {
                    execute = false
                    flags = 0
                    disabled = true
                    RotationManager.remove(this)
                }
            }
        }
    }
}