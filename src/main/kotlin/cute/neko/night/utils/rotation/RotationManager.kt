package cute.neko.night.utils.rotation

import cute.neko.night.event.EventListener
import cute.neko.night.event.events.game.network.PacketEvent
import cute.neko.night.event.events.game.player.PlayerMotionEvent
import cute.neko.night.event.events.game.player.PlayerVelocityEvent
import cute.neko.night.event.handle
import cute.neko.night.utils.entity.rotation
import cute.neko.night.utils.entity.setRotation
import cute.neko.night.utils.interfaces.Accessor
import cute.neko.night.utils.movement.DirectionalInput
import cute.neko.night.utils.rotation.RotationUtils.angleDifference
import cute.neko.night.utils.rotation.api.rotationPriority
import cute.neko.night.utils.rotation.data.Rotation
import cute.neko.night.utils.rotation.data.RotationTarget
import cute.neko.night.utils.rotation.features.MovementCorrection
import net.minecraft.client.input.KeyboardInput
import net.minecraft.entity.Entity
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket
import net.minecraft.util.math.MathHelper
import kotlin.math.abs
import kotlin.math.hypot


/**
 * @author yuchenxue
 * @date 2025/05/09
 */

object RotationManager : EventListener, Accessor {

    /**
     * The rotation plan your will complete.
     * Contains (rotation, correction, horizontal speed, vertical speed).
     */
    var rotationTarget: RotationTarget? = null

    /**
     * The rotation we want aim at.
     * The sever will change your rotation sometime,
     * so it may not be your actual rotation.
     */
    var currentRotation: Rotation? = null
        set(value) {
            previousRotation = if (value == null) {
                null
            } else {
                field ?: mc.player?.rotation ?: Rotation.ZERO
            }

            field = value
        }

    // previous rotation
    var previousRotation: Rotation? = null

    /**
     * The rotation recognized by the server.
     * It is your actual rotation, seen by other players.
     */
    var serverRotation: Rotation = Rotation.ZERO

    /**
     * Input rotation target
     */
    fun applyRotation(rotation: RotationTarget?) {
        rotation?.let {
            if (rotationTarget == null) {
                rotationTarget = it
                return
            }

            if (it.controller.rotationPriority.value
                >= rotationTarget!!.controller.rotationPriority.value
            ) {
                rotationTarget = it
            }
        } ?: run {
            rotationTarget = null
        }

        update()
    }

    /**
     * Reset rotation
     */
    fun reset(listener: EventListener) {
        rotationTarget?.let {
            if (listener != it.controller) {
                return
            }
        }

        applyRotation(null)
    }

    private fun update() {
        updateCurrentRotation()
    }

    private fun updateCurrentRotation() {
        rotationTarget?.let { target ->
            val lastRotation = (currentRotation ?: player.rotation)

            val yawDifference = angleDifference(target.rotation.yaw, lastRotation.yaw)
            val pitchDifference = angleDifference(target.rotation.pitch, lastRotation.pitch)

            val rotationDifference = hypot(abs(yawDifference), abs(pitchDifference))

            val (horizontal, vertical) = target.horizontalSpeed to target.verticalSpeed

            val straightLineYaw = abs(yawDifference / rotationDifference) * horizontal
            val straightLinePitch = abs(pitchDifference / rotationDifference) * vertical

            val nextRotation = Rotation(
                lastRotation.yaw + yawDifference.coerceIn(-straightLineYaw, straightLineYaw),
                lastRotation.pitch + pitchDifference.coerceIn(-straightLinePitch, straightLinePitch)
            )

            currentRotation = nextRotation
        } ?: run {
            currentRotation = null
        }
    }

    @Suppress("unused")
    private val onPlayerMotionPre = handle<PlayerMotionEvent.Pre> { event ->
        val correction = rotationTarget?.correction ?: return@handle

        currentRotation?.let {
            when (correction) {
                MovementCorrection.LOOK -> {
                    player.setRotation(it)
                }

                else -> {
                    event.yaw = it.yaw
                    event.pitch = it.pitch
                }
            }
        }
    }

    @Suppress("unused")
    private val onPlayerVelocityUpdate = handle<PlayerVelocityEvent> { event ->
        rotationTarget?.correction?.let { correction ->
            if (correction != MovementCorrection.NONE) {
                val rotation = currentRotation ?: return@handle

                event.velocity = Entity.movementInputToVelocity(
                    event.movementInput,
                    event.speed,
                    rotation.yaw
                )
            }
        }
    }

    @Suppress("unused")
    private val onPackerSend = handle<PacketEvent.Send> { event ->
        val rotation = when (val packet = event.packet) {
            is PlayerMoveC2SPacket -> {
                if (!packet.changeLook) {
                    return@handle
                }

                Rotation(packet.yaw, packet.pitch, true)
            }

            is PlayerPositionLookS2CPacket -> Rotation(packet.change.yaw, packet.change.pitch, true)
            is PlayerInteractItemC2SPacket -> Rotation(packet.yaw, packet.pitch, true)

            else -> return@handle
        }

        serverRotation = rotation
    }

    /**
     * For movement correction [MovementCorrection.SILENT]
     */
    fun transformInput(input: DirectionalInput): DirectionalInput {
        val player = mc.player ?: return input
        val target = rotationTarget ?: return input
        val rotation = currentRotation ?: return input

        if (target.correction != MovementCorrection.SILENT) {
            return input
        }

        val z = KeyboardInput.getMovementMultiplier(input.forwards, input.backwards)
        val x = KeyboardInput.getMovementMultiplier(input.left, input.right)

        val deltaYaw = player.yaw - rotation.yaw

        val newX: Float = x * MathHelper.cos(deltaYaw * 0.017453292f) - z *
                MathHelper.sin(deltaYaw * 0.017453292f)
        val newZ: Float = z * MathHelper.cos(deltaYaw * 0.017453292f) + x *
                MathHelper.sin(deltaYaw * 0.017453292f)

        val movementSideways = Math.round(newX)
        val movementForward = Math.round(newZ)

        return DirectionalInput(movementForward.toFloat(), movementSideways.toFloat())
    }
}