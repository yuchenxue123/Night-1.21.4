package cute.neko.night.features.module.render

import cute.neko.night.features.module.ClientModule
import cute.neko.night.features.module.ModuleCategory
import cute.neko.night.utils.lang.LanguageManager

object ModuleSettings : ClientModule(
    "Settings",
    ModuleCategory.RENDER,
    locked = true,
) {
    val language by mode("Language", LanguageManager.languages)
}