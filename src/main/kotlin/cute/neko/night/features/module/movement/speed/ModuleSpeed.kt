package cute.neko.night.features.module.movement.speed

import cute.neko.night.features.module.ClientModule
import cute.neko.night.features.module.ModuleCategory
import cute.neko.night.features.module.movement.speed.modes.SpeedMotion
import cute.neko.night.features.module.movement.speed.modes.SpeedWatchdog

/**
 * @author yuchenxue
 * @date 2025/06/03
 */

object ModuleSpeed : ClientModule(
    "Speed",
    ModuleCategory.MOVEMENT
) {
    val mode = choices("Mode", arrayOf(
        SpeedMotion,
        SpeedWatchdog
    ))
}