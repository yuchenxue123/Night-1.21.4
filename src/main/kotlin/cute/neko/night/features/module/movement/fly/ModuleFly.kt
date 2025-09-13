package cute.neko.night.features.module.movement.fly

import cute.neko.night.features.module.ClientModule
import cute.neko.night.features.module.ModuleCategory
import cute.neko.night.features.module.movement.fly.modes.FlyMotion

object ModuleFly : ClientModule(
    "Fly",
    ModuleCategory.MOVEMENT
) {
    val mode = choices(
        "Mode", arrayOf(
            FlyMotion
        )
    )

    override val suffix: String
        get() = mode.getActive().modeName
}