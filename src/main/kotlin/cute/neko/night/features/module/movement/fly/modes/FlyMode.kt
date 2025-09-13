package cute.neko.night.features.module.movement.fly.modes

import cute.neko.night.features.module.movement.fly.ModuleFly
import cute.neko.night.features.setting.config.types.choice.Choice
import cute.neko.night.features.setting.config.types.choice.ChoicesConfigurable

open class FlyMode(name: String) : Choice(name) {
    override val controller: ChoicesConfigurable<*>
        get() = ModuleFly.mode
}