package cute.neko.night.features.module.player.nofall.modes

import cute.neko.night.event.EventState
import cute.neko.night.event.handler
import cute.neko.night.event.events.game.player.PlayerMotionEvent
import cute.neko.night.utils.client.Timer
import cute.neko.night.utils.entity.hasFalldownDamage
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket

object NoFallHypixel : NoFallMode("Hypixel") {

    private var switch = false

    override fun disable() {
        switch = false

        Timer.reset()
    }

    @Suppress("unused")
    private val onMotionPre = handler<PlayerMotionEvent> { event ->
        if (event.state != EventState.PRE) {
            return@handler
        }

        if (voidCheck()) {
            switch = false
            Timer.reset()
            return@handler
        }

        if (switch) {
            Timer.reset()
            switch = false
        }

        if (player.hasFalldownDamage) {
            Timer.set(0.5f)
            network.sendPacket(PlayerMoveC2SPacket.OnGroundOnly(true, false))
            player.onLanding()
            switch = true
        }
    }
}