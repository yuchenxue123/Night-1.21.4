package cute.neko.night.features.module.player.scaffold.features

import cute.neko.night.event.events.game.player.PlayerTickEvent
import cute.neko.night.event.handle
import cute.neko.night.features.module.player.scaffold.ModuleScaffold
import cute.neko.night.features.setting.config.Configurable
import cute.neko.night.features.setting.config.types.EmptyConfigurable
import cute.neko.night.utils.extensions.sequence
import cute.neko.night.utils.kotlin.Priority
import cute.neko.night.utils.rotation.util.BlockSearchDirections
import net.minecraft.block.BlockState
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction

/**
 * @author yuchenxue
 * @date 2025/07/08
 */

object ScaffoldSearchBlock : Configurable("ScaffoldSearchBlock") {

    /**
     * 这个结果是放置时瞄准的方块位置和面
     */
    var result: SearchResult? = null

    fun reset() {
        result = null
    }

    fun working() {
        val basePosition = player.blockPos.down()
        val (horizontal, vertical) = 5 to 3

        basePosition.sequence(
            -horizontal..horizontal,
            -vertical..0,
            -horizontal..horizontal
        ).filter { pos ->
            world.getBlockState(pos).isReplaceable
        }.sortedBy {
            player.squaredDistanceTo(it.toCenterPos())
        }.map {
            it to world.getBlockState(it)
        }.forEach { (pos, state) ->
            if (state.isSolidBlock(world, pos) || search(pos, state)) {
                return
            }
        }
    }

    private fun search(
        pos: BlockPos,
        state: BlockState
    ): Boolean {
        if (!state.isReplaceable) {
            return false
        }

        val directions = BlockSearchDirections.NORMAL.directions

        directions.forEach directions@ { direction ->
            val neighbor = pos.offset(direction.opposite)

            val neighborState = world.getBlockState(neighbor)
            if (!neighborState.isSolidBlock(world, neighbor) && neighborState.isReplaceable) {
                return@directions
            }

            result = SearchResult(neighbor, direction)
            return true
        }

        return false
    }

    data class SearchResult(
        val pos: BlockPos,
        val direction: Direction
    )
}