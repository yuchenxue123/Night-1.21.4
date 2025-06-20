package cute.neko.night.ui.screen.click.styles

import cute.neko.night.ui.interfaces.Screen
import net.minecraft.client.util.Window

/**
 * @author yuchenxue
 * @date 2025/05/05
 */

interface Style : Screen {
    val window: Window
        get() = mc.window

    fun open() {}

    fun hasClosed(): Boolean
}