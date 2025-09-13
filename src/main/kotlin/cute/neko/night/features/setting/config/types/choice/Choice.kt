package cute.neko.night.features.setting.config.types.choice

import cute.neko.night.event.EventListener
import cute.neko.night.features.setting.AbstractSetting
import cute.neko.night.features.setting.config.Configurable
import cute.neko.night.features.setting.config.types.ToggleListener
import cute.neko.night.features.setting.config.types.Toggleable
import cute.neko.night.features.setting.type.mode.SubMode
import cute.neko.night.utils.client.inGame

abstract class Choice(override val modeName: String) : Configurable(modeName), Toggleable, SubMode, EventListener {
    abstract val controller: ChoicesConfigurable<*>

    override fun parent(): EventListener? = controller.parent

    fun isActive(): Boolean = controller.getActive() == this

    override fun onToggled(state: Boolean) {
        inner.filterIsInstance<ToggleListener>().forEach { it.onToggled(state) }

        if (!inGame) {
            return
        }

        super.onToggled(state)
    }

    override val running: Boolean
        get() = super.running && isActive()

    override val settings: MutableList<AbstractSetting<*>>
        get() = super.settings.map {
            it.visibility { isActive() }
        }.toMutableList()
}