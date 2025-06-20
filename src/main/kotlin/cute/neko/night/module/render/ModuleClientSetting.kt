package cute.neko.night.module.render

import cute.neko.night.module.ClientModule
import cute.neko.night.module.ModuleCategory
import cute.neko.night.utils.lang.LanguageManager

/**
 * @author yuchenxue
 * @date 2025/05/08
 */

object ModuleClientSetting : ClientModule(
    "ClientSetting",
    ModuleCategory.RENDER,
    locked = true,
) {
    val language by mode("Language", LanguageManager.languages)
}