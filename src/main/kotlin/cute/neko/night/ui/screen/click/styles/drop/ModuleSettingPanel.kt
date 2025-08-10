package cute.neko.night.ui.screen.click.styles.drop

import cute.neko.night.features.setting.type.mode.AbstractModeSetting
import cute.neko.night.features.setting.type.mode.MultiEnumSetting
import cute.neko.night.features.setting.type.number.AbstractNumberSetting
import cute.neko.night.features.setting.type.primitive.BooleanSetting
import cute.neko.night.ui.interfaces.Screen
import cute.neko.night.ui.screen.click.styles.drop.component.RectComponent
import cute.neko.night.ui.screen.click.styles.drop.setting.AbstractSettingButton
import cute.neko.night.ui.screen.click.styles.drop.setting.BooleanSettingButton
import cute.neko.night.ui.screen.click.styles.drop.setting.NumberSettingButton
import cute.neko.night.ui.screen.click.styles.drop.setting.mode.ModeSettingButton
import cute.neko.night.ui.screen.click.styles.drop.setting.mode.MultiEnumSettingButton
import cute.neko.night.utils.animation.AnimationType
import cute.neko.night.utils.animation.SimpleAnimation
import cute.neko.night.utils.extensions.sum
import cute.neko.night.utils.misc.option.FloatOption
import cute.neko.night.utils.render.nano.NanoUtils
import net.minecraft.client.gui.DrawContext
import java.awt.Color

/**
 * @author yuchenxue
 * @date 2025/08/08
 */

class ModuleSettingPanel(
    private val button: DropModuleButton
) : RectComponent(
    button.x(),
    button.y() + button.height(),
    button.width(),
    button.height()
), Screen {

    private val buttons = mutableListOf<AbstractSettingButton<*>>()

    fun isNotEmpty() = buttons.isNotEmpty()

    val height = FloatOption(0f)
    private val height_animation = SimpleAnimation.create()
        .type(AnimationType.QUAD_OUT)
        .target(height.default)
        .finish()

    init {
        build()
    }

    override fun render(context: DrawContext, mouseX: Int, mouseY: Int, delta: Float) {
        refresh()

        update()

        height.animate(height_animation)

        if (height.get() > 0) {
            NanoUtils.drawRect(
                x(), y(),
                width(), height.get(),
                Color(40, 40, 40)
            )

            NanoUtils.scissor(x(), y(), width(), height.get()) {
                buttons.forEach {
                    if (!it.visible()) return@forEach

                    it.render(context, mouseX, mouseY, delta)
                }
            }
        }
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int) {
        if (this.button.open.get()) {
            buttons.forEach {
                if (!it.visible()) return@forEach

                it.mouseClicked(mouseX, mouseY, button)
            }
        }
    }

    override fun mouseReleased(mouseX: Double, mouseY: Double, button: Int) {
        if (this.button.open.get()) {
            buttons.forEach {
                if (!it.visible()) return@forEach

                it.mouseReleased(mouseX, mouseY, button)
            }
        }
    }

    fun getHeight(): Float {
        return if (button.open.get()) {
            buttons.sum {
                if (!it.visible()) return@sum 0f

                return@sum it.getHeight()
            }
        } else {
            0f
        }
    }

    fun getAnimationHeight(): Float {
        return height.get()
    }

    private fun update() {
        if (getHeight() != height_animation.target) {
            height_animation
                .start(this.height.get())
                .target(getHeight())
                .reset()
        }
    }

    fun update(state: Boolean) {
        when (state) {
            true -> {
                height_animation
                    .start(height.get())
                    .target(getHeight())
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

    private fun refresh() {
        position(button.x(), button.y() + button.height())

        var offset = button.y() + button.height()

        buttons.forEach {
            if (!it.visible()) return@forEach

            it.position(x = x(), y = offset)
            offset += it.getAnimateHeight()
        }
    }

    private fun build() {
        buttons.clear()

        button.module.settings.forEach { setting ->
            when (setting) {
                is BooleanSetting -> buttons.add(BooleanSettingButton(setting))
                is AbstractNumberSetting -> buttons.add(NumberSettingButton(setting))
                is AbstractModeSetting -> buttons.add(ModeSettingButton(setting))
                is MultiEnumSetting -> buttons.add(MultiEnumSettingButton(setting))
            }
        }

        refresh()
    }
}