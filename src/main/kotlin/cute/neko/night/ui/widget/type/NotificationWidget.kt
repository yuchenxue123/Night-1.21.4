package cute.neko.night.ui.widget.type

import cute.neko.night.ui.widget.AbstractWidget
import cute.neko.night.ui.widget.WidgetType
import net.minecraft.client.gui.DrawContext

/**
 * @author yuchenxue
 * @date 2025/06/03
 */

object NotificationWidget : AbstractWidget(
    WidgetType.NOTIFICATION,
    0f,
    0f,
    0f,
    0f
) {
    override fun render(context: DrawContext) {
    }
}