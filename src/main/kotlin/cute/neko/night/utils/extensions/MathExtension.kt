package cute.neko.night.utils.extensions

import net.minecraft.util.math.Vec3d
import org.joml.Vector3d
import org.joml.Vector3f
import org.joml.Vector4f
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.round
import kotlin.math.sqrt

fun Double.decimals(n: Int): Double {
    val bigDecimal = BigDecimal(this)
    val rounded = bigDecimal.setScale(n, RoundingMode.HALF_UP)
    return rounded.toDouble()
}

fun Double.toRadians(): Double = this * 0.017453292519943295
fun Double.toDegrees(): Double = this * 57.29577951308232
fun Double.step(step: Double): Double = round(this / step) * step
fun Double.squared(): Double = this * this

fun Float.toRadians() = this * 0.017453292f
fun Float.toDegrees() = this * 57.29578f
fun Float.decimals(n: Int): Float = this.toDouble().decimals(n).toFloat()
fun Float.step(step: Float): Float = round(this / step) * step
fun Float.squared(): Float = this * this

operator fun Vec3d.plus(other: Vec3d): Vec3d = add(other)
operator fun Vec3d.minus(other: Vec3d): Vec3d = subtract(other)
operator fun Vec3d.times(n: Double): Vec3d = multiply(n)
operator fun Vec3d.div(n: Double): Vec3d = multiply(1 / n)

fun Vector3d.toVec3d() = Vec3d(x, y, z)

operator fun Vec3d.component1() = x
operator fun Vec3d.component2() = y
operator fun Vec3d.component3() = z

operator fun Vector4f.component1() = x
operator fun Vector4f.component2() = y
operator fun Vector4f.component3() = z
operator fun Vector4f.component4() = w
