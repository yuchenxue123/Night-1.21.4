package cute.neko.night.features.module.misc.disabler

import cute.neko.night.features.module.ClientModule
import cute.neko.night.features.module.ModuleCategory
import cute.neko.night.features.module.misc.disabler.modes.DisablerWatchdog

object ModuleDisabler : ClientModule(
    "Disabler",
    ModuleCategory.MISC
) {

    init {
        tree(DisablerWatchdog)
    }
}