package cute.neko.night.ui.widget.edit

import cute.neko.night.ui.widget.WidgetManager
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.Screen
import net.minecraft.text.Text

class EditScreen : Screen(Text.of("EditScreen")) {

    override fun render(context: DrawContext, mouseX: Int, mouseY: Int, delta: Float) {
        WidgetManager.draggableWidgets.forEach { widget ->
            widget.handleScreenRender(mouseX, mouseY, delta)
        }
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean {
        WidgetManager.draggableWidgets.forEach { widget ->
            widget.handleScreenMouseClick(mouseX, mouseY, button)
        }

        return false
    }

    override fun mouseReleased(mouseX: Double, mouseY: Double, button: Int): Boolean {
        WidgetManager.draggableWidgets.forEach { widget ->
            widget.handleScreenMouseRelease(mouseX, mouseY, button)
        }

        return false
    }
}