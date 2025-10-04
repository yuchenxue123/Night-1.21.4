package cute.neko.night.mod.mousetweaks

import net.minecraft.screen.slot.Slot

/**
 * @author yuchenxue
 * @date 2025/07/17
 */

interface ScreenHandler {

    fun isIgnored(slot: Slot): Boolean

}