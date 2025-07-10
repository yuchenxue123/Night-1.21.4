package cute.neko.night.features.setting.config.types.choice

import cute.neko.night.event.EventListener
import cute.neko.night.features.setting.AbstractSetting
import cute.neko.night.features.setting.config.Configurable
import cute.neko.night.features.setting.config.types.EmptyConfigurable
import cute.neko.night.features.setting.config.types.ToggleConfigurable

/**
 * @author yuchenxue
 * @date 2025/05/10
 */

open class ChoicesConfigurable<T : Choice>(
    name: String,
    private val choices: Array<T>,
    default: T = choices[0],
    val parent: EventListener? = null,
) : Configurable(name) {

    private var active = default

    fun getActive(): T = active

    @Suppress("unused")
    private val mode by mode(name, choices.map { it.modeName }.toTypedArray())
        .listener { old, new ->
            choices.find { it.modeName.equals(old, true)}?.disable()
            val newChoice = choices.find { it.modeName.equals(new, true)} ?: choices[0]
            active = newChoice
            newChoice.enable()
        }

    fun newState(new: Boolean) {
        inner.filterIsInstance<ChoicesConfigurable<*>>().forEach { it.newState(new) }
        inner.filterIsInstance<ToggleConfigurable>().forEach { it.newState(new) }
        inner.filterIsInstance<EmptyConfigurable>().forEach { it.newState(new) }

        if (new) {
            this.getActive().enable()
        } else {
            this.getActive().disable()
        }
    }

    override val settings: MutableList<AbstractSetting<*>>
        get() = super.settings.also {
            choices.forEach { choice ->
                it += choice.settings
            }
        }
}