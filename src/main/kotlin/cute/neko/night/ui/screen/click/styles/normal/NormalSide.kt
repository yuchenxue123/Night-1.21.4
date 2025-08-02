package cute.neko.night.ui.screen.click.styles.normal

import cute.neko.night.Night
import cute.neko.night.features.module.ModuleCategory
import cute.neko.night.ui.interfaces.Screen
import cute.neko.night.ui.isHovered
import cute.neko.night.ui.screen.click.styles.normal.StyleNormal.PANEL_HEIGHT
import cute.neko.night.ui.screen.click.styles.normal.StyleNormal.PANEL_RADIUS
import cute.neko.night.ui.screen.click.styles.normal.StyleNormal.SIDE_PANEL_WIDTH
import cute.neko.night.utils.render.nano.NanoUtils
import cute.neko.night.utils.render.nano.NanoFontManager
import net.minecraft.client.gui.DrawContext
import java.awt.Color

/**
 * @author yuchenxue
 * @date 2025/05/05
 */

object NormalSide : Screen {

    private val renderX: Float
        get() = StyleNormal.renderX

    private val renderY: Float
        get() = StyleNormal.renderY

    private const val UP_SPACE = 20f
    private const val TITLE_HEIGHT = 40f
    private const val TITLE_SIZE = 24f
    private const val MIDDLE_SPACE = 20f

    private const val CATEGORY_HEIGHT = 20f
    private const val CATEGORY_SPACE = 10f

    override fun render(context: DrawContext, mouseX: Int, mouseY: Int, delta: Float) {
        val font = NanoFontManager.GENSHIN

        NanoUtils.drawRoundRect(
            renderX,
            renderY,
            SIDE_PANEL_WIDTH,
            PANEL_HEIGHT,
            PANEL_RADIUS,
            Color(40, 40, 40, 120)
        )

        val title = Night.CLIENT_NAME
        font.drawText(
            title,
            renderX + (SIDE_PANEL_WIDTH - font.width(title, TITLE_SIZE)) / 2,
            renderY + UP_SPACE + (TITLE_HEIGHT - font.height(title, TITLE_SIZE)) / 2,
            Color(200, 200, 200, 255),
            TITLE_SIZE
        )

        var offset = UP_SPACE + TITLE_HEIGHT + MIDDLE_SPACE + CATEGORY_SPACE
        ModuleCategory.entries.forEach { category ->
            val text = category.showName
            val color = if (NormalMiddle.category == category) Color.WHITE else Color(200, 200, 200, 255)

            font.drawText(
                text,
                renderX + (SIDE_PANEL_WIDTH - font.width(text)) / 2,
                renderY + offset + (CATEGORY_HEIGHT - font.height(text)) / 2,
                color
            )

            offset += CATEGORY_HEIGHT + CATEGORY_SPACE
        }

    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int) {
        var offset = UP_SPACE + TITLE_HEIGHT + MIDDLE_SPACE + CATEGORY_SPACE

        ModuleCategory.entries.forEach { category ->
            val hovered = isHovered(
                renderX,
                renderY + offset,
                SIDE_PANEL_WIDTH,
                CATEGORY_HEIGHT,
                mouseX, mouseY
            )

            if (hovered && button == 0) {
                NormalMiddle.category = category
            }

            offset += CATEGORY_HEIGHT + CATEGORY_SPACE
        }
    }
}