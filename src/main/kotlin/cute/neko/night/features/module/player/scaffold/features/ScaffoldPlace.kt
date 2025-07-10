package cute.neko.night.features.module.player.scaffold.features

import cute.neko.night.features.setting.config.Configurable
import cute.neko.night.utils.extensions.times
import cute.neko.night.utils.interfaces.Accessor
import cute.neko.night.utils.rotation.RaytraceUtils
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction

/**
 * @author yuchenxue
 * @date 2025/07/08
 */

object ScaffoldPlace : Accessor {

    fun place(pos: BlockPos, direction: Direction) {
        val raytrace = RaytraceUtils.raytrace()

        if (player.eyePos.distanceTo(raytrace.pos) > player.blockInteractionRange) {
            return
        }

        if (!world.getBlockState(pos.offset(direction)).isReplaceable) {
            return
        }

        if (!RaytraceUtils.canSeePoint(player.eyePos, pos.toCenterPos().add(direction.doubleVector * 0.5))) {
            return
        }

        if (raytrace.blockPos == pos) {
            if (interactionManager.interactBlock(
                    player,
                    Hand.MAIN_HAND,
                    BlockHitResult(raytrace.pos, direction, pos, false)
                ).isAccepted) {
                player.swingHand(Hand.MAIN_HAND)
            }
        }
    }
}