package cute.neko.night.mod.mousetweaks

import cute.neko.night.utils.client.mc
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen
import net.minecraft.screen.slot.Slot

class GuiContainerCreativeScreenHandler(
    container: CreativeInventoryScreen,
) : GuiContainerScreenHandler(container) {
    override fun isIgnored(slot: Slot): Boolean {
        return (super.isIgnored(slot) || slot.inventory != mc.player?.getInventory());
    }
}