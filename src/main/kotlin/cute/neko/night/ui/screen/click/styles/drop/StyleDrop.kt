package cute.neko.night.ui.screen.click.styles.drop

import cute.neko.night.features.module.ModuleCategory
import cute.neko.night.ui.screen.click.styles.Style
import cute.neko.night.ui.screen.click.styles.drop.Constants.COMPONENT_HEIGHT
import cute.neko.night.ui.screen.click.styles.drop.Constants.COMPONENT_WIDTH
import cute.neko.night.ui.screen.click.styles.drop.component.Bars
import cute.neko.night.utils.extensions.reversedList
import cute.neko.night.utils.render.nano.NanoUtils
import net.minecraft.client.gui.DrawContext

object StyleDrop : Style() {

    val bars = Bars()

    init {
        build()
    }

    override fun open() {
        super.open()
    }

    override fun close() {
        super.close()
    }

    override fun render(context: DrawContext, mouseX: Int, mouseY: Int, delta: Float) {
        NanoUtils.draw {
            bars.get().forEach {
                it.render(context, mouseX, mouseY, delta)
            }
        }
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int) {
        bars.get().reversedList().forEach {
            it.mouseClicked(mouseX, mouseY, button)
        }
    }

    override fun mouseReleased(mouseX: Double, mouseY: Double, button: Int) {
        bars.get().reversedList().forEach {
            it.mouseReleased(mouseX, mouseY, button)
        }
    }

    override fun keyPressed(keyCode: Int, scanCode: Int, modifiers: Int) {
        bars.get().forEach {
            it.keyPressed(keyCode, scanCode, modifiers)
        }
    }

    override fun keyReleased(keyCode: Int, scanCode: Int, modifiers: Int) {
        bars.get().forEach {
            it.keyReleased(keyCode, scanCode, modifiers)
        }
    }

    private fun build() {
        bars.clear()

        ModuleCategory.entries.forEachIndexed { index, category ->
            bars.put(DropCategoryBar(
                category,
                40f + index * (COMPONENT_WIDTH + 10f),
                40f,
                COMPONENT_WIDTH,
                COMPONENT_HEIGHT
            ))
        }
    }
}