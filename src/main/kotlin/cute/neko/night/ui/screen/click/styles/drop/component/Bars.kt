package cute.neko.night.ui.screen.click.styles.drop.component

/**
 * @author yuchenxue
 * @date 2025/08/08
 */

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