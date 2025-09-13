package cute.neko.night.ui.screen.click.styles.drop.setting.mode

import cute.neko.night.features.setting.type.mode.MultiEnumSetting
import cute.neko.night.ui.screen.click.styles.drop.Constants.SELECT_BUTTON_HEIGHT

class MultiEnumSettingButton(
    setting: MultiEnumSetting<*>
) : AbstractModeSettingButton<MultiEnumSetting<*>>(setting) {
    override fun build() {
        buttons.clear()

        setting.selects.forEach { mode ->
            val button = ModeButton.create(mode)
                .receive {
                    setting.isActive(it)
                }
                .callback {
                    setting.toggle(it)
                }

            button.size(width(), SELECT_BUTTON_HEIGHT)

            buttons.add(button)
        }

        refresh()
    }
}