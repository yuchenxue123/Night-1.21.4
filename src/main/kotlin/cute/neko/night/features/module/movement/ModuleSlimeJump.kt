package cute.neko.night.features.module.movement

import cute.neko.night.event.events.game.player.PlayerMoveEvent
import cute.neko.night.event.handler
import cute.neko.night.features.module.ClientModule
import cute.neko.night.features.module.ModuleCategory
import cute.neko.night.utils.extensions.getBlock
import net.minecraft.block.Blocks
import kotlin.math.min
import kotlin.math.pow

// from LiquidBounce discord
// for grim
// date 25/11/2
object ModuleSlimeJump : ClientModule("SlimeJump", ModuleCategory.MOVEMENT) {

    private val pow by float("Pow", 0.75f, 0.1f..1f)
    private val multiplier by float("Multiplier", 1.4f, 0.1f..2f)

    @Suppress("unused")
    private val onPlayerMove = handler<PlayerMoveEvent> { event ->
        if (player.isOnGround) {
            val pos = player.blockPos.down()
            val movement_y = event.movement.y

            val on_slime = pos.getBlock() == Blocks.SLIME_BLOCK

            if (on_slime && movement_y > 0 && movement_y != 0.41999998688697815) {

//                debug.debug("MY:" + event.movement.y)

                event.movement.y = bounce(
                    movement_y,
                    pow,
                    multiplier
                )

            }
        }
    }

    private fun bounce(y: Double, pow: Float, multiplier: Float): Double {
        val capped = min(y, 1.0)
        val boosted = capped.pow(pow.toDouble()) * multiplier
        return boosted.coerceAtMost(1.0)
    }
}