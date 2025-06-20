package cute.neko.night.ui.screen.click.styles.normal.setting

import cute.neko.night.setting.type.primitive.BooleanSetting
import cute.neko.night.ui.screen.special.CheckBox
import cute.neko.night.utils.nano.NanoUtils
import net.minecraft.client.gui.DrawContext
import java.awt.Color
import kotlin.math.max

/**
 * @author yuchenxue
 * @date 2025/05/05
 */

class BooleanSettingButton(
    setting: BooleanSetting
) : AbstractSettingButton<BooleanSetting>(setting) {

    private val checkbox = CheckBox.create()
        .size(50f, 24f, 4f)
        .receive {
            setting.get()
        }
        .callback { state ->
            setting.set(state)
        }

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

        val text = setting.name
        font.render(
            context,
            text,
            renderX + padding,
            renderY + offset + (height - font.height(text)) / 2,
            Color(200, 200, 200, 255),
        )

        checkbox.position(
            renderX + width - padding - checkbox.width,
            renderY + offset + (height - checkbox.height) / 2
        ).render(context, mouseX, mouseY, delta)
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int) {
        checkbox.mouseClicked(mouseX, mouseY, button)
    }

    override val height: Float
        get() = padding + max(checkbox.height, font.height(setting.name)) + padding
}