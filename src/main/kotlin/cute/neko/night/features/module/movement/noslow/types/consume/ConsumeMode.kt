package cute.neko.night.features.module.movement.noslow.types.consume

import cute.neko.night.features.module.movement.noslow.types.NoSlowConsume
import cute.neko.night.features.setting.config.types.choice.Choice
import cute.neko.night.features.setting.config.types.choice.ChoicesConfigurable
import net.minecraft.item.consume.UseAction

/**
 * @author yuchenxue
 * @date 2025/07/25
 */

open class ConsumeMode(name: String) : Choice(name) {
    override val controller: ChoicesConfigurable<*>
        get() = NoSlowConsume

    override val running: Boolean
        get() = super.running && player.activeItem.useAction in arrayOf(UseAction.EAT, UseAction.DRINK)
}