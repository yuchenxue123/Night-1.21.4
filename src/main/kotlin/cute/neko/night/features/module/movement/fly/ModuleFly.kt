package cute.neko.night.features.module.movement.fly

import cute.neko.night.features.module.ClientModule
import cute.neko.night.features.module.ModuleCategory

/**
 * @author yuchenxue
 * @date 2025/07/07
 */

object ModuleFly : ClientModule(
    "Fly",
    ModuleCategory.MOVEMENT
) {
    val mode = choices("Mode", arrayOf())
}