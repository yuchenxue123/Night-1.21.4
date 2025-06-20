package cute.neko.night.module.player

import cute.neko.night.event.events.game.player.PlayerTickEvent
import cute.neko.night.event.handle
import cute.neko.night.module.ClientModule
import cute.neko.night.module.ModuleCategory
import cute.neko.night.utils.player.inventory.isRubbish
import cute.neko.night.utils.time.TimeTracker
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen
import net.minecraft.screen.slot.SlotActionType
import org.lwjgl.glfw.GLFW

/**
 * @author yuchenxue
 * @date 2025/06/14
 */

object ModuleStealer : ClientModule(
    "Stealer",
    ModuleCategory.PLAYER,
    key = GLFW.GLFW_KEY_G
) {

    private val delay by int("Delay", 50, 0..2000, 50)

    private val closeDelay by int("CloseDelay", 200, 0..2000, 50)

    private var active = false
    private var size = 0
    private var slotIndex = 0

    // finish delay
    private var finished = false

    private val tracker = TimeTracker()

    override fun disable() {
        active = false
        slotIndex = 0
        finished = false
    }

    private val onPlayerTick = handle<PlayerTickEvent> {
        val screen = mc.currentScreen

        if (screen !is GenericContainerScreen) {
            stop()
        }

        if (screen is GenericContainerScreen && !active && !finished) {
            size = screen.screenHandler.slots.size - 36 // player inventory has 36 slots

            if (size != 0) {
                active = true
            }
        }

        if (active && screen is GenericContainerScreen && !finished) {
            val screenHandler = screen.screenHandler

            if (tracker.hasPassedTime(delay.toLong())) {
                // skip all useless item.
                while ((!screenHandler.getSlot(slotIndex).hasStack()
                            || screenHandler.getSlot(slotIndex).stack.isRubbish())
                    && slotIndex < size - 1
                ) {
                    slotIndex++
                }

                val slot = screenHandler.getSlot(slotIndex)

                if (slot.hasStack()) {
                    interactionManager.clickSlot(
                        screenHandler.syncId,
                        slot.id,
                        0,
                        SlotActionType.QUICK_MOVE,
                        player
                    )
                }

                slotIndex++
                tracker.reset()

                // if out of slot size, stop it.
                if (slotIndex >= size) {
                    stop()
                    finished = true
                }
            }
        }

        if (screen is GenericContainerScreen && finished && tracker.hasPassedTime(closeDelay.toLong())) {
            player.closeHandledScreen()
            finished = false
        }
    }

    private fun stop() {
        active = false
        slotIndex = 0
    }
}