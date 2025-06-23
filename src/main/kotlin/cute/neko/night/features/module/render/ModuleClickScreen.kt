package cute.neko.night.features.module.render

import cute.neko.night.features.module.ClientModule
import cute.neko.night.features.module.ModuleCategory
import cute.neko.night.ui.screen.click.ClickScreen
import org.lwjgl.glfw.GLFW

/**
 * @author yuchenxue
 * @date 2025/05/05
 */

object ModuleClickScreen : ClientModule(
    "ClickScreen",
    ModuleCategory.RENDER,
    key = GLFW.GLFW_KEY_RIGHT_SHIFT,
    locked = true,
) {

    override fun enable() {
        mc.setScreen(ClickScreen())
        toggle()
    }
}