package cute.neko.night.features.module.movement

import cute.neko.event.handler
import cute.neko.night.event.events.game.player.PlayerTickEvent
import cute.neko.night.features.module.ClientModule
import cute.neko.night.features.module.ModuleCategory
import net.minecraft.client.option.KeyBinding

/**
 * @author yuchenxue
 * @date 2025/01/12 - 20:24
 */

object ModuleSprint : ClientModule(
    "Sprint",
    ModuleCategory.MOVEMENT
) {

    @Suppress("unused")
    private val onPlayerTick = handler<PlayerTickEvent> {
        if (mc.options.forwardKey.isPressed) {
            KeyBinding.setKeyPressed(mc.options.sprintKey.boundKey, true)
        }
    }
}