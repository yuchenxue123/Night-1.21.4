package cute.neko.night.module.render

import cute.neko.night.module.ClientModule
import cute.neko.night.module.ModuleCategory
import cute.neko.night.setting.type.mode.SubMode

/**
 * @author yuchenxue
 * @date 2025/05/11
 */

object ModuleRotations : ClientModule(
    "Rotations",
    ModuleCategory.RENDER
) {
    private val part = enum("BodyPart", BodyPart.HEAD).apply {
        activeAll()
    }

    enum class BodyPart(override val modeName: String) : SubMode {
        HEAD("Head"),
        BODY("Body")
    }

    fun isActive(bodyPart: BodyPart): Boolean {
        return this.running && part.isActive(bodyPart)
    }
}