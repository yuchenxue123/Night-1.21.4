package cute.neko.night.ui.screen.click.styles.normal.setting

import cute.neko.night.features.setting.type.mode.AbstractModeSetting
import cute.neko.night.ui.screen.click.styles.normal.setting.mode.ModeButton

class ModeSettingButton(
    setting: AbstractModeSetting<*>
) : AbstractSelectSetting<AbstractModeSetting<*>>(setting) {
    override fun build() {
        buttons.clear()

        setting.modes.forEach { mode ->
            val button = ModeButton(mode)
                .handler {
                    setting.getAsString() == it.modeName
                }
                .callback { new ->
                    setting.setByString(new)
                }

            buttons.add(button)
        }

        this.bottom = refresh(width - padding * 2)
    }

}