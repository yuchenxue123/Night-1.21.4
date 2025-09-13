package cute.neko.night.utils.misc.option

class BooleanOption(default: Boolean) : SimpleOption<Boolean>(default) {

    fun toggle(block: (new: Boolean) -> Unit = {}) {
        set(!get())
        block.invoke(get())
    }
}