package cute.neko.night.utils.extensions.math

class Fibonacci(val size: Int) {
    val data = IntArray(size)

    init {
        for (i in 0 until size) {
            data[i] = getFibonacci(i + 1)
        }
    }

    operator fun get(i: Int): Int {
        require(i in 0 until size) { "Out of index." }
        return data[i]
    }
}

fun getFibonacci(n: Int): Int {
    if (n <= 0) return 0
    return if (n == 1) 1 else getFibonacci(n - 1) + getFibonacci(n - 2)
}