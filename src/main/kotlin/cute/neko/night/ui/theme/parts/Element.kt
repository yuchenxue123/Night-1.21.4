package cute.neko.night.ui.theme.parts

import net.minecraft.client.gui.DrawContext

abstract class Element(
    adapter: BuilderAdapter<*> = BuilderAdapter.EMPTY
) {

    init {
        adapter.build(this)
    }

    private val elements: MutableList<Element> = mutableListOf()

    abstract fun render(context: DrawContext, mouseX: Double, mouseY: Double, deltaTime: Float)

    open fun mouseClicked(mouseX: Double, mouseY: Double, button: Int) {}

    open fun mouseReleased(mouseX: Double, mouseY: Double, button: Int) {}

    open fun mouseScrolled(mouseX: Double, mouseY: Double, horizontal: Double, vertical: Double) {}

    open fun keyPressed(keyCode: Int, scanCode: Int, modifiers: Int) {}

    open fun keyReleased(keyCode: Int, scanCode: Int, modifiers: Int) {}

    fun element(element: Element) = apply {
        elements.add(element)
    }

    abstract class BuilderAdapter<T> {

        abstract val items: Iterable<T>

        companion object {
            val EMPTY = object : BuilderAdapter<Any>() {
                override val items = emptyList<Any>()
                override fun accept(element: Element, module: Any) {}
            }
        }

        abstract fun accept(element: Element, module: T)

        fun build(element: Element) = items.forEach { item -> accept(element, item) }
    }
}