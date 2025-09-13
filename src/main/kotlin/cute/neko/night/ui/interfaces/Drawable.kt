package cute.neko.night.ui.interfaces

import cute.neko.night.utils.interfaces.Accessor
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.util.Window

interface Drawable : Accessor {
    val window: Window
        get() = mc.window

    fun render(context: DrawContext)
}