package cute.neko.night.features.module.movement.noslow

import cute.neko.night.features.module.ClientModule
import cute.neko.night.features.module.ModuleCategory
import cute.neko.night.features.module.movement.noslow.modes.NoSlowNone

object ModuleNoSlow : ClientModule(
    "NoSlow",
    ModuleCategory.MOVEMENT
) {

    val mode = choices("Mode", arrayOf(NoSlowNone))

    override val suffix: String
        get() = mode.getActive().modeName
}