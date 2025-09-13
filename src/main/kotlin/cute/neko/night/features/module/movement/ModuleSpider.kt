package cute.neko.night.features.module.movement

import cute.neko.night.event.events.game.player.PlayerTickEvent
import cute.neko.night.event.handler
import cute.neko.night.features.module.ClientModule
import cute.neko.night.features.module.ModuleCategory

object ModuleSpider : ClientModule(
    "Spider",
    ModuleCategory.MOVEMENT
) {

    private val motion by float("Motion", 0.3f, 0.05f..1f, 0.05f)

    @Suppress("unused")
    private val onPlayerTick = handler<PlayerTickEvent> {

        if (player.horizontalCollision) {
            player.velocity.y = motion.toDouble()
        }
    }
}