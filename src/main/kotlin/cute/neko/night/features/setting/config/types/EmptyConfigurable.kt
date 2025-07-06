package cute.neko.night.features.setting.config.types

import cute.neko.night.event.EventListener
import cute.neko.night.features.setting.config.Configurable
import cute.neko.night.utils.interfaces.Accessor

/**
 * @author yuchenxue
 * @date 2025/07/06
 */

open class EmptyConfigurable(
    name: String,
    private val parent: EventListener? = null,
) : Configurable(name), EventListener, Accessor {

    override fun parent(): EventListener? = parent
}