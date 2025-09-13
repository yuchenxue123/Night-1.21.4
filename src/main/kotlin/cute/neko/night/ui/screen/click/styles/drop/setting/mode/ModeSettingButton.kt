package cute.neko.night.ui.screen.click.styles.drop.setting.mode

import cute.neko.night.features.setting.type.mode.AbstractModeSetting
import cute.neko.night.ui.screen.click.styles.drop.Constants.SELECT_BUTTON_HEIGHT

class ModeSettingButton(
    setting: AbstractModeSetting<*>
) : AbstractModeSettingButton<AbstractModeSetting<*>>(setting) {
    override fun build() {
        buttons.clear()

        setting.modes.forEach { mode ->
            val button = ModeButton.create(mode)
                .receive {
                    setting.getAsString() == mode
                }
                .callback {
                    setting.setByString(it)
                }

            button.size(width(), SELECT_BUTTON_HEIGHT)

            buttons.add(button)
        }

        refresh()
    }
}