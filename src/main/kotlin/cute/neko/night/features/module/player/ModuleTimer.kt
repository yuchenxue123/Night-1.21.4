package cute.neko.night.features.module.player

import cute.neko.event.handler
import cute.neko.night.event.events.game.player.PlayerTickEvent
import cute.neko.night.features.module.ClientModule
import cute.neko.night.features.module.ModuleCategory
import cute.neko.night.utils.client.Timer

/**
 * @author yuchenxue
 * @date 2025/07/03
 */

object ModuleTimer : ClientModule(
    "Timer",
    ModuleCategory.PLAYER
) {

    private val timerSpeed by float("TimerSpeed", 1.5f, 0.2f..2f)

    override fun disable() {
        Timer.reset()
    }

    @Suppress("unused")
    private val onPlayerTick = handler<PlayerTickEvent> {
        Timer.set(timerSpeed)
    }
}