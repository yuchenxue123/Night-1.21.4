package cute.neko.night.features.module.combat.killaura.features

import cute.neko.night.features.module.combat.killaura.ModuleKillAura
import cute.neko.night.features.setting.config.types.ToggleConfigurable

object KillAuraAutoBlock : ToggleConfigurable("AutoBlock", false, ModuleKillAura) {

    var blocking = false

    fun block() {

        blocking = true
    }

    fun unblock() {

        blocking = false
    }
}