package cute.neko.night.features.module.player.nofall.modes

import cute.neko.night.event.handler
import cute.neko.night.event.events.game.misc.MovementInputEvent
import cute.neko.night.event.events.game.network.PacketEvent
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket

object NoFallGrim : NoFallMode("Grim") {

    private var lastOnGround = false
    private var lastFallDistance = 0f
    private var shouldJump = false

    override fun disable() {
        lastOnGround = false
        lastFallDistance = 0f
        shouldJump = false
    }

    @Suppress("unused")
    private val onPacket = handler<PacketEvent> { event ->
        val packet = event.packet

        if (packet is PlayerMoveC2SPacket) {
            if (packet.onGround && !lastOnGround && lastFallDistance > 4f) {
                packet.x += 1024
                packet.z += 1024
                packet.onGround = false
                player.onLanding()
                shouldJump = true
            }

            lastOnGround = packet.onGround
            lastFallDistance = player.fallDistance
        }
    }

    @Suppress("unused")
    private val onMovementInput = handler<MovementInputEvent> { event ->
        if (shouldJump) {
            event.jump = true
            shouldJump = false
        }
    }
}