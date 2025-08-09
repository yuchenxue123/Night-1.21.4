package cute.neko.night.ui.screen.click.styles.drop.setting

import cute.neko.night.features.setting.AbstractSetting
import cute.neko.night.ui.interfaces.Screen
import cute.neko.night.ui.screen.click.styles.drop.Constants.COMPONENT_WIDTH
import cute.neko.night.ui.screen.click.styles.drop.Constants.SETTING_BUTTON_HEIGHT
import cute.neko.night.ui.screen.click.styles.drop.component.RectComponent
import cute.neko.night.utils.render.nano.NanoFontManager

/**
 * @author yuchenxue
 * @date 2025/08/08
 */

abstract class AbstractSettingButton<T : AbstractSetting<*>>(
    protected val setting: T,
) : RectComponent(0f, 0f, COMPONENT_WIDTH, SETTING_BUTTON_HEIGHT), Screen {
    protected val font = NanoFontManager.GENSHIN_15

    fun visible(): Boolean = setting.visible

    abstract fun getHeight(): Float

    abstract fun getAnimateHeight(): Float
}