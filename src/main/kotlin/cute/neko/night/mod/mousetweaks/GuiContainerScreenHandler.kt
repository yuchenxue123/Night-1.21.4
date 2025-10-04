package cute.neko.night.mod.mousetweaks

import net.minecraft.client.gui.screen.ingame.HandledScreen
import net.minecraft.screen.slot.Slot
import net.minecraft.screen.slot.SlotActionType

open class GuiContainerScreenHandler(
    val container: HandledScreen<*>
) : AbstractContainerScreenHandler() {
    override fun getOverSlot(mouseX: Double, mouseY: Double): Slot? {
        return container.getOverSlot(mouseX, mouseY)
    }

    override fun click(slot: Slot, action: SlotActionType) {
        if (isIgnored(slot)) {
            return
        }

        container.handleMouseClick(slot, slot.id, MouseButton.LEFT.id, action)
    }

    override fun isIgnored(slot: Slot): Boolean {
        return false
    }
}