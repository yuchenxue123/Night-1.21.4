package cute.neko.night.features.module.misc.disabler

import cute.neko.night.features.module.ClientModule
import cute.neko.night.features.module.ModuleCategory
import cute.neko.night.features.module.misc.disabler.modes.DisablerWatchdog

/**
 * @author yuchenxue
 * @date 2025/06/04
 */

object ModuleDisabler : ClientModule(
    "Disabler",
    ModuleCategory.MISC
) {

    init {
        tree(DisablerWatchdog)
    }
}