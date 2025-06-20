package cute.neko.night.utils.extensions

import net.minecraft.util.math.Vec3d
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.round
import kotlin.math.sqrt

/**
 * @author yuchenxue
 * @date 2025/05/06
 */

fun Double.decimals(n: Int): Double {
    val bigDecimal = BigDecimal(this)
    val rounded = bigDecimal.setScale(n, RoundingMode.HALF_UP)
    return rounded.toDouble()
}

fun Double.toRadians(): Double = this * 0.017453292519943295
fun Double.toDegrees(): Double = this * 57.29577951308232
fun Double.step(step: Double): Double = round(this / step) * step
fun Double.squared(): Double = this * this
infix fun Double.hypotenuse(other: Double): Double = sqrt(this.squared() + other.squared())

fun Float.toRadians() = this * 0.017453292f
fun Float.toDegrees() = this * 57.29578f
fun Float.decimals(n: Int): Float = this.toDouble().decimals(n).toFloat()
fun Float.step(step: Float): Float = round(this / step) * step
fun Float.squared(): Float = this * this
infix fun Float.hypotenuse(other: Float): Float = sqrt(this.squared() + other.squared())

operator fun Vec3d.plus(other: Vec3d): Vec3d = add(other)
operator fun Vec3d.minus(other: Vec3d): Vec3d = subtract(other)
operator fun Vec3d.times(n: Double): Vec3d = multiply(n)
operator fun Vec3d.div(n: Double): Vec3d = multiply(1 / n)

operator fun Vec3d.component1() = x
operator fun Vec3d.component2() = y
operator fun Vec3d.component3() = z
