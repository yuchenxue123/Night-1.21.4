package cute.neko.night.ui.screen.click.styles.drop.setting

import cute.neko.night.features.setting.type.number.AbstractNumberSetting
import cute.neko.night.ui.screen.click.styles.drop.Constants.SETTING_LEFT_RIGHT_SPACE
import cute.neko.night.ui.screen.click.styles.drop.component.Slider
import cute.neko.night.utils.render.nano.NanoUtils
import net.minecraft.client.gui.DrawContext
import java.awt.Color

/**
 * @author yuchenxue
 * @date 2025/08/09
 */

class NumberSettingButton(
    setting: AbstractNumberSetting<*>
) : AbstractSettingButton<AbstractNumberSetting<*>>(setting) {

    private val slider = Slider.create()
        .padding(down = 8f)
        .receive {
            setting.process
        }
        .callback { process ->
            setting.setProcess(process)
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

        val value = "${setting.get()}"

        font.drawText(
            value,
            x() + width() - SETTING_LEFT_RIGHT_SPACE - font.width(value),
            y() + (height() - font.height(text)) / 2,
            Color(255, 255, 255)
        )

        slider.let {
            it.size(width() - SETTING_LEFT_RIGHT_SPACE * 2f, 8f)
            it.position(x() + SETTING_LEFT_RIGHT_SPACE, y() + height())
            it.render(context, mouseX, mouseY, delta)
        }
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int) {
        slider.mouseClicked(mouseX, mouseY, button)
    }

    override fun mouseReleased(mouseX: Double, mouseY: Double, button: Int) {
        slider.mouseReleased(mouseX, mouseY, button)
    }

    override fun getHeight(): Float = height() + slider.getHeight()

    override fun getAnimateHeight(): Float = getHeight()
}