package cute.neko.night.utils.rotation.util

import net.minecraft.util.math.Direction

/**
 * @author yuchenxue
 * @date 2025/05/11
 */

enum class BlockSearchDirections {

    /**
     * For block walk common
     */
    NORMAL {
        override val directions: Array<Direction>
            get() = arrayOf(Direction.UP, Direction.WEST, Direction.EAST, Direction.SOUTH, Direction.NORTH)
    },

    /**
     * For block walk enabled downwards
     */
    DOWNWARDS {
        override val directions: Array<Direction>
            get() = arrayOf(Direction.DOWN, Direction.WEST, Direction.EAST, Direction.SOUTH, Direction.NORTH)
    },

    /**
     * For block walk enabled keep y
     */
    HORIZONTAL {
        override val directions: Array<Direction>
            get() = arrayOf(Direction.WEST, Direction.EAST, Direction.SOUTH, Direction.NORTH)
    },
    ;

    abstract val directions: Array<Direction>
}