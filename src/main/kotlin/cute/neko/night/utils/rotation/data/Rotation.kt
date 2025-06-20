package cute.neko.night.utils.rotation.data

import cute.neko.night.utils.client.player
import cute.neko.night.utils.entity.rotation
import cute.neko.night.utils.rotation.RotationManager
import cute.neko.night.utils.rotation.RotationUtils
import cute.neko.night.utils.rotation.RotationUtils.angleDifference
import net.minecraft.util.math.Vec3d
import kotlin.math.roundToInt

/**
 * @author yuchenxue
 * @date 2025/05/09
 */

class Rotation(
    var yaw: Float,
    var pitch: Float,
    var normalized: Boolean = false,
) {

    companion object {
        val ZERO = Rotation(0f, 0f)
    }

    val directionVector: Vec3d
        get() = Vec3d.fromPolar(pitch, yaw)

    /**
     * Normalize the rotation
     *
     * @return rotation after modify
     */
    fun normalize(): Rotation {
        if (normalized) return this

        val gcd = RotationUtils.gcd

        // We use the [currentRotation] to calculate the normalized rotation, if it's null, we use
        // the player's rotation
        val currentRotation = RotationManager.currentRotation ?: player.rotation

        // get rotation differences
        val diff = currentRotation.rotationDeltaTo(this)

        // proper rounding
        val g1 = (diff.deltaYaw / gcd).roundToInt() * gcd
        val g2 = (diff.deltaPitch / gcd).roundToInt() * gcd

        // fix rotation
        val yaw = currentRotation.yaw + g1.toFloat()
        val pitch = currentRotation.pitch + g2.toFloat()

        return Rotation(yaw, pitch.coerceIn(-90f, 90f), normalized = true)
    }

    fun rotationDeltaTo(other: Rotation): RotationDelta {
        return RotationDelta(
            angleDifference(other.yaw, this.yaw),
            angleDifference(other.pitch, this.pitch)
        )
    }

    operator fun plus(rotation: Rotation): Rotation {
        return Rotation(yaw + rotation.yaw, pitch + rotation.pitch)
    }

    operator fun minus(rotation: Rotation): Rotation {
        return Rotation(yaw - rotation.yaw, pitch - rotation.pitch)
    }

    operator fun times(scalar: Float): Rotation {
        return Rotation(yaw * scalar, pitch * scalar)
    }

    operator fun div(scalar: Float): Rotation {
        return Rotation(yaw / scalar, pitch / scalar)
    }
}