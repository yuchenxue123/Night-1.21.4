package cute.neko.night.features.module.combat.antivelocity.modes

import cute.neko.night.features.module.combat.antivelocity.ModuleAntiVelocity
import cute.neko.night.features.setting.config.types.choice.Choice
import cute.neko.night.features.setting.config.types.choice.ChoicesConfigurable

/**
 * @author yuchenxue
 * @date 2025/05/10
 */

open class AntiVelocityMode(name: String) : Choice(name) {
    override val controller: ChoicesConfigurable<*>
        get() = ModuleAntiVelocity.mode
}