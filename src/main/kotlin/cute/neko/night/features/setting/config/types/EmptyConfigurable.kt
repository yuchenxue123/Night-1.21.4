package cute.neko.night.features.setting.config.types

import cute.neko.event.EventListener
import cute.neko.night.features.setting.config.Configurable
import cute.neko.night.features.setting.config.types.choice.ChoicesConfigurable

/**
 * @author yuchenxue
 * @date 2025/07/06
 */

open class EmptyConfigurable(
    name: String,
    private val parent: EventListener? = null,
) : Configurable(name), EventListener {

    override fun parent(): EventListener? = parent

    open fun enable() {}
    open fun disable() {}

    fun newState(new: Boolean) {
        inner.filterIsInstance<ChoicesConfigurable<*>>().forEach { it.newState(new) }
        inner.filterIsInstance<ToggleConfigurable>().forEach { it.newState(new) }
        inner.filterIsInstance<EmptyConfigurable>().forEach { it.newState(new) }

        if (new) {
            enable()
        } else {
            disable()
        }
    }
}