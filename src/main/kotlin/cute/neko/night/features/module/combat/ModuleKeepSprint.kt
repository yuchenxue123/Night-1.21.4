package cute.neko.night.features.module.combat

import cute.neko.night.features.module.ClientModule
import cute.neko.night.features.module.ModuleCategory

object ModuleKeepSprint : ClientModule(
    "KeepSprint",
    ModuleCategory.COMBAT
) {
    val motion by float("Motion", 1f, 0.1f..1f, 0.05f)
}