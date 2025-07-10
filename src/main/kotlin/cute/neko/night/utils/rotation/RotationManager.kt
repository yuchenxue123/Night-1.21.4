package cute.neko.night.utils.rotation

import cute.neko.night.event.EventListener
import cute.neko.night.event.events.game.network.PacketEvent
import cute.neko.night.event.events.game.player.PlayerMotionEvent
import cute.neko.night.event.events.game.player.PlayerVelocityEvent
import cute.neko.night.event.handle
import cute.neko.night.utils.entity.box
import cute.neko.night.utils.entity.rotation
import cute.neko.night.utils.entity.setRotation
import cute.neko.night.utils.interfaces.Accessor
import cute.neko.night.utils.kotlin.Priority
import cute.neko.night.utils.movement.DirectionalInput
import cute.neko.night.utils.rotation.RotationUtils.angleDifference
import cute.neko.night.utils.rotation.data.Rotation
import cute.neko.night.utils.rotation.data.RotationRequest
import cute.neko.night.utils.rotation.features.MovementCorrection
import net.minecraft.client.input.KeyboardInput
import net.minecraft.entity.Entity
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket
import net.minecraft.util.math.MathHelper
import java.util.*
import kotlin.math.abs
import kotlin.math.hypot


/**
 * @author yuchenxue
 * @date 2025/05/09
 */

object RotationManager : EventListener, Accessor {

    /**
     * 请求转头储存，并按优先级排序
     */
    private val requests = PriorityQueue<RotationRequest>(
        compareByDescending { it.priority }
    )

    /**
     * 活动的请求转头
     */
    val activeRequest: RotationRequest?
        get() = requests.peek() ?: null

    /**
     * 客户端转头，自己的目标转头
     */
    var currentRotation: Rotation? = null
        set(value) {
            previousRotation = if (value == null) {
                null
            } else {
                field ?: mc.player?.rotation
            }

            field = value
        }

    /**
     * 更新前的转头
     */
    var previousRotation: Rotation? = null

    /**
     * 服务器转头，其他玩家看到的转头
     */
    var serverRotation: Rotation = Rotation.ZERO

    /**
     * 请求转头
     */
    fun request(request: RotationRequest) {
        requests.removeAll { it.listener == request.listener }
        requests.add(request)

        update()
    }

    fun request(
        listener: EventListener,
        rotation: Rotation,
        priority: Int = Priority.ROTATION_NORMAL,
        horizontalSpeed: Float = 180f,
        verticalSpeed: Float = 180f,
        correction: MovementCorrection = MovementCorrection.NONE
    ) {
        request(RotationRequest(
            listener,
            rotation,
            priority,
            horizontalSpeed,
            verticalSpeed,
            correction
        ))
    }

    fun request(
        listener: EventListener,
        entity: Entity,
        priority: Int = Priority.ROTATION_NORMAL,
        horizontalSpeed: Float = 180f,
        verticalSpeed: Float = 180f,
        correction: MovementCorrection = MovementCorrection.NONE
    ) {
        request(RotationRequest(
            listener,
            RotationUtils.toRotation(entity.box.center).normalize(),
            priority,
            horizontalSpeed,
            verticalSpeed,
            correction
        ))
    }

    fun remove(listener: EventListener) {
        requests.removeAll { it.listener == listener }

        update()
    }

    fun reset() {
        requests.clear()

        update()
    }

    private fun update() {
        if (requests.isEmpty() || activeRequest == null) {
            currentRotation = null
            return
        }

        activeRequest?.let { request ->
            val lastRotation = currentRotation ?: player.rotation

            val yawDifference = angleDifference(request.rotation.yaw, lastRotation.yaw)
            val pitchDifference = angleDifference(request.rotation.pitch, lastRotation.pitch)

            val rotationDifference = hypot(abs(yawDifference), abs(pitchDifference))

            val (horizontal, vertical) = request.horizontalSpeed to request.verticalSpeed

            val straightLineYaw = abs(yawDifference / rotationDifference) * horizontal
            val straightLinePitch = abs(pitchDifference / rotationDifference) * vertical

            val nextRotation = Rotation(
                lastRotation.yaw + yawDifference.coerceIn(-straightLineYaw, straightLineYaw),
                lastRotation.pitch + pitchDifference.coerceIn(-straightLinePitch, straightLinePitch)
            )

            currentRotation = nextRotation
        }
    }


    @Suppress("unused")
    private val onPlayerMotionPre = handle<PlayerMotionEvent.Pre> { event ->
        val correction = activeRequest?.correction ?: MovementCorrection.NONE

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
        val correction = activeRequest?.correction ?: MovementCorrection.NONE

        currentRotation?.let {
            if (correction != MovementCorrection.NONE) {
                event.velocity = Entity.movementInputToVelocity(
                    event.movementInput,
                    event.speed,
                    it.yaw
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
        val rotation = currentRotation ?: return input

        val correction = activeRequest?.correction ?: MovementCorrection.NONE

        if (correction != MovementCorrection.SILENT) {
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