package cute.neko.night.features.module.render

import cute.neko.event.handler
import cute.neko.night.event.events.game.render.ScreenRenderEvent
import cute.neko.night.features.module.ClientModule
import cute.neko.night.features.module.ModuleCategory
import cute.neko.night.features.module.render.share.SharedTargetOption
import cute.neko.night.features.setting.config.types.choice.Choice
import cute.neko.night.features.setting.config.types.choice.ChoicesConfigurable
import cute.neko.night.utils.entity.interpolatedBox
import cute.neko.night.utils.render.nano.NanoUtils.nvg
import cute.neko.night.utils.render.nano.NanoUtils
import cute.neko.night.utils.render.nano.NanoFontManager
import cute.neko.night.utils.render.utils.WorldToScreen
import net.minecraft.entity.LivingEntity
import org.lwjgl.nanovg.NanoVG
import java.awt.Color

/**
 * @author yuchenxue
 * @date 2025/06/24
 */

object ModuleNameTags : ClientModule(
    "NameTags",
    ModuleCategory.RENDER
) {
    val mode = choices("Mode", arrayOf(NameTagsNormal))

    private val targets = tree(SharedTargetOption)

    private val font = NanoFontManager.GENSHIN

    object NameTagsNormal : Choice("Normal") {
        override val controller: ChoicesConfigurable<*>
            get() = mode

        @Suppress("unused")
        private val onScreenRender = handler<ScreenRenderEvent> { event ->
            world.entities.filterIsInstance<LivingEntity>()
                .filter { targets.isTarget(it) }
                .forEach { entity ->
                    WorldToScreen.calculate(entity.interpolatedBox(event.tickDelta)) { x, y, width, height ->
                        val padding = 8f
                        val text = entity.displayName!!
                        val w = font.width(text) + padding * 2
                        val h = font.height(text) + padding * 2

                        val startX = x - (w - width) / 2f
                        val startY = y - h - 4f

                        val distance = player.distanceTo(entity)
                        val scale = (40f / distance).coerceIn(0.5f, 1f)

                        NanoVG.nvgSave(nvg)
                        NanoVG.nvgTranslate(nvg, startX + w / 2f, startY + h / 2f)
                        NanoVG.nvgScale(nvg, scale, scale)
                        NanoVG.nvgTranslate(nvg, - w / 2f, - h / 2f)

                        NanoUtils.drawRect(0f, 0f, w, h, Color(40, 40, 40, 120))

                        font.drawText(text, padding, padding, Color.WHITE)

                        NanoVG.nvgRestore(nvg)
                    }
                }
        }
    }
}