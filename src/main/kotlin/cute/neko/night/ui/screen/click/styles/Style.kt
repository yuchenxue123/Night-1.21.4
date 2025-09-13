package cute.neko.night.ui.screen.click.styles

import cute.neko.night.ui.interfaces.Screen
import cute.neko.night.utils.misc.option.BooleanOption
import net.minecraft.client.util.Window

abstract class Style : Screen {
    protected val window: Window
        get() = mc.window

    protected val closed = BooleanOption(false)

    open fun open() {
        closed.set(false)
    }

    open fun close() {
        closed.set(true)
    }

    open fun shouldClose(): Boolean = closed.get()
}