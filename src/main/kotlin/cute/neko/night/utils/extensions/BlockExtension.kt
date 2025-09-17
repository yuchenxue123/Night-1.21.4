package cute.neko.night.utils.extensions

import cute.neko.night.utils.client.mc
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3i

fun Vec3i.toBlockPos() = BlockPos(this)

fun BlockPos.getState() = mc.world?.getBlockState(this)

fun BlockPos.getBlock() = getState()?.block

fun BlockPos.sequence(
    xRange: IntRange,
    yRange: IntRange,
    zRange: IntRange,
): Sequence<BlockPos> {
    return sequence genshin@ {
        for (x in xRange) {
            for (y in yRange) {
                for (z in zRange) {
                    yield(this@sequence.add(x, y, z))
                }
            }
        }
    }
}