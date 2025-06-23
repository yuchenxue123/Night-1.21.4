package cute.neko.night.features.module.misc

import cute.neko.night.features.module.ClientModule
import cute.neko.night.features.module.ModuleCategory
import cute.neko.night.features.setting.type.mode.ModeSetting
import cute.neko.night.features.setting.type.mode.SubMode
import cute.neko.night.features.setting.type.number.FloatSetting
import cute.neko.night.features.setting.type.primitive.BooleanSetting
import org.lwjgl.glfw.GLFW

/**
 * @author yuchenxue
 * @date 2025/05/04
 */

object ModuleTest : ClientModule(
    "Test",
    ModuleCategory.MISC,
    key = GLFW.GLFW_KEY_K,
) {

    init {
        setting(ModeSetting("Mode", arrayOf("Grim", "Watchdog", "星见雅", "江风", "雪风", "AAAAAAAAAAAAAAAAAAAAAAAAAAAA", "7084"), "Watchdog"))
        setting(BooleanSetting("Boolean", false))
        setting(FloatSetting("Float", 1f, 1f..2f, 0.1f))
    }

    private val test = enum("TestMulti", Test.NULL)

    enum class Test(override val modeName: String) : SubMode {
        NULL("Null"),
        EMPTY("Empty"),
        UNKNOWN("Unknown")
    }
}