package cute.neko.night.mod.mousetweaks

import net.minecraft.screen.slot.Slot
import net.minecraft.screen.slot.SlotActionType

/**
 * @author yuchenxue
 * @date 2025/07/17
 */

abstract class AbstractContainerScreenHandler : ScreenHandler {

    abstract fun click(slot: Slot, action: SlotActionType)

    abstract fun getOverSlot(mouseX: Double, mouseY: Double): Slot?

}