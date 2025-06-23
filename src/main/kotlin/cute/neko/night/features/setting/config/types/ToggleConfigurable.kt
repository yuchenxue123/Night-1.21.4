package cute.neko.night.features.setting.config.types

import cute.neko.night.event.EventListener
import cute.neko.night.features.setting.AbstractSetting
import cute.neko.night.features.setting.config.Configurable
import cute.neko.night.features.setting.config.types.choice.ChoicesConfigurable
import cute.neko.night.utils.interfaces.Accessor

/**
 * @author yuchenxue
 * @date 2025/05/10
 */

 open class ToggleConfigurable(
    name: String,
    state: Boolean,
    private val parent: EventListener? = null
) : Configurable(name), EventListener, Accessor {

    private val enable by boolean(name, state)
        .listener { _, new ->
            if (new) {
                enable()
            } else {
                disable()
            }
        }

    open fun enable() {}
    open fun disable() {}

    fun newState(new: Boolean) {
        inner.filterIsInstance<ChoicesConfigurable<*>>().forEach { it.newState(new) }
        inner.filterIsInstance<ToggleConfigurable>().forEach { it.newState(new) }

        if (new) {
            enable()
        } else {
            disable()
        }
    }

    override fun parent(): EventListener? = parent

    override val running: Boolean
        get() = super.running && enable

    override val settings: MutableList<AbstractSetting<*>>
        get() = super.settings.mapIndexed { index, value ->
            if (index == 0) value else value.visibility { enable }
        }.toMutableList()
}