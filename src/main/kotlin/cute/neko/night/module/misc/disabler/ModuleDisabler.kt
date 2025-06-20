package cute.neko.night.module.misc.disabler

import cute.neko.night.module.ClientModule
import cute.neko.night.module.ModuleCategory
import cute.neko.night.module.misc.disabler.modes.DisablerWatchdog

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