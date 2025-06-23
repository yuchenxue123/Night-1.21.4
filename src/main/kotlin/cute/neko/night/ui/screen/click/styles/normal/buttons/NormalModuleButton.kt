package cute.neko.night.ui.screen.click.styles.normal.buttons

import cute.neko.night.ui.interfaces.Screen
import cute.neko.night.ui.isHovered
import cute.neko.night.ui.screen.click.styles.normal.NormalMain
import cute.neko.night.ui.screen.click.styles.normal.NormalMiddle
import cute.neko.night.ui.screen.click.styles.normal.NormalMiddle.MODULE_BUTTON_HEIGHT
import cute.neko.night.ui.screen.click.styles.normal.StyleNormal.MIDDLE_PANEL_WIDTH
import cute.neko.night.utils.animation.AnimationType
import cute.neko.night.utils.animation.SimpleAnimation
import cute.neko.night.utils.nano.font.NanoFontManager
import net.minecraft.client.gui.DrawContext
import java.awt.Color

/**
 * @author yuchenxue
 * @date 2025/05/05
 */

class NormalModuleButton(
    val module: cute.neko.night.features.module.ClientModule
) : Screen {

    private val renderX: Float
        get() = NormalMiddle.renderX
    private val renderY: Float
        get() = NormalMiddle.renderY + NormalMiddle.addition

    private var offset = 0f

    private val offsetAnimation = SimpleAnimation.create().type(AnimationType.QUAD_OUT).duration(100f).target(0f)

    override fun render(context: DrawContext, mouseX: Int, mouseY: Int, delta: Float) {
        offset = offsetAnimation.animate()

        val font = NanoFontManager.GENSHIN

        val text = module.showName
        val color = if (module == NormalMain.module) Color.WHITE else Color(200, 200, 200, 255)

        font.render(
            context,
            text,
            renderX + (MIDDLE_PANEL_WIDTH - font.width(text)) / 2,
            renderY + offset + (MODULE_BUTTON_HEIGHT - font.height(text)) / 2,
            color
        )
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int) {
        val hovered = isHovered(renderX, renderY + offset, MIDDLE_PANEL_WIDTH, MODULE_BUTTON_HEIGHT, mouseX, mouseY)

        if (hovered && button == 0) {
            NormalMain.module = module
        }
    }

    fun offset(offset: Float) = apply {
        if (offset == this.offset || offset == offsetAnimation.target) {
            return@apply
        }

        offsetAnimation.start(this.offset).target(offset).reset()
    }
}