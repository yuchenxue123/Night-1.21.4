package cute.neko.night.features.module

import cute.neko.night.utils.lang.LanguageManager
import cute.neko.night.utils.lang.translate

/**
 * @author yuchenxue
 * @date 2025/01/12 - 19:10
 */

enum class ModuleCategory(private val renderName: String) {
    COMBAT("Combat"),
    MOVEMENT("Movement"),
    PLAYER("Player"),
    RENDER("Render"),
    MISC("Misc")
    ;

    val showName: String
        get() {
            val key = "neko.module.category.${renderName.lowercase()}.name"
            return when {
                LanguageManager.hasTranslate(key) -> translate(key)
                else -> renderName
            }
        }

    val modules: List<ClientModule>
        get() = ModuleManager.modules.filter { it.category == this }.sortedBy { it.name }
}