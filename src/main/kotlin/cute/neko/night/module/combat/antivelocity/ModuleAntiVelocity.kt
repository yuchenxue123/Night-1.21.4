package cute.neko.night.module.combat.antivelocity

import cute.neko.night.module.ClientModule
import cute.neko.night.module.ModuleCategory
import cute.neko.night.module.combat.antivelocity.modes.AntiVelocityCancel
import cute.neko.night.module.combat.antivelocity.modes.AntiVelocityWatchdog

/**
 * @author yuchenxue
 * @date 2025/01/15 - 13:56
 */

object ModuleAntiVelocity : ClientModule(
    "AntiVelocity",
    ModuleCategory.COMBAT
) {
    val mode = choices("Mode", arrayOf(
        AntiVelocityCancel,
        AntiVelocityWatchdog,
    ), AntiVelocityCancel
    )

    override val suffix: String
        get() = mode.getActive().modeName
}