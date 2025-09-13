package cute.neko.night.features.module.render

import cute.neko.night.features.module.ClientModule
import cute.neko.night.features.module.ModuleCategory
import cute.neko.night.features.setting.type.mode.SubMode
import cute.neko.night.ui.screen.click.ClickScreen
import cute.neko.night.ui.screen.click.styles.drop.StyleDrop
import org.lwjgl.glfw.GLFW

object ModuleClickScreen : ClientModule(
    "ClickScreen",
    ModuleCategory.RENDER,
    key = GLFW.GLFW_KEY_RIGHT_SHIFT,
    locked = true,
) {

    private val style by mode("Style", ClickScreenType.DEFAULT)

    override fun enable() {
        display()
        toggle()
    }

    private fun display() {
        when (style) {
            ClickScreenType.DEFAULT -> mc.setScreen(ClickScreen())
            ClickScreenType.DROPDOWN -> mc.setScreen(ClickScreen(StyleDrop))
        }
    }

    enum class ClickScreenType(override val modeName: String) : SubMode {
        DEFAULT("Default"),
        DROPDOWN("Dropdown"),
    }
}