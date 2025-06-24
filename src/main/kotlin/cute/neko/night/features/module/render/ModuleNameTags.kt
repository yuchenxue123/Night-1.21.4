package cute.neko.night.features.module.render

import cute.neko.night.features.module.ClientModule
import cute.neko.night.features.module.ModuleCategory
import cute.neko.night.features.setting.config.types.choice.Choice
import cute.neko.night.features.setting.config.types.choice.ChoicesConfigurable

/**
 * @author yuchenxue
 * @date 2025/06/24
 */

object ModuleNameTags : ClientModule(
    "NameTags",
    ModuleCategory.RENDER
) {
    val mode = choices("Mode", arrayOf(NameTagsNormal))

//    val targets = tree(TargetOption())

    object NameTagsNormal : Choice("Normal") {
        override val controller: ChoicesConfigurable<*>
            get() = mode
    }
}