package cute.neko.night.ui.interfaces

import cute.neko.night.utils.interfaces.Accessor
import net.minecraft.client.gui.DrawContext

/**
 * @author yuchenxue
 * @date 2025/05/05
 */

interface Screen : Accessor {
    fun render(context: DrawContext, mouseX: Int, mouseY: Int, delta: Float) {}

    fun mouseClicked(mouseX: Double, mouseY: Double, button: Int) {}
    fun mouseReleased(mouseX: Double, mouseY: Double, button: Int) {}

    fun mouseScrolled(
        mouseX: Double,
        mouseY: Double,
        horizontal: Double,
        vertical: Double
    ) {}


    fun keyPressed(keyCode: Int, scanCode: Int, modifiers: Int) {}
    fun keyReleased(keyCode: Int, scanCode: Int, modifiers: Int) {}

    fun close() {}
}