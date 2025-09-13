package cute.neko.night.utils.movement

import net.minecraft.util.math.Direction
import kotlin.math.abs

enum class MoveDirection(val yaw: Float, val diagonal: Boolean) {
    NORTH(0f, false),
    NORTHEAST(45f, true),
    EAST(90f, false),
    SOUTHEAST(135f, true),
    SOUTH(180f, false),
    SOUTHWEST(225f, true),
    WEST(270f, false),
    NORTHWEST(315f, true),

    ;

    companion object {
        fun parse(direction: Float, threshold: Float): MoveDirection {
            require(threshold in 0f..45f) { "Threshold must be between 0 and 45" }

            val yaw = abs((direction + 360f) % 360f)

            return when {
                yaw < threshold || yaw >= 360f - threshold -> NORTH

                yaw in threshold..<90f - threshold -> NORTHEAST

                yaw in 90f - threshold..<90f + threshold -> EAST

                yaw in 90f + threshold..<180f - threshold -> SOUTHEAST

                yaw in 180f - threshold..<180 + threshold -> SOUTH

                yaw in 180f + threshold..<270f - threshold -> SOUTHWEST

                yaw in 270f - threshold..<270f + threshold -> WEST

                yaw in 270f + threshold..<360 - threshold -> NORTHWEST

                else -> NORTH
            }
        }
    }

    fun toGameDirection(): Array<Direction> {
        return when (this) {
            NORTH -> arrayOf(Direction.NORTH)
            NORTHEAST -> arrayOf(Direction.NORTH, Direction.EAST)
            EAST -> arrayOf(Direction.EAST)
            SOUTHEAST -> arrayOf(Direction.SOUTH, Direction.EAST)
            SOUTH -> arrayOf(Direction.SOUTH)
            SOUTHWEST -> arrayOf(Direction.SOUTH, Direction.WEST)
            WEST -> arrayOf(Direction.WEST)
            NORTHWEST -> arrayOf(Direction.NORTH, Direction.WEST)
        }
    }
}