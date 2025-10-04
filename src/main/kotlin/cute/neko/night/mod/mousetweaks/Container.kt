package cute.neko.night.mod.mousetweaks

import cute.neko.night.injection.mixins.accessor.HandledScreenAccessor
import net.minecraft.client.gui.screen.ingame.HandledScreen
import net.minecraft.screen.slot.Slot
import net.minecraft.screen.slot.SlotActionType

fun HandledScreen<*>.getOverSlot(mouseX: Double, mouseY: Double): Slot? {
    return (this as HandledScreenAccessor).`neko$getOverSlot`(mouseX, mouseY)
}

fun HandledScreen<*>.handleMouseClick(slot: Slot, slotId: Int, button: Int, type: SlotActionType) {
    (this as HandledScreenAccessor).`neko$clickSlot`(slot, slotId, button, type)
}