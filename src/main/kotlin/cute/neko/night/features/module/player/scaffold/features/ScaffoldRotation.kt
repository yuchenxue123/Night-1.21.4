package cute.neko.night.features.module.player.scaffold.features

import cute.neko.night.features.setting.config.Configurable
import cute.neko.night.utils.entity.direction
import cute.neko.night.utils.rotation.RaytraceUtils
import cute.neko.night.utils.rotation.data.Rotation
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction

object ScaffoldRotation : Configurable("ScaffoldRotation") {

    fun calculateRotation(pos: BlockPos, direction: Direction): Rotation {
        val yaw = (player.direction + 180f) % 360f

        var pitch = when {
            direction == Direction.UP -> 79f
            else -> {
                findPitch(pos, yaw) ?: 85f
            }
        }

        if (!player.groundCollision) {
            pitch += 5f
        }

        return Rotation(yaw, pitch)
    }

    private fun findPitch(pos: BlockPos, yaw: Float): Float? {
        for (i in generateSequence(-90f) { it + 1f }.takeWhile { it in -90f..90f }) {
            val blockPos = RaytraceUtils.raytrace(rotation = Rotation(yaw, i)).blockPos
            if (blockPos == pos) {
                return i
            }
        }

        return null
    }
}