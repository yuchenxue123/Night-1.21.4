package cute.neko.night.features.setting.type.mode

class ModeSetting(
    name: String,
    override val modes: Array<String>,
    value: String,
    visibility: () -> Boolean = { true }
) : AbstractModeSetting<String>(name, value, visibility) {

    override fun set(value: String) {
        val new = modes.find { it.equals(value, true) } ?: modes[0]
        super.set(new)
    }

    override fun getAsString(): String = value
    override fun setByString(value: String) {
        this.set(value)
    }
}