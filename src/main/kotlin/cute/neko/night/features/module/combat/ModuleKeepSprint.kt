package cute.neko.night.features.module.combat

import cute.neko.night.features.module.ClientModule
import cute.neko.night.features.module.ModuleCategory

/**
 * @author yuchenxue
 * @date 2025/07/06
 */

object ModuleKeepSprint : ClientModule(
    "KeepSprint",
    ModuleCategory.COMBAT
) {
    val motion by float("Motion", 1f, 0.1f..1f, 0.05f)
}