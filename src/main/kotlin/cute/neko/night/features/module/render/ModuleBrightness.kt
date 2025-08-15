package cute.neko.night.features.module.render

import cute.neko.night.event.handler
import cute.neko.night.event.events.game.player.PlayerTickEvent
import cute.neko.night.features.module.ClientModule
import cute.neko.night.features.module.ModuleCategory
import cute.neko.night.features.setting.config.types.choice.Choice
import cute.neko.night.features.setting.config.types.choice.ChoicesConfigurable
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.effect.StatusEffects

/**
 * @author yuchenxue
 * @date 2025/01/15 - 14:16
 */

object ModuleBrightness : ClientModule(
    "Brightness",
    ModuleCategory.RENDER
) {

    private val mode = choices("Mode", arrayOf(
        BrightnessGamma,
        BrightnessPotion
    ), BrightnessGamma
    )

    override val suffix: String
        get() = mode.getActive().modeName


    object BrightnessGamma : Choice("Gamma") {
        override val controller: ChoicesConfigurable<*>
            get() = mode

        private val brightness by float("Brightness", 15f, 1f..15f, 0.5f)

        var gamma = 0.0

        override fun enable() {
            gamma = mc.options.gamma.value
        }

        @Suppress("unused")
        private val onPlayerTick = handler<PlayerTickEvent> {
            if (gamma < brightness) {
                gamma = (gamma + 0.1).coerceAtMost(brightness.toDouble())
            }
        }
    }

    object BrightnessPotion : Choice("Potion") {
        override val controller: ChoicesConfigurable<*>
            get() = mode

        override fun disable() {
            player.removeStatusEffect(StatusEffects.NIGHT_VISION)
        }

        @Suppress("unused")
        private val onPlayerTick = handler<PlayerTickEvent> {
            player.addStatusEffect(StatusEffectInstance(StatusEffects.NIGHT_VISION, 20 * (13 * 60 + 38)))
        }
    }
}