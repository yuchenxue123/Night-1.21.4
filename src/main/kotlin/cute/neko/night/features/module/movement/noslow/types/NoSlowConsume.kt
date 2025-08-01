package cute.neko.night.features.module.movement.noslow.types

import cute.neko.night.features.module.movement.noslow.ModuleNoSlow
import cute.neko.night.features.module.movement.noslow.types.consume.ConsumeGrimV1
import cute.neko.night.features.module.movement.noslow.types.consume.ConsumeMode
import cute.neko.night.features.setting.config.types.choice.ChoicesConfigurable

/**
 * @author yuchenxue
 * @date 2025/07/08
 */

object NoSlowConsume : ChoicesConfigurable<ConsumeMode>(
    name = "NoSlowConsume",
    choices = arrayOf(
        ConsumeGrimV1
    ),
    parent = ModuleNoSlow
)