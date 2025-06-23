package cute.neko.night.features.module.player.nofall

import cute.neko.night.features.module.ClientModule
import cute.neko.night.features.module.ModuleCategory
import cute.neko.night.features.module.player.nofall.modes.NoFallTimer

/**
 * @author yuchenxue
 * @date 2025/06/23
 */

object ModuleNoFall : ClientModule(
    "NoFall",
    ModuleCategory.PLAYER
) {
    val mode = choices("Mode", arrayOf(
        NoFallTimer
    ))

    override val suffix: String
        get() = mode.getActive().name
}