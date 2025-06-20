package cute.neko.night.module.movement.speed

import cute.neko.night.module.ClientModule
import cute.neko.night.module.ModuleCategory
import cute.neko.night.module.movement.speed.modes.SpeedWatchdog

/**
 * @author yuchenxue
 * @date 2025/06/03
 */

object ModuleSpeed : ClientModule(
    "Speed",
    ModuleCategory.MOVEMENT
) {
    val mode = choices("Mode", arrayOf(
        SpeedWatchdog
    ))
}