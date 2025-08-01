package cute.neko.night.features.module.movement.noslow

import cute.neko.night.features.module.ClientModule
import cute.neko.night.features.module.ModuleCategory
import cute.neko.night.features.module.movement.noslow.types.NoSlowConsume

/**
 * @author yuchenxue
 * @date 2025/07/08
 */

object ModuleNoSlow : ClientModule(
    "NoSlow",
    ModuleCategory.MOVEMENT
) {

    init {
        tree(NoSlowConsume)
    }
}