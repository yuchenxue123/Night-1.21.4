package cute.neko.night.features.module.movement.fly.modes

import cute.neko.night.event.EventState
import cute.neko.night.event.events.game.player.PlayerMotionEvent
import cute.neko.night.event.handler
import cute.neko.night.event.tickHandler
import cute.neko.night.utils.entity.strafe

/**
 * @author yuchenxue
 * @date 2025/07/07
 */

object FlyMotion : FlyMode("Motion") {
    private val horizontalSpeed by float("HorizontalSpeed", 0.5f, 0.1f..10f)
    private val verticalSpeed by float("VerticalSpeed", 0.5f, 0.1f..10f)
    private val glide by float("Glide", 0.0f, -1f..1f, 0.02f)
    private val spoofGround by boolean("SpoofGround", false)
    private val bypassVanilla by boolean("BypassVanilla", false)

    @Suppress("unused")
    private val tickHandler = tickHandler {
        player.strafe(horizontalSpeed.toDouble(), fastStop = true)

        player.velocity.y = when {
            mc.options.jumpKey.isPressed -> verticalSpeed.toDouble()
            mc.options.sneakKey.isPressed -> -horizontalSpeed.toDouble()
            else -> glide.toDouble()
        }

        if (bypassVanilla && player.age % 40 == 0) {
            waitTicks(1)
            player.velocity.y = -0.04
            waitTicks(1)
        }
    }

    @Suppress("unused")
    private val onPlayerMotion = handler<PlayerMotionEvent> { event ->
        if (event.state != EventState.PRE) {
            return@handler
        }

        if (spoofGround) {
            event.onGround = true
        }
    }
}