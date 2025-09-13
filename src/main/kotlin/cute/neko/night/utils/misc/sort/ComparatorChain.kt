package cute.neko.night.utils.misc.sort

class ComparatorChain<T>(
    private vararg val conditions: Comparator<T>
) : Comparator<T> {

    override fun compare(o1: T, o2: T): Int {
        for (condition in conditions) {
            val result = condition.compare(o1, o2)

            if (result != 0) {
                return result
            }
        }
        return 0
    }
}