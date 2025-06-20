package cute.neko.night.module.movement

import cute.neko.night.event.events.game.player.PlayerTickEvent
import cute.neko.night.event.handle
import cute.neko.night.module.ClientModule
import cute.neko.night.module.ModuleCategory
import net.minecraft.client.MinecraftClient

/**
 * @author yuchenxue
 * @date 2025/01/12 - 20:24
 */

object ModuleSprint : ClientModule(
    "Sprint",
    ModuleCategory.MOVEMENT
) {

    @Suppress("unused")
    private val onPlayerTick = handle<PlayerTickEvent> {
        val mc = MinecraftClient.getInstance()
        val player = mc.player ?: return@handle

        if (mc.options.forwardKey.isPressed
            && !player.isSneaking && player.input.movementInput.y > 0.8f
            && !player.horizontalCollision) {
            player.isSprinting = true
        }
    }
}