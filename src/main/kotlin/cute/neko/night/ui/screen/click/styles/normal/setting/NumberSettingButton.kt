package cute.neko.night.ui.screen.click.styles.normal.setting

import cute.neko.night.features.setting.type.number.AbstractNumberSetting
import cute.neko.night.ui.screen.click.styles.normal.special.Slider
import cute.neko.night.utils.render.nano.NanoUtils
import net.minecraft.client.gui.DrawContext
import java.awt.Color
import kotlin.math.max

/**
 * @author yuchenxue
 * @date 2025/05/06
 */

class NumberSettingButton(
    setting: AbstractNumberSetting<*>
) : AbstractSettingButton<AbstractNumberSetting<*>>(setting) {

    private val slider = Slider.create()
        .size(width - padding * 2)
        .tag()
        .tagText {
            setting.get().toString()
        }
        .receive {
            setting.process
        }
        .callback { process ->
            setting.setProcess(process)
        }

    // render text
    private val text
        get()= "${setting.name}: ${setting.get()}"
    private val min = setting.min.toString()
    private val max = setting.max.toString()

    override fun render(context: DrawContext, mouseX: Int, mouseY: Int, delta: Float) {
        super.render(context, mouseX, mouseY, delta)

        NanoUtils.drawRoundRect(
            renderX,
            renderY + offset,
            width,
            height,
            padding,
            Color(40, 40, 40, 80)
        )

        var tempOffset = padding

        font.drawText(
            text,
            renderX + padding,
            renderY + offset + tempOffset,
            Color(200, 200, 200, 255)
        )

        tempOffset += font.height(text) + padding

        font.drawText(
            min,
            renderX + padding,
            renderY + offset + tempOffset,
            Color(200, 200, 200, 255)
        )

        font.drawText(
            max,
            renderX + width - padding - font.width(max),
            renderY + offset + tempOffset,
            Color(200, 200, 200, 255)
        )

        tempOffset += max(font.height(min), font.height(max)) + padding

        slider.position(
            renderX + padding,
            renderY + offset + tempOffset
        ).render(context, mouseX, mouseY, delta)
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int) {
        slider.mouseClicked(mouseX, mouseY, button)
    }

    override fun mouseReleased(mouseX: Double, mouseY: Double, button: Int) {
        slider.mouseReleased(mouseX, mouseY, button)
    }

    override val height: Float
        get() = padding + font.height(text) + padding + max(font.height(min), font.height(min)) + padding + slider.height + padding
}