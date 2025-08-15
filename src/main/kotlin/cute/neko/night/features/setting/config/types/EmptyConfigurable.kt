package cute.neko.night.features.setting.config.types

import cute.neko.night.event.EventListener
import cute.neko.night.features.setting.config.Configurable
import cute.neko.night.utils.client.inGame

/**
 * @author yuchenxue
 * @date 2025/07/06
 */

open class EmptyConfigurable(
    name: String,
    private val parent: EventListener? = null,
) : Configurable(name), Toggleable, EventListener {

    override fun parent(): EventListener? = parent

    override fun onToggled(state: Boolean) {
        inner.filterIsInstance<ToggleListener>().forEach { it.onToggled(state) }

        if (!inGame) {
            return
        }

        super.onToggled(state)
    }
}