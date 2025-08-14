package cute.neko.night.features.module.player.nofall

import cute.neko.night.features.module.ClientModule
import cute.neko.night.features.module.ModuleCategory
import cute.neko.night.features.module.player.nofall.modes.NoFallGrim
import cute.neko.night.features.module.player.nofall.modes.NoFallHypixel
import cute.neko.night.features.module.player.nofall.modes.NoFallPacket

/**
 * @author yuchenxue
 * @date 2025/06/23
 */

object ModuleNoFall : ClientModule(
    "NoFall",
    ModuleCategory.PLAYER
) {
    val mode = choices("Mode", arrayOf(
        NoFallPacket,
        NoFallHypixel,
        NoFallGrim
    ))

    override val suffix: String
        get() = mode.getActive().name
}