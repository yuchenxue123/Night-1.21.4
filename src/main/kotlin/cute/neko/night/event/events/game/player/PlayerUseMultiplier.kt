package cute.neko.night.event.events.game.player

import cute.neko.night.event.CancellableEvent
import cute.neko.night.event.EventManager
import cute.neko.night.utils.entity.div
import cute.neko.night.utils.entity.multiply
import cute.neko.night.utils.entity.setMovementVector
import net.minecraft.client.input.Input

class PlayerUseMultiplier(
    var forward: Float,
    var sideways: Float
) : CancellableEvent() {

    companion object {

        @JvmStatic
        fun hookCustomMultiplier(input: Input) {
            input.setMovementVector(
                input.movementInput.div(0.2f, 0.2f)
            )

            // then
            val playerUseMultiplier = PlayerUseMultiplier(0.2f, 0.2f)
            EventManager.callEvent(playerUseMultiplier)

            if (playerUseMultiplier.cancelled) {
                return
            }

            input.setMovementVector(
                input.movementInput.multiply(playerUseMultiplier.forward, playerUseMultiplier.sideways)
            )
        }
    }

}