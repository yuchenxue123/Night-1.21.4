package cute.neko.night.features.module.player

import cute.neko.night.event.handler
import cute.neko.night.event.events.game.player.PlayerTickEvent
import cute.neko.night.features.module.ClientModule
import cute.neko.night.features.module.ModuleCategory
import cute.neko.night.utils.extensions.getState
import cute.neko.night.utils.movement.DirectionalInput
import net.minecraft.client.option.KeyBinding

object ModuleEagle : ClientModule(
    "Eagle",
    ModuleCategory.PLAYER
) {

    private val onlyGround by boolean("OnlyGround", true)
    private val onlyBack by boolean("OnlyBack", false)

    private var sneaked = false

    override fun disable() {
        if (sneaked) release()
    }

    @Suppress("unused")
    private val onPlayerTick = handler<PlayerTickEvent> {
        if (onlyGround && !player.isOnGround
            || onlyBack && !DirectionalInput.of(player.input).backwards)
        {
            if (sneaked) release()
            return@handler
        }

        val below = player.blockPos.down().getState()

        if (below?.isReplaceable == true)
        {
            press()
            sneaked = true
        }
        else if (sneaked)
        {
            release()
            sneaked = false
        }
    }

    private fun press() {
        KeyBinding.setKeyPressed(mc.options.sneakKey.boundKey, true)
    }

    private fun release() {
        KeyBinding.setKeyPressed(mc.options.sneakKey.boundKey, false)
    }
}