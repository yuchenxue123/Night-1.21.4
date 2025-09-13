package cute.neko.night.utils.extensions.math

open class Matrix(val rows: Int, val cols: Int, init: IntArray = IntArray(rows * cols) { 0 }) {
    val data = Array(rows) { IntArray(cols) }
    val size: Int = rows * cols

    init {
        var index = 0
        for (i in 0 until rows) {
            for (j in 0 until cols) {
                data[i][j] = if (init.size == rows * cols) init[index++] else 0
            }
        }
    }

    fun getIntArray(): IntArray {
        val array = IntArray(rows * cols) { 0 }
        var index = 0
        for (i in 0 until rows) {
            for (j in 0 until cols) {
                array[index++] = data[i][j]
            }
        }
        return array
    }

    fun multiply(matrix: Matrix): Matrix {
        if (this.cols != matrix.rows) return this
        val result = Matrix(this.rows, matrix.cols)

        for (i in 0 until this.rows) {
            for (j in 0 until matrix.cols) {
                for (k in 0 until this.cols) {
                    result[i][j] += this[i][k] * matrix[k][j]
                }
            }
        }

        return result
    }

    fun inverse(): Matrix {
        require(this.rows == this.cols) { "Matrix must have the same number of rows and cols." }
        val determinant = Matrix(rows, cols).toDeterminant()
        require(determinant.result() != 0) { "Determinant can't equals 0." }
        val result = determinant.adjoint() / determinant.result()
        return result
    }

    open fun subtract(row: Int, col: Int): Matrix {
        require(row in 0 until this.rows && col in 0 until this.cols) { "Out of index." }
        val result = Matrix(rows - 1, cols - 1)
        var subRow = 0
        for (i in 0 until rows) {
            if (i == row) continue
            var subCol = 0
            for (j in 0 until cols) {
                if (j == col) continue
                result[subRow][subCol] = this[i][j]
                subCol++
            }
            subRow++
        }
        return result
    }

    fun print() {
        for (i in 0 until rows) {
            println(data[i].joinToString("\t"))
        }
    }

    fun toDeterminant(): Determinant {
        require(this.rows == this.cols) { "Matrix must have the same number of rows and cols." }
        return Determinant(rows, this.getIntArray())
    }

    operator fun get(i: Int): IntArray {
        require(i in 0 until rows) { "Out of index." }
        return data[i]
    }

    operator fun set(i: Int, value: IntArray) {
        require(i in 0 until rows) { "Out of index." }
        data[i] = value
    }

    /**
     * @param matrix 必须行列相同，否则返回前一个矩阵
     */
    operator fun plus(matrix: Matrix): Matrix {
        if (matrix.rows != this.rows || matrix.cols != this.cols) return this
        val result = Matrix(rows, cols)
        for (i in 0 until this.rows) {
            for (j in 0 until this.cols) {
                result[i][j] = this[i][j] + matrix[i][j]
            }
        }
        return result
    }

    /**
     * @param matrix 必须行列相同，否则返回前一个矩阵
     */
    operator fun minus(matrix: Matrix): Matrix {
        if (matrix.rows != this.rows || matrix.cols != this.cols) return this
        val result = Matrix(rows, cols)
        for (i in 0 until this.rows) {
            for (j in 0 until this.cols) {
                result[i][j] = this[i][j] - matrix[i][j]
            }
        }
        return result
    }

    operator fun times(n: Int): Matrix {
        val result = Matrix(rows, cols)
        for (i in 0 until rows) {
            for (j in 0 until cols) {
                result[i][j] = data[i][j] * n
            }
        }
        return result
    }

    operator fun timesAssign(n: Int) {
        for (i in 0 until rows) {
            for (j in 0 until cols) {
                data[i][j] *= n
            }
        }
    }

    operator fun div(n: Int): Matrix {
        val result = Matrix(rows, cols)
        for (i in 0 until rows) {
            for (j in 0 until cols) {
                result[i][j] = this[i][j] / n
            }
        }
        return result
    }

    operator fun iterator(): Iterator<Int> {
        return object : Iterator<Int> {
            var currentRow = 0
            var currentCol = 0

            override fun hasNext(): Boolean {
                return currentRow < rows && currentCol < cols
            }

            override fun next(): Int {
                val result = data[currentRow][currentCol]
                currentCol++
                if (currentCol >= rows) {
                    currentCol = 0
                    currentRow++
                }
                return result
            }
        }
    }
}

class Determinant(val n: Int, init: IntArray = IntArray(n * n) { 0 }) : Matrix(n, n, init) {

    fun result(): Int {
        val matrix = this.data
        val n = matrix.size

        if (n == 1) return matrix[0][0]
        if (n == 2) return matrix[0][0] * matrix[1][1] - matrix[0][1] * matrix[1][0]

        var reslut = 0

        for (i in 0 until n) {
            val minor = this.subtract(0, i)
            reslut += matrix[0][i] * minor.result() * if (i % 2 == 0) 1 else -1
        }

        return reslut
    }

    /**
     * 伴随矩阵
     */
    fun adjoint(): Determinant {
        val result = Determinant(n)
        for (i in 0 until n) {
            for (j in 0 until n) {
                result[i][j] = this.subtract(i, j).result() * if ((i + j) % 2 == 0) 1 else -1
            }
        }
        return result
    }

    override fun subtract(row: Int, col: Int): Determinant {
        return super.subtract(row, col).toDeterminant()
    }

    fun toMatrix(): Matrix {
        return Matrix(rows, cols, this.getIntArray())
    }
}