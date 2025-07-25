package cute.neko.night.features.setting.config.types.choice

import cute.neko.event.EventListener
import cute.neko.night.features.setting.AbstractSetting
import cute.neko.night.features.setting.config.Configurable
import cute.neko.night.features.setting.type.mode.SubMode

/**
 * @author yuchenxue
 * @date 2025/05/10
 */

abstract class Choice(override val modeName: String) : Configurable(modeName), SubMode, EventListener {
    abstract val controller: ChoicesConfigurable<*>

    open fun enable() {}
    open fun disable() {}

    override fun parent(): EventListener? = controller.parent

    fun isActive(): Boolean = controller.getActive() == this

    override val running: Boolean
        get() = super.running && isActive()

    override val settings: MutableList<AbstractSetting<*>>
        get() = super.settings.map {
            it.visibility { isActive() }
        }.toMutableList()
}