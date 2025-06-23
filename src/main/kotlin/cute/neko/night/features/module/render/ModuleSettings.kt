package cute.neko.night.features.module.render

import cute.neko.night.features.module.ClientModule
import cute.neko.night.features.module.ModuleCategory
import cute.neko.night.utils.lang.LanguageManager

/**
 * @author yuchenxue
 * @date 2025/05/08
 */

object ModuleSettings : ClientModule(
    "Settings",
    ModuleCategory.RENDER,
    locked = true,
) {
    val language by mode("Language", LanguageManager.languages)
}