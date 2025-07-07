package cute.neko.night.features.module.movement.speed.modes

import cute.neko.night.event.events.game.player.PlayerTickEvent
import cute.neko.night.event.handle
import cute.neko.night.features.module.misc.disabler.modes.DisablerWatchdog
import cute.neko.night.utils.entity.moving
import cute.neko.night.utils.entity.strafe
import cute.neko.night.utils.extensions.getBlock
import net.minecraft.block.SlabBlock
import net.minecraft.entity.effect.StatusEffects
import kotlin.math.round


/**
 * @author yuchenxue
 * @date 2025/06/04
 */

object SpeedWatchdog : SpeedMode("Watchdog") {

    private val onPlayerTick = handle<PlayerTickEvent> {

        low()
        if (player.isOnGround && player.moving) {
            player.jump()

            val level = player.getStatusEffect(StatusEffects.SPEED)?.amplifier ?: 0

            val speed = if (level == 0) {
                0.48f
            } else {
                0.48f + (level + 1) * 0.12f
            }

            player.strafe(speed.toDouble())
        }
    }

    private fun low() {
        val block = player.blockPos.getBlock()

        if (block is SlabBlock || !DisablerWatchdog.running || !DisablerWatchdog.disabled) {
            return
        }

        val y =  Math.round(player.y % 1.0 * 10000.0).toInt()

        when (y) {
            1138 -> player.velocity.y -= 0.13
            2031 -> player.velocity.y -= 0.2
            4200 -> player.velocity.y = 0.39
        }
    }
}