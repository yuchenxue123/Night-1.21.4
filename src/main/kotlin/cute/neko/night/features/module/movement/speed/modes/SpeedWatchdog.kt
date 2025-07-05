package cute.neko.night.features.module.movement.speed.modes

import cute.neko.night.event.events.game.player.PlayerTickEvent
import cute.neko.night.event.handle
import cute.neko.night.utils.entity.moving
import cute.neko.night.utils.entity.strafe
import net.minecraft.entity.effect.StatusEffects


/**
 * @author yuchenxue
 * @date 2025/06/04
 */

object SpeedWatchdog : SpeedMode("Watchdog") {

    private var fallTicks = 0

    private val onPlayerTick = handle<PlayerTickEvent> {

        if (player.velocity.y < .0) {
            fallTicks++
        } else {
            fallTicks = 0
        }


        if (player.isOnGround) {
            if (player.moving && !mc.options.jumpKey.isPressed) {
                player.jump()
            }

            val level = player.getStatusEffect(StatusEffects.SPEED)?.amplifier ?: 0

            val speed = if (level == 0) {
                0.48f
            } else {
                0.48f + (level + 1) * 0.12f
            }

            player.strafe(speed.toDouble())
        }
    }
}