package cute.neko.night.ui.screen.click.styles.drop.component

class Bars {
    private val bars = mutableListOf<Bar>()

    fun put(bar: Bar) {
        bars.add(bar)
    }

    fun clear() {
        bars.clear()
    }

    fun get(): MutableList<Bar> {
        return bars
    }

    fun top(bar: Bar) {
        bars.sortBy { it == bar }
    }
}