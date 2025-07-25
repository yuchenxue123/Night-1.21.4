package cute.neko.night.features.module.movement.fly.modes

import cute.neko.event.LifecycleEventState
import cute.neko.event.handler
import cute.neko.night.event.events.game.player.PlayerMotionEvent
import cute.neko.night.event.events.game.player.PlayerTickEvent
import cute.neko.night.utils.entity.strafe

/**
 * @author yuchenxue
 * @date 2025/07/07
 */

object FlyMotion : FlyMode("Motion") {
    private val horizontalSpeed by float("HorizontalSpeed", 0.5f, 0.1f..10f)
    private val verticalSpeed by float("VerticalSpeed", 0.5f, 0.1f..10f)
    private val glide by float("Glide", 0.0f, -1f..1f, 0.02f) { !noStayInAir }
    private val noStayInAir by boolean("NoStayInAir", false)
    private val spoofGround by boolean("SpoofGround", false)

    private val onPlayerTick = handler<PlayerTickEvent> {

        player.strafe(horizontalSpeed.toDouble())

        when {
            noStayInAir -> {
                if (mc.options.jumpKey.isPressed) {
                    player.velocity.y = verticalSpeed.toDouble()
                }
            }

            else -> {
                player.velocity.y = when {
                    mc.options.jumpKey.isPressed -> verticalSpeed.toDouble()
                    mc.options.sneakKey.isPressed -> -horizontalSpeed.toDouble()
                    else -> glide.toDouble()
                }
            }
        }
    }

    private val onPlayerMotionPre = handler<PlayerMotionEvent> { event ->
        if (event.state != LifecycleEventState.PRE) {
            return@handler
        }

        if (spoofGround) {
            event.onGround = true
        }
    }
}