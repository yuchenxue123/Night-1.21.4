package cute.neko.night.features.module.render

import cute.neko.night.features.module.ClientModule
import cute.neko.night.features.module.ModuleCategory
import cute.neko.night.ui.widget.edit.EditScreen

/**
 * @author yuchenxue
 * @date 2025/08/10
 */

object ModuleEditScreen : ClientModule(
    "EditScreen",
    ModuleCategory.RENDER
) {

    override fun enable() {
        mc.setScreen(EditScreen())
        toggle()
    }
}