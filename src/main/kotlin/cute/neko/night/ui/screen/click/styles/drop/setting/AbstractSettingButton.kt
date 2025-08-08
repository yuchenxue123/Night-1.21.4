package cute.neko.night.ui.screen.click.styles.drop.setting

import cute.neko.night.features.setting.AbstractSetting

/**
 * @author yuchenxue
 * @date 2025/08/08
 */

abstract class AbstractSettingButton<T : AbstractSetting<*>>(
    protected val setting: T,
) {
    fun visible(): Boolean = setting.visible

    abstract fun height(): Float
}