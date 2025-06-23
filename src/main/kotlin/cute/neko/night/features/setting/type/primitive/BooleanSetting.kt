package cute.neko.night.features.setting.type.primitive

import cute.neko.night.features.setting.MutableSetting

/**
 * @author yuchenxue
 * @date 2025/05/04
 */

class BooleanSetting(
    name: String,
    value: Boolean,
    visibility: () -> Boolean = { true }
) : MutableSetting<Boolean>(name, value, visibility)