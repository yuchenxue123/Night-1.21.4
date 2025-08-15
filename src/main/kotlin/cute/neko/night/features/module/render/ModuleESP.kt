package cute.neko.night.features.module.render

import cute.neko.night.event.handler
import cute.neko.night.event.events.game.render.ScreenRenderEvent
import cute.neko.night.features.module.ClientModule
import cute.neko.night.features.module.ModuleCategory
import cute.neko.night.features.module.render.share.SharedTargetOption
import cute.neko.night.utils.entity.interpolatedBox
import cute.neko.night.utils.render.nano.NanoUtils
import cute.neko.night.utils.render.utils.WorldToScreen
import net.minecraft.entity.LivingEntity
import java.awt.Color


/**
 * @author yuchenxue
 * @date 2025/07/07
 */

object ModuleESP : ClientModule(
    "ESP",
    ModuleCategory.RENDER
) {

    private val targets = tree(SharedTargetOption)

    @Suppress("unused")
    private val onScreenRender = handler<ScreenRenderEvent> { event ->
        world.entities.filterIsInstance<LivingEntity>()
            .filter { targets.isTarget(it) }
            .forEach { entity ->
                WorldToScreen.calculate(entity.interpolatedBox(event.tickDelta)) { x, y, width, height ->
                    NanoUtils.drawRect(x, y, width, height, Color(0, 0, 0, 100))
                }
            }
    }
}