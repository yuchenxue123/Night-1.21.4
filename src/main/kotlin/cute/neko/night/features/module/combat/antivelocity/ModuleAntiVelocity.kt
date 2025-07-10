package cute.neko.night.features.module.combat.antivelocity

import cute.neko.night.features.module.ClientModule
import cute.neko.night.features.module.ModuleCategory
import cute.neko.night.features.module.combat.antivelocity.modes.AntiVelocityCancel
import cute.neko.night.features.module.combat.antivelocity.modes.AntiVelocityDelay
import cute.neko.night.features.module.combat.antivelocity.modes.AntiVelocitySimple
import cute.neko.night.features.module.combat.antivelocity.modes.AntiVelocityWatchdog

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
        AntiVelocitySimple,
        AntiVelocityDelay
    ), AntiVelocityCancel
    )

    override val suffix: String
        get() = mode.getActive().modeName
}