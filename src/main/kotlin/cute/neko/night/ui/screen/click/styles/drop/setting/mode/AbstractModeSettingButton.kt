package cute.neko.night.ui.screen.click.styles.drop.setting.mode

import cute.neko.night.features.setting.AbstractSetting
import cute.neko.night.ui.isHovered
import cute.neko.night.ui.screen.click.styles.drop.Constants.SETTING_LEFT_RIGHT_SPACE
import cute.neko.night.ui.screen.click.styles.drop.setting.AbstractSettingButton
import cute.neko.night.utils.animation.AnimationType
import cute.neko.night.utils.animation.SimpleAnimation
import cute.neko.night.utils.extensions.sum
import cute.neko.night.utils.misc.option.BooleanOption
import cute.neko.night.utils.misc.option.FloatOption
import cute.neko.night.utils.render.nano.NanoUtils
import net.minecraft.client.gui.DrawContext
import java.awt.Color

/**
 * @author yuchenxue
 * @date 2025/08/09
 */

abstract class AbstractModeSettingButton<T : AbstractSetting<*>>(
    setting: T
) : AbstractSettingButton<T>(setting) {

    protected val buttons = mutableListOf<ModeButton>()

    init {
        build()
    }

    private val open = BooleanOption(false)

    private val height = FloatOption(0f)
    private val height_animation = SimpleAnimation.create()
        .type(AnimationType.QUAD_OUT)
        .finish()

    override fun render(context: DrawContext, mouseX: Int, mouseY: Int, delta: Float) {
        refresh()

        height.animate(height_animation)

        val text = setting.name

        NanoUtils.drawRect(
            x(),
            y() + height(),
            width(),
            height.get(),
            Color(35, 35, 35)
        )

        // bbb
        NanoUtils.drawRect(
            x(),
            y(),
            width(),
            height(),
            Color(40, 40, 40)
        )

        font.drawText(
            text,
            x() + SETTING_LEFT_RIGHT_SPACE,
            y() + (height() - font.height(text)) / 2f,
            Color(255, 255, 255)
        )

        NanoUtils.scissor(x(), y() + height(), width(), height.get()) {
            buttons.forEach {
                it.render(context, mouseX, mouseY, delta)
            }
        }
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int) {
        val hovered = isHovered(rect, mouseX, mouseY)

        if (hovered && button == 1) {
            open.toggle { new ->
                when (new) {
                    true -> {
                        height_animation
                            .start(height.get())
                            .target(getHeight() - height())
                            .reset()
                    }

                    false -> {
                        height_animation
                            .start(height.get())
                            .target(height.default)
                            .reset()
                    }
                }
            }
        }

        if (open.get()) {
            buttons.forEach {
                it.mouseClicked(mouseX, mouseY, button)
            }
        }
    }

    protected fun refresh() {
        var offset = y() + height()

        buttons.forEach {
            it.position(x(), offset)
            offset += it.height()
        }
    }

    abstract fun build()

    override fun getHeight(): Float = height() + if (open.get()) buttons.sum { it.height() } else { 0f }

    override fun getAnimateHeight(): Float = height() + height.get()
}