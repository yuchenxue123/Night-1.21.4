package cute.neko.night.utils.rotation

import cute.neko.night.utils.entity.box
import cute.neko.night.utils.extensions.*
import cute.neko.night.utils.interfaces.Accessor
import cute.neko.night.utils.rotation.data.Rotation
import net.minecraft.entity.Entity
import net.minecraft.util.math.Box
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Vec3d
import org.joml.Vector2f
import kotlin.math.atan2
import kotlin.math.hypot
import kotlin.math.sqrt

/**
 * @author yuchenxue
 * @date 2025/05/10
 */

object RotationUtils : Accessor {

    val gcd: Double
        get() {
            val f = mc.options.mouseSensitivity.value * 0.6 + 0.2
            return f * f * f * 8.0 * 0.15
        }

    fun getNearestPoint(eyes: Vec3d, box: Box): Vec3d {
        val origin = doubleArrayOf(eyes.x, eyes.y, eyes.z)
        val destMins = doubleArrayOf(box.minX, box.minY, box.minZ)
        val destMaxs = doubleArrayOf(box.maxX, box.maxY, box.maxZ)

        for (i in 0..2) {
            origin[i] = origin[i].coerceIn(destMins[i], destMaxs[i])
        }

        return Vec3d(origin[0], origin[1], origin[2])
    }

    /**
     * Transform vec to rotation
     *
     * @param vec           target vec
     * @param predict       simple predict pos
     * @param fromEntity    from entity
     *
     * @return transformed rotation
     */
    fun toRotation(vec: Vec3d, predict: Boolean = false, fromEntity: Entity = mc.player!!): Rotation {
        val eyesPos = fromEntity.eyePos

        if (predict) eyesPos.add(fromEntity.velocity.x, fromEntity.velocity.y, fromEntity.velocity.z)

        val (diffX, diffY, diffZ) = vec - eyesPos
        return Rotation(
            MathHelper.wrapDegrees(atan2(diffZ, diffX).toDegrees().toFloat() - 90f),
            MathHelper.wrapDegrees(
                -atan2(diffY, sqrt(diffX * diffX + diffZ * diffZ)).toDegrees().toFloat()
            )
        )
    }

    fun toRotation(vec: Vec3d, eyesPos: Vec3d): Rotation {
        val (diffX, diffY, diffZ) = vec - eyesPos
        return Rotation(
            MathHelper.wrapDegrees(atan2(diffZ, diffX).toDegrees().toFloat() - 90f),
            MathHelper.wrapDegrees(
                -atan2(diffY, sqrt(diffX * diffX + diffZ * diffZ)).toDegrees().toFloat()
            )
        )
    }

    /**
     * Calculate difference between two angle points
     */
    fun angleDifference(a: Float, b: Float) = MathHelper.wrapDegrees(a - b)

    /**
     * Calculate difference between two rotations
     */
    fun angleDifferences(a: Rotation, b: Rotation) =
        Vector2f(
            angleDifference(a.yaw, b.yaw),
            angleDifference(a.pitch, b.pitch)
        )

    fun rotationDifference(target: Rotation, current: Rotation) =
        hypot(angleDifference(target.yaw, current.yaw), angleDifference(current.pitch, current.pitch))

    fun rotationDifference(entity: Entity): Float =
        rotationDifference(toRotation(entity.box.center), RotationManager.serverRotation)
}