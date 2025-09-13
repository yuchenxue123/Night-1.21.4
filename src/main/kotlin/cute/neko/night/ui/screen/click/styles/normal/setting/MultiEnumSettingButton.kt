package cute.neko.night.ui.screen.click.styles.normal.setting

import cute.neko.night.features.setting.type.mode.MultiEnumSetting
import cute.neko.night.ui.screen.click.styles.normal.setting.mode.ModeButton

class MultiEnumSettingButton(
    setting: MultiEnumSetting<*>
) : AbstractSelectSetting<MultiEnumSetting<*>>(setting) {

    override fun build() {
        buttons.clear()

        setting.selects.forEach { mode ->
            val button = ModeButton(mode)
                .handler {
                    setting.isActive(it.modeName)
                }
                .callback { new ->
                    setting.toggle(new)
                }

            buttons.add(button)
        }

        this.bottom = refresh(width - padding * 2)
    }
}