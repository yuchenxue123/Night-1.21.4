package cute.neko.night.features.module.movement.speed.modes

import cute.neko.night.event.EventState
import cute.neko.night.event.handler
import cute.neko.night.event.events.game.player.PlayerMotionEvent
import cute.neko.night.utils.entity.moving
import cute.neko.night.utils.entity.strafe

object SpeedMotion : SpeedMode("Motion") {

    private val horizontalSpeed by float("HorizontalSpeed", 0.5f, 0.1f..7f)
    private val verticalSpeed by float("VerticalSpeed", 0.42f, 0.1f..2f)

    private val fastStop by boolean("FastStop", true)

    @Suppress("unused")
    private val onPlayerMotionPre = handler<PlayerMotionEvent> { event ->
        if (event.state != EventState.PRE) {
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