package cute.neko.night.features.setting.config.types

import cute.neko.night.event.EventListener
import cute.neko.night.features.setting.AbstractSetting
import cute.neko.night.features.setting.config.Configurable
import cute.neko.night.utils.client.inGame

/**
 * @author yuchenxue
 * @date 2025/05/10
 */

 open class ToggleConfigurable(
    name: String,
    state: Boolean,
    private val parent: EventListener? = null
) : Configurable(name), Toggleable, EventListener {

    private val enable by boolean(name, state)
        .listener { _, new -> onToggled(new) }

    override fun onToggled(state: Boolean) {
        inner.filterIsInstance<ToggleListener>().forEach { it.onToggled(state) }

        if (!inGame) {
            return
        }

        super.onToggled(state)
    }

    override fun parent(): EventListener? = parent

    override val running: Boolean
        get() = super.running && enable

    override val settings: MutableList<AbstractSetting<*>>
        get() = super.settings.mapIndexed { index, value ->
            if (index == 0) value else value.visibility { enable }
        }.toMutableList()
}