package cute.neko.night.features.module.movement.noslow.modes

import cute.neko.night.event.events.game.player.PlayerUseMultiplier
import cute.neko.night.event.handler

object NoSlowNone : NoSlowMode("None") {

    @Suppress("unused")
    private val onUseMultiplier = handler<PlayerUseMultiplier> { event ->
        event.cancel()
    }
}