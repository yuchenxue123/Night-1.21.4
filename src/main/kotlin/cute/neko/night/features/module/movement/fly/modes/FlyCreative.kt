package cute.neko.night.features.module.movement.fly.modes

import cute.neko.night.event.tickHandler

object FlyCreative : FlyMode("Creative") {

    override fun enable() {
        player.abilities.allowFlying = true
    }

    override fun disable() {
        player.abilities.allowFlying = false
        player.abilities.flying = false
    }

    private val repeatable = tickHandler {
        player.abilities.flying = true
    }
}