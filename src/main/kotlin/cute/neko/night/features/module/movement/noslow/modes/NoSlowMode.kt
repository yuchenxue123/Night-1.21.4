package cute.neko.night.features.module.movement.noslow.modes

import cute.neko.night.features.module.movement.noslow.ModuleNoSlow
import cute.neko.night.features.setting.config.types.choice.Choice
import cute.neko.night.features.setting.config.types.choice.ChoicesConfigurable

open class NoSlowMode(name: String) : Choice(name) {
    override val controller: ChoicesConfigurable<*>
        get() = ModuleNoSlow.mode
}