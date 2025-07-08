package cute.neko.night.ui.screen.click.styles.normal.setting

import cute.neko.night.features.setting.AbstractSetting
import cute.neko.night.ui.isHovered
import cute.neko.night.ui.screen.click.styles.normal.setting.mode.ModeButton
import cute.neko.night.utils.animation.AnimationType
import cute.neko.night.utils.animation.SimpleAnimation
import cute.neko.night.utils.nano.NanoUtils
import net.minecraft.client.gui.DrawContext
import java.awt.Color
import kotlin.math.max

/**
 * @author yuchenxue
 * @date 2025/05/06
 */

abstract class AbstractSelectSetting<T : AbstractSetting<*>>(
    setting: T
) : AbstractSettingButton<T>(setting) {

    private var open = false

    // expand height
    private var expand = 0f
    private val animation = SimpleAnimation.create().type(AnimationType.QUAD_OUT).duration(100f).target(expand).finish()

    // buttons
    protected val buttons = mutableListOf<ModeButton>()

    // total height of
    protected var bottom = 0f

    init {
        this.build()
    }

    override fun render(context: DrawContext, mouseX: Int, mouseY: Int, delta: Float) {
        super.render(context, mouseX, mouseY, delta)

        refresh(width - padding * 2)

        expand = animation.animate()

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
            text,
            renderX + padding,
            renderY + offset + padding,
            Color(200, 200, 200, 255)
        )

        NanoUtils.scissor(renderX, renderY + offset, width, height) {
            buttons.forEach {
                it.render(context, mouseX, mouseY, delta)
            }
        }
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int) {
        val hovered = isHovered(renderX, renderY + offset, width, height, mouseX, mouseY)

        if (hovered && button == 1) {
            update(open.not())
        }

        buttons.forEach {
            it.mouseClicked(mouseX, mouseY, button)
        }
    }

    private fun update(bln: Boolean) {
        if (bln == open) {
            return
        }

        when {
            bln -> {
                build()
                animation.start(expand).target(bottom).reset()
            }

            else -> {
                animation.start(expand).target(0f).reset()
            }
        }

        open = bln
    }

    protected fun refresh(width: Float): Float {
        val space = 10f

        var current = 0f
        // y offset
        var offset = 5f
        var lastRowHeight = 0f

        buttons.forEach { button ->
            if (current > 0f && current + button.width > width) {
                offset += lastRowHeight + space
                current = 0f
                lastRowHeight = 0f
            }

            val top = font.height(setting.name) + padding * 2
            button.position(renderX + padding + current, renderY + top + this.offset + offset)

            current += button.width + space

            lastRowHeight = max(lastRowHeight, button.height)
        }

        return offset + lastRowHeight
    }

    abstract fun build()

    override val height: Float
        get() = padding + font.height(setting.name) + padding + expand + if (open) padding else 0f

    override fun getFinalHeight(): Float {
        return font.height(setting.name) + padding * 2 + animation.target + if (open) padding else 0f
    }
}