package cute.neko.night.utils.entity

import cute.neko.night.utils.client.player
import cute.neko.night.utils.extensions.squared
import cute.neko.night.utils.movement.DirectionalInput
import cute.neko.night.utils.rotation.data.Rotation
import net.minecraft.client.network.ClientPlayerEntity
import net.minecraft.entity.Entity
import net.minecraft.item.consume.UseAction
import net.minecraft.util.math.Box
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

/**
 * @author yuchenxue
 * @date 2025/05/10
 */

val Entity.rotation: Rotation
    get() = Rotation(yaw, pitch, true)

fun ClientPlayerEntity.setRotation(rotation: Rotation) {
    rotation.normalize().let {
        lastYaw = it.yaw
        lastPitch = it.pitch

        renderYaw = it.yaw
        lastRenderYaw = it.yaw

        yaw = it.yaw
        pitch = it.pitch
    }
}

val Entity.box: Box
    get() = boundingBox.expand(targetingMargin.toDouble())

val ClientPlayerEntity.moving
    get() = input.movementInput.x != 0.0f || input.movementInput.y != 0.0f

val ClientPlayerEntity.isBlockAction: Boolean
    get() = isUsingItem && activeItem.useAction == UseAction.BLOCK

val ClientPlayerEntity.direction: Float
    get() = getMovementDirectionOfInput(DirectionalInput(input))

fun ClientPlayerEntity.getMovementDirectionOfInput(input: DirectionalInput): Float {
    return getMovementDirectionOfInput(this.yaw, input)
}

val ClientPlayerEntity.sqrtSpeed
    get() = sqrt(velocity.x.squared() + velocity.z.squared())

fun ClientPlayerEntity.strafe(
    speed: Double = sqrtSpeed,
    strength: Double = 1.0,
    input: DirectionalInput? = DirectionalInput(player.input),
    yaw: Float = player.getMovementDirectionOfInput(input ?: DirectionalInput(player.input)),
) {
    if (!moving) {
        return
    }

    val prevX = x * (1.0 - strength)
    val prevZ = z * (1.0 - strength)
    val useSpeed = speed * strength

    val angle = Math.toRadians(yaw.toDouble())
    val x = (-sin(angle) * useSpeed) + prevX
    val z = (cos(angle) * useSpeed) + prevZ

    velocity.x = x
    velocity.z = z
}

fun getMovementDirectionOfInput(facingYaw: Float, input: DirectionalInput): Float {
    var actualYaw = facingYaw
    var forward = 1f

    if (input.backwards) {
        actualYaw += 180f
        forward = -0.5f
    } else if (input.forwards) {
        forward = 0.5f
    }

    if (input.left) {
        actualYaw -= 90f * forward
    }
    if (input.right) {
        actualYaw += 90f * forward
    }

    return actualYaw
}

