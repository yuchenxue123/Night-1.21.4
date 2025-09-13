package cute.neko.night.ui.screen.click.styles.drop

import cute.neko.night.features.module.ModuleCategory
import cute.neko.night.ui.isHovered
import cute.neko.night.ui.screen.click.styles.drop.Constants.CATEGORY_BOTTOM_SPACE
import cute.neko.night.ui.screen.click.styles.drop.Constants.CATEGORY_BUTTON_SPACE
import cute.neko.night.ui.screen.click.styles.drop.Constants.CATEGORY_RADIUS
import cute.neko.night.ui.screen.click.styles.drop.Constants.COMPONENT_HEIGHT
import cute.neko.night.ui.screen.click.styles.drop.Constants.COMPONENT_WIDTH
import cute.neko.night.ui.screen.click.styles.drop.component.Bar
import cute.neko.night.ui.screen.click.styles.drop.component.Bars
import cute.neko.night.utils.animation.AnimationType
import cute.neko.night.utils.animation.SimpleAnimation
import cute.neko.night.utils.extensions.sum
import cute.neko.night.utils.misc.option.BooleanOption
import cute.neko.night.utils.misc.option.FloatOption
import cute.neko.night.utils.render.nano.NanoFontManager
import cute.neko.night.utils.render.nano.NanoUtils
import net.minecraft.client.gui.DrawContext
import java.awt.Color

class DropCategoryBar(
    private val category: ModuleCategory,
    x: Float,
    y: Float,
    width: Float,
    height: Float,
) : Bar(x, y, width, height) {
    override val bars: Bars
        get() = StyleDrop.bars

    private val font = NanoFontManager.GENSHIN

    private val buttons = mutableListOf<DropModuleButton>()

    init {
        build()
    }

    val open = BooleanOption(false)

    private val height_option = FloatOption(height())
    private val height_animation = SimpleAnimation.create()
        .type(AnimationType.QUAD_OUT)
        .target(height())
        .finish()

    override fun render(context: DrawContext, mouseX: Int, mouseY: Int, delta: Float) {
        super.render(context, mouseX, mouseY, delta)

        refresh()

        height_option.animate(height_animation)

        update()

        NanoUtils.drawRoundRect(
            x(), y(),
            width(), height_option.get(),
            CATEGORY_RADIUS,
            Color( 50, 50, 50)
        )

        val text = category.showName
        font.drawText(
            text,
            x() + (COMPONENT_WIDTH - font.width(text)) / 2,
            y() + (COMPONENT_HEIGHT - font.height(text)) / 2,
            Color(255, 255, 255)
        )

        if (open.get()) {
            NanoUtils.drawHorizontalLine(
                x(), y() + height(),
                width(),
                0.5f,
                Color(255, 255, 255)
            )
        }

        val height = height_option.get() - height() - CATEGORY_BUTTON_SPACE - CATEGORY_BOTTOM_SPACE

        if (height > 0) {
            NanoUtils.scissor(
                x(),
                y() + height() + CATEGORY_BUTTON_SPACE,
                width(),
                height
            ) {
                buttons.forEach {
                    it.render(context, mouseX, mouseY, delta)
                }
            }
        }
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int) {
        // 只点一个
        if (bars.get().any { it.focus.get() }) {
            return
        }

        super.mouseClicked(mouseX, mouseY, button)

        val hovered = isHovered(x(), y(), width(), height(), mouseX, mouseY)

        if (hovered) {
            when (button) {
                1 -> {
                    open.toggle { new ->
                        when (new) {
                            true -> {
                                height_animation
                                    .start(height_option.get())
                                    .target(getHeight())
                                    .reset()
                            }

                            false -> {
                                height_animation
                                    .start(height_option.get())
                                    .target(height())
                                    .reset()
                            }
                        }
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

    override fun mouseReleased(mouseX: Double, mouseY: Double, button: Int) {
        super.mouseReleased(mouseX, mouseY, button)

        if (open.get()) {
            buttons.forEach {
                it.mouseReleased(mouseX, mouseY, button)
            }
        }
    }

    override fun keyPressed(keyCode: Int, scanCode: Int, modifiers: Int) {

        if (open.get()) {
            buttons.forEach {
                it.keyPressed(keyCode, scanCode, modifiers)
            }
        }
    }

    fun update() {
        if (open.get()) {
            height_animation
                .target(getAnimationHeight())
        }
    }

    private fun getHeight(): Float {
        return height() + if (open.get()) {
            CATEGORY_BUTTON_SPACE + buttons.sum { it.getHeight() } + CATEGORY_BOTTOM_SPACE
        } else {
            0f
        }
    }

    private fun getAnimationHeight(): Float {
        return height() + if (open.get()) {
            CATEGORY_BUTTON_SPACE + buttons.sum { it.getAnimateHeight() } + CATEGORY_BOTTOM_SPACE
        } else {
            0f
        }
    }

    private fun refresh() {
        var offset = y() + height() + CATEGORY_BUTTON_SPACE

        buttons.forEach {
            it.position(x = x(), y = offset)
            offset += it.getAnimateHeight()
        }
    }

    private fun build() {
        buttons.clear()

        category.modules.forEachIndexed { index, module ->
            buttons.add(DropModuleButton(
                this,
                module,
                x(),
                y() + height() + CATEGORY_BUTTON_SPACE + index * COMPONENT_HEIGHT,
                width(),
                COMPONENT_HEIGHT
            ))
        }
    }
}