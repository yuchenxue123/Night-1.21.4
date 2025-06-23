package cute.neko.night.features.module.movement.speed.modes

import cute.neko.night.features.module.movement.speed.ModuleSpeed
import cute.neko.night.features.setting.config.types.choice.Choice
import cute.neko.night.features.setting.config.types.choice.ChoicesConfigurable

/**
 * @author yuchenxue
 * @date 2025/06/03
 */

open class SpeedMode(name: String) : Choice(name) {
    override val controller: ChoicesConfigurable<*>
        get() = ModuleSpeed.mode
}