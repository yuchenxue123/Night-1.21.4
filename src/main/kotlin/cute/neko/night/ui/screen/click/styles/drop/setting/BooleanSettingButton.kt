package cute.neko.night.ui.screen.click.styles.drop.setting

import cute.neko.night.features.setting.type.primitive.BooleanSetting
import cute.neko.night.ui.screen.click.styles.drop.Constants.SETTING_LEFT_RIGHT_SPACE
import cute.neko.night.ui.screen.click.styles.drop.Constants.TOGGLE_BUTTON_WIDTH
import cute.neko.night.ui.screen.click.styles.drop.component.ToggleButton
import cute.neko.night.utils.render.nano.NanoUtils
import net.minecraft.client.gui.DrawContext
import java.awt.Color

class BooleanSettingButton(
    setting: BooleanSetting
) : AbstractSettingButton<BooleanSetting>(setting) {

    private val button = ToggleButton.create()
        .receive {
            setting.get()
        }
        .callback { new ->
            setting.set(new)
        }

    override fun render(context: DrawContext, mouseX: Int, mouseY: Int, delta: Float) {

        NanoUtils.drawRect(
            x(),
            y(),
            width(),
            height(),
            Color(40, 40, 40)
        )

        val text = setting.name
        font.drawText(
            text,
            x() + SETTING_LEFT_RIGHT_SPACE,
            y() + (height() - font.height(text)) / 2,
            Color(255, 255, 255)
        )

        button.let {
            it.size(TOGGLE_BUTTON_WIDTH, height() - 16f)
            it.position(x() + width() - SETTING_LEFT_RIGHT_SPACE - it.width(), y() + 8f)
            it.padding(4f, 4f)
            it.render(context, mouseX, mouseY, delta)
        }
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int) {
        this.button.mouseClicked(mouseX, mouseY, button)
    }

    override fun getHeight(): Float = height()

    override fun getAnimateHeight(): Float = height()
}