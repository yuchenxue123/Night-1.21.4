package cute.neko.night.module.combat.antivelocity.modes

import cute.neko.night.module.combat.antivelocity.ModuleAntiVelocity
import cute.neko.night.setting.config.types.choice.Choice
import cute.neko.night.setting.config.types.choice.ChoicesConfigurable

/**
 * @author yuchenxue
 * @date 2025/05/10
 */

open class AntiVelocityMode(name: String) : Choice(name) {
    override val controller: ChoicesConfigurable<*>
        get() = ModuleAntiVelocity.mode
}