package cute.neko.night.ui.widget

import cute.neko.event.EventListener
import cute.neko.event.handler
import cute.neko.night.event.events.game.render.ScreenRenderEvent
import cute.neko.night.features.module.render.ModuleInterface
import cute.neko.night.ui.widget.type.ArraylistWidget
import cute.neko.night.ui.widget.type.DynamicIslandWidget
import cute.neko.night.ui.widget.type.TargetWidget
import cute.neko.night.utils.interfaces.Accessor

/**
 * @author yuchenxue
 * @date 2025/06/01
 */

object WidgetManager : Accessor, EventListener {

    private val widgets = mutableListOf<AbstractWidget>()

    fun load() {
        register(
            ArraylistWidget,
            TargetWidget,
            DynamicIslandWidget
        )
    }

    private fun register(vararg array: AbstractWidget) {
        widgets.addAll(array)
    }

    private var lastWindow = mc.window

    @Suppress("unused")
    private val onScreenRender = handler<ScreenRenderEvent> { event ->

        widgets.forEach {
            if (!it.condition) return@forEach

            it.render(event.context)
        }

        val window = event.window

        if (window.width != lastWindow.width || window.height != lastWindow.height) {

            widgets.forEach {
                if (!it.condition) return@forEach

                it.onWindowResize(window.width.toFloat(), window.height.toFloat())
            }

            lastWindow = window
        }
    }

    override val running: Boolean
        get() = ModuleInterface.running
}