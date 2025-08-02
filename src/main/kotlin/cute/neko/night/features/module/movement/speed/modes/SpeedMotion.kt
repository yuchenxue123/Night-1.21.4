package cute.neko.night.features.module.movement.speed.modes

import cute.neko.event.LifecycleEventState
import cute.neko.event.handler
import cute.neko.night.event.events.game.player.PlayerMotionEvent
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

    @Suppress("unused")
    private val onPlayerMotionPre = handler<PlayerMotionEvent> { event ->
        if (event.state != LifecycleEventState.PRE) {
            return@handler
        }

        if (player.isSneaking) {
            return@handler
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