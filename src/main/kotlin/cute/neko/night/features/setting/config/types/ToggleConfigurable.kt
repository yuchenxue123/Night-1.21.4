package cute.neko.night.features.setting.config.types

import cute.neko.night.event.EventListener
import cute.neko.night.event.removeEventListenerScope
import cute.neko.night.features.setting.AbstractSetting
import cute.neko.night.features.setting.config.Configurable

open class ToggleConfigurable(
    name: String,
    state: Boolean,
    private val parent: EventListener? = null
) : Configurable(name), Toggleable, EventListener {

    open var enable by boolean(name, state)
        .listener { _, new -> onToggled(new) }

    override fun onToggled(state: Boolean): Boolean {
        if (!state) {
            runCatching {
                // Remove and cancel coroutine scope
                removeEventListenerScope()
            }.onFailure {
                error("failed cancel sequences: $it")
            }
        }

        val state = super.onToggled(state)
        inner.filterIsInstance<Toggleable>().forEach { it.onToggled(state) }
        return state
    }

    override fun parent(): EventListener? = parent

    override val running: Boolean
        get() = super.running && enable

    override val settings: MutableList<AbstractSetting<*>>
        get() = super.settings.mapIndexed { index, value ->
            if (index == 0) value else value.visibility { enable }
        }.toMutableList()
}