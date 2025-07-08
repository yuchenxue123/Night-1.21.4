package cute.neko.night.utils.render

import com.mojang.blaze3d.systems.RenderSystem
import cute.neko.night.event.EventListener
import cute.neko.night.event.events.game.render.WorldRenderEvent
import cute.neko.night.event.handle
import cute.neko.night.utils.extensions.minus
import cute.neko.night.utils.interfaces.Accessor
import cute.neko.night.utils.kotlin.Priority
import net.minecraft.entity.Entity
import net.minecraft.util.math.Box
import net.minecraft.util.math.Vec3d
import org.joml.*

/**
 * @author yuchenxue
 * @date 2025/07/07
 */

// skid LiquidBounce
object WorldToScreen : EventListener, Accessor {

    private val mvMatrix = Matrix4f()
    private val projectionMatrix = Matrix4f()

    private val cacheMatrix = Matrix4f()
    private val cacheVec3f = Vector3f()

    private val onWorldRender = handle<WorldRenderEvent>(priority =  Priority.FINAL) { event ->
        val matrixStack = event.matrixStack

        this.mvMatrix.set(matrixStack.peek().positionMatrix)
        this.projectionMatrix.set(RenderSystem.getProjectionMatrix())
    }

    fun calculate(
        pos: Vec3d,
        callback: (x: Float, y: Float) -> Unit = { _, _ -> }
    ): Vector3f? {
        val relativePos = pos - mc.gameRenderer.camera.pos

        val transformedPos = cacheVec3f.set(relativePos)
            .mulProject(cacheMatrix.set(projectionMatrix).mul(mvMatrix))

        val screenPos = transformedPos
            .mul(1f, -1f, 1f)
            .add(1f, 1f, 0f)
            .mul(0.5f, 0.5f, 1f)
            .mul(mc.window.width.toFloat(), mc.window.height.toFloat(), 1f)

        if (transformedPos.z < 1f) {
            callback.invoke(screenPos.x, screenPos.y)
        }

        return if (transformedPos.z < 1f) Vector3f(screenPos.x, screenPos.y, transformedPos.z) else null
    }

    fun calculate(
        box: Box,
        callback: (x: Float, y: Float, width: Float, height: Float) -> Unit = { _, _, _, _ -> }
    ): ScreenRect? {
        val corners = arrayOf(
            Vec3d(box.minX, box.minY, box.minZ),
            Vec3d(box.minX, box.minY, box.maxZ),
            Vec3d(box.minX, box.maxY, box.minZ),
            Vec3d(box.minX, box.maxY, box.maxZ),
            Vec3d(box.maxX, box.minY, box.minZ),
            Vec3d(box.maxX, box.minY, box.maxZ),
            Vec3d(box.maxX, box.maxY, box.minZ),
            Vec3d(box.maxX, box.maxY, box.maxZ)
        )

        val screenPoints = corners.mapNotNull {
            calculate(it)?.let { pos -> Vector2f(pos.x, pos.y) }
        }

        if (screenPoints.isEmpty()) {
            return null
        }

        val minX = screenPoints.minOf { it.x }
        val minY = screenPoints.minOf { it.y }
        val maxX = screenPoints.maxOf { it.x }
        val maxY = screenPoints.maxOf { it.y }

        val width = maxX - minX
        val height = maxY - minY

        if (width < 1f || height < 1f) {
            return null
        }

        callback.invoke(minX, minY, width, height)

        return ScreenRect(minX, minY, width, height)
    }

    fun calculate(
        entity: Entity,
        callback: (x: Float, y: Float, width: Float, height: Float) -> Unit = { _, _, _, _ -> }
    ): ScreenRect? = calculate(entity.boundingBox, callback)

    data class ScreenRect(val x: Float, val y: Float, val width: Float, val height: Float)

    private fun Vector3f.set(vec3d: Vec3d) = set(vec3d.x, vec3d.y, vec3d.z)
}