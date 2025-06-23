package cute.neko.night.features.module.player.nofall.modes

import cute.neko.night.event.events.game.player.PlayerMotionEvent
import cute.neko.night.event.handle
import cute.neko.night.utils.client.Timer
import cute.neko.night.utils.client.network
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket

/**
 * @author yuchenxue
 * @date 2025/06/23
 */

object NoFallTimer : NoFallMode("Timer") {

    private var switch = false

    override fun disable() {
        switch = false

        Timer.reset()
    }

    private val onMotionPre = handle<PlayerMotionEvent.Pre> {

        if (voidCheck()) {
            switch = false
            Timer.reset()
            return@handle
        }

        if (player.fallDistance - player.velocity.y > 3.3) {
            Timer.set(0.5f)
            network.sendPacket(PlayerMoveC2SPacket.OnGroundOnly(true, false))
            player.fallDistance = 0f
            switch = true
        } else if (switch) {
            Timer.reset()
            switch = false
        }
    }
}