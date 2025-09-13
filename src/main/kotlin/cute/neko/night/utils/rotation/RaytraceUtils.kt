package cute.neko.night.utils.rotation

import cute.neko.night.utils.entity.rotation
import cute.neko.night.utils.extensions.squared
import cute.neko.night.utils.interfaces.Accessor
import cute.neko.night.utils.rotation.data.Rotation
import net.minecraft.block.BlockState
import net.minecraft.block.ShapeContext
import net.minecraft.entity.Entity
import net.minecraft.entity.projectile.ProjectileUtil
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.hit.EntityHitResult
import net.minecraft.util.hit.HitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import net.minecraft.world.RaycastContext
import kotlin.math.max

object RaytraceUtils : Accessor {

    fun raytraceEntity(
        range: Double,
        rotation: Rotation = RotationManager.currentRotation ?: player.rotation,
        filter: (Entity) -> Boolean,
    ): EntityHitResult? {
        val entity = mc.cameraEntity ?: return null

        val cameraVec = entity.eyePos
        val rotationVec = rotation.directionVector

        val vec3d3 = cameraVec.add(rotationVec.x * range, rotationVec.y * range, rotationVec.z * range)
        val box = entity.boundingBox.stretch(rotationVec.multiply(range)).expand(1.0, 1.0, 1.0)

        val hitResult =
            ProjectileUtil.raycast(
                entity,
                cameraVec,
                vec3d3,
                box,
                { !it.isSpectator && it.canHit() && filter.invoke(it) },
                range * range,
            )

        return hitResult
    }

    fun raytraceBlock(
        range: Double,
        rotation: Rotation = RotationManager.currentRotation ?: player.rotation,
        pos: BlockPos,
        state: BlockState,
    ): BlockHitResult? {
        val entity: Entity = mc.cameraEntity ?: return null

        val start = entity.eyePos
        val rotationVec = rotation.directionVector

        val end = start.add(rotationVec.x * range, rotationVec.y * range, rotationVec.z * range)

        return mc.world?.raycastBlock(
            start,
            end,
            pos,
            state.getOutlineShape(mc.world, pos, ShapeContext.of(mc.player)),
            state,
        )
    }

    fun raytrace(
        rotation: Rotation = RotationManager.currentRotation ?: player.rotation,
        range: Double = max(player.blockInteractionRange, player.entityInteractionRange),
        includeFluids: Boolean = false,
        tickDelta: Float = 1f,
    ): BlockHitResult {
        return raytrace(
            range = range,
            includeFluids = includeFluids,
            start = player.getCameraPosVec(tickDelta),
            direction = rotation.directionVector,
        )
    }

    fun raytrace(
        range: Double = max(player.blockInteractionRange, player.entityInteractionRange),
        includeFluids: Boolean = false,
        start: Vec3d,
        direction: Vec3d,
        entity: Entity = mc.cameraEntity!!,
    ): BlockHitResult {
        val end = start.add(direction.x * range, direction.y * range, direction.z * range)

        return world.raycast(
            RaycastContext(
                start,
                end,
                RaycastContext.ShapeType.OUTLINE,
                if (includeFluids) RaycastContext.FluidHandling.ANY else RaycastContext.FluidHandling.NONE,
                entity,
            ),
        )
    }

    fun faceEntity(
        fromEntity: Entity = mc.cameraEntity!!,
        toEntity: Entity,
        rotation: Rotation,
        range: Double,
        wallsRange: Double,
    ): Boolean {
        val cameraVec = fromEntity.eyePos
        val rotationVec = rotation.directionVector

        val rangeSquared = range.squared()
        val wallsRangeSquared = wallsRange.squared()

        val vec3d3 = cameraVec.add(rotationVec.x * range, rotationVec.y * range, rotationVec.z * range)
        val box = fromEntity.boundingBox.stretch(rotationVec.multiply(range)).expand(1.0, 1.0, 1.0)

        val entityHitResult =
            ProjectileUtil.raycast(
                fromEntity, cameraVec, vec3d3, box, { !it.isSpectator && it.canHit() && it == toEntity }, rangeSquared,
            ) ?: return false

        val distance = cameraVec.squaredDistanceTo(entityHitResult.pos)

        return distance <= rangeSquared && canSeePoint(cameraVec, entityHitResult.pos) || distance <= wallsRangeSquared
    }

    fun canSeePoint(
        eyes: Vec3d,
        vec3: Vec3d,
    ) = mc.world?.raycast(
        RaycastContext(
            eyes, vec3, RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, mc.player,
        ),
    )?.type == HitResult.Type.MISS

}