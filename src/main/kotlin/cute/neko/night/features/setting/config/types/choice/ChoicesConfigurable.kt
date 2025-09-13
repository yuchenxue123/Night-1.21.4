package cute.neko.night.features.setting.config.types.choice

import cute.neko.night.event.EventListener
import cute.neko.night.features.setting.AbstractSetting
import cute.neko.night.features.setting.config.Configurable
import cute.neko.night.features.setting.config.types.ToggleListener

open class ChoicesConfigurable<T : Choice>(
    name: String,
    private val choices: Array<T>,
    default: T = choices[0],
    val parent: EventListener? = null,
) : Configurable(name), ToggleListener {

    private var activeChoice = default

    fun getActive(): T = activeChoice

    @Suppress("unused")
    private val mode by mode(name, choices.map { it.modeName }.toTypedArray())
        .listener { _, new -> set(new) }

    override fun onToggled(state: Boolean) {
        inner.filterIsInstance<ToggleListener>().forEach { it.onToggled(state) }

        activeChoice.onToggled(state)
    }

    private fun set(modeName: String) {
        val newChoice = choices.firstOrNull {
            it.modeName.equals(modeName, true)
        } ?: choices[0]

        if (activeChoice == newChoice) {
            return
        }

        if (activeChoice.running) {
            activeChoice.disable()
        }

        activeChoice = newChoice

        if (activeChoice.running) {
            activeChoice.enable()
        }
    }

    override val settings: MutableList<AbstractSetting<*>>
        get() = super.settings.also {
            choices.forEach { choice ->
                it += choice.settings
            }
        }
}