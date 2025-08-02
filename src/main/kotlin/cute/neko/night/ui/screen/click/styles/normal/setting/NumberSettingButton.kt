package cute.neko.night.ui.screen.click.styles.normal.setting

import cute.neko.night.features.setting.type.number.AbstractNumberSetting
import cute.neko.night.features.setting.type.number.FloatSetting
import cute.neko.night.features.setting.type.number.IntegerSetting
import cute.neko.night.ui.screen.special.Slider
import cute.neko.night.utils.extensions.decimals
import cute.neko.night.utils.extensions.step
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
            return@receive when (setting) {
                is FloatSetting -> {
                    ((setting.get() - setting.min) / (setting.max - setting.min)).coerceIn(0f, 1f)
                }

                is IntegerSetting -> {
                    ((setting.get() - setting.min).toFloat() / (setting.max - setting.min)).coerceIn(0f, 1f)
                }

                else -> 0f
            }
        }
        .callback { process ->
            when (setting) {
                is FloatSetting -> {
                    setting.set(
                        (setting.min + ((setting.max - setting.min) * process).step(setting.step)).decimals(2)
                    )
                }

                is IntegerSetting -> {
                    setting.set(
                        setting.min + ((setting.max - setting.min) * process).step(setting.step.toFloat()).toInt()
                    )
                }
            }
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