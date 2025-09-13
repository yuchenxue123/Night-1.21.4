package cute.neko.night.ui.screen.click.styles.normal

import cute.neko.night.features.module.ModuleCategory
import cute.neko.night.features.module.ModuleManager
import cute.neko.night.ui.interfaces.Screen
import cute.neko.night.ui.isHovered
import cute.neko.night.ui.screen.click.styles.normal.StyleNormal.MIDDLE_PANEL_WIDTH
import cute.neko.night.ui.screen.click.styles.normal.StyleNormal.PANEL_HEIGHT
import cute.neko.night.ui.screen.click.styles.normal.StyleNormal.PANEL_RADIUS
import cute.neko.night.ui.screen.click.styles.normal.StyleNormal.PANEL_SPACE
import cute.neko.night.ui.screen.click.styles.normal.StyleNormal.SIDE_PANEL_WIDTH
import cute.neko.night.ui.screen.click.styles.normal.buttons.NormalModuleButton
import cute.neko.night.utils.animation.AnimationType
import cute.neko.night.utils.animation.SimpleAnimation
import cute.neko.night.utils.render.nano.NanoUtils
import net.minecraft.client.gui.DrawContext
import java.awt.Color
import kotlin.math.min

object NormalMiddle : Screen {

    val renderX: Float
        get() = StyleNormal.renderX + SIDE_PANEL_WIDTH + PANEL_SPACE
    val renderY: Float
        get() = StyleNormal.renderY

    private const val UP_BOTTOM_SPACE = 20f
    const val MODULE_BUTTON_HEIGHT = 20f
    private const val MODULE_BUTTON_SPACE = 10f

    var category = ModuleCategory.COMBAT
        set(value) {
            if (value == field) return
            field = value
            reset()
        }

    private val buttons = mutableListOf<NormalModuleButton>()

    // module button addition y offset
    var addition = 0f
    private val animation = SimpleAnimation.create().type(AnimationType.QUAD_OUT).duration(120f)

    init {
        reset()
    }

    override fun render(context: DrawContext, mouseX: Int, mouseY: Int, delta: Float) {
        addition = animation.animate()

        refresh()

        // background
        NanoUtils.drawRoundRect(
            renderX,
            renderY,
            MIDDLE_PANEL_WIDTH,
            PANEL_HEIGHT,
            PANEL_RADIUS,
            Color(40, 40, 40, 120)
        )

        // buttons
        NanoUtils.scissor(
            renderX,
            renderY + UP_BOTTOM_SPACE,
            MIDDLE_PANEL_WIDTH,
            PANEL_HEIGHT  - UP_BOTTOM_SPACE * 2,
        ) {
            buttons.forEach {
                if (it.module.category != category) {
                    return@forEach
                }
                it.render(context, mouseX, mouseY, delta)
            }
        }
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int) {
        buttons.forEach {
            if (it.module.category != category) {
                return@forEach
            }
            it.mouseClicked(mouseX, mouseY, button)
        }
    }

    override fun mouseScrolled(mouseX: Double, mouseY: Double, horizontal: Double, vertical: Double) {
        val hovered = isHovered(
            renderX, renderY + UP_BOTTOM_SPACE,
            MIDDLE_PANEL_WIDTH, PANEL_HEIGHT  - UP_BOTTOM_SPACE * 2,
            mouseX, mouseY
        )

        if (!hovered) return

        if (vertical == .0) {
            return
        }

        val height = category.modules.size * (MODULE_BUTTON_HEIGHT + MODULE_BUTTON_SPACE) + MODULE_BUTTON_SPACE

        val minScroll = PANEL_HEIGHT - UP_BOTTOM_SPACE * 2 - height

        val newScroll = (addition + vertical.toFloat() * 30f).coerceIn(min(minScroll, 0f), 0f)
        animation.start(addition).target(newScroll).reset()
    }

    private fun refresh() {
        var offset = UP_BOTTOM_SPACE + MODULE_BUTTON_SPACE
        buttons.forEach { button ->
            if (button.module.category != category) {
                return@forEach
            }
            button.offset(offset)

            offset += MODULE_BUTTON_HEIGHT + MODULE_BUTTON_SPACE
        }
    }

    private fun reset() {
        animation.target(0f).finish()
        build()
        if (category.modules.isNotEmpty()) {
            NormalMain.module = category.modules.first()
        } else {
            NormalMain.module = null
        }
    }

    private fun build() {
        buttons.clear()

        ModuleManager.modules
            .sortedBy { it.name }
            .forEach { m ->
                buttons.add(NormalModuleButton(m))
            }

        refresh()
    }
}