package cute.neko.night.features.module.movement.speed.modes

import cute.neko.night.event.events.game.player.PlayerMotionEvent
import cute.neko.night.event.handle
import cute.neko.night.utils.entity.moving
import cute.neko.night.utils.entity.strafe

/**
 * @author yuchenxue
 * @date 2025/07/21
 */

object SpeedMotion : SpeedMode("Motion") {

    private val horizontalSpeed by float("HorizontalSpeed", 0.5f, 0.1f..7f)
    private val verticalSpeed by float("VerticalSpeed", 0.42f, 0.1f..2f)

    private val fastStop by boolean("FastStop", true)

    private val onPlayerMotionPre = handle<PlayerMotionEvent.Pre> {
        if (player.isSneaking) {
            return@handle
        }

        if (player.moving && player.isOnGround) {
            player.velocity.y = verticalSpeed.toDouble()
        }

        player.strafe(
            speed = horizontalSpeed.toDouble(),
            fastStop = fastStop
        )
    }
}