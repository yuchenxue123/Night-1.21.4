package cute.neko.night.features.module.movement.speed

import cute.neko.night.features.module.ClientModule
import cute.neko.night.features.module.ModuleCategory
import cute.neko.night.features.module.movement.speed.modes.SpeedGrimCollision
import cute.neko.night.features.module.movement.speed.modes.SpeedMotion

object ModuleSpeed : ClientModule(
    "Speed",
    ModuleCategory.MOVEMENT
) {
    val mode = choices(
        "Mode", arrayOf(
            SpeedMotion,
            SpeedGrimCollision
        )
    )

    override val suffix: String
        get() = mode.getActive().modeName
}