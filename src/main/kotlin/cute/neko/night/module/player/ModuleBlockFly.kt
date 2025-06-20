package cute.neko.night.module.player

import cute.neko.night.event.events.game.player.PlayerTickEvent
import cute.neko.night.event.handle
import cute.neko.night.module.ClientModule
import cute.neko.night.module.ModuleCategory
import cute.neko.night.setting.type.mode.SubMode
import cute.neko.night.utils.client.Priority
import cute.neko.night.utils.entity.direction
import cute.neko.night.utils.extensions.sequence
import cute.neko.night.utils.extensions.squared
import cute.neko.night.utils.extensions.step
import cute.neko.night.utils.extensions.times
import cute.neko.night.utils.movement.MoveDirection
import cute.neko.night.utils.player.inventory.Slots
import cute.neko.night.utils.rotation.RaytraceUtils
import cute.neko.night.utils.rotation.RotationManager
import cute.neko.night.utils.rotation.RotationUtils
import cute.neko.night.utils.rotation.api.RotationPriority
import cute.neko.night.utils.rotation.data.Rotation
import cute.neko.night.utils.rotation.data.RotationTarget
import cute.neko.night.utils.rotation.features.MovementCorrection
import cute.neko.night.utils.rotation.util.BlockSearchDirections
import net.minecraft.block.BlockState
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Vec3d
import org.lwjgl.glfw.GLFW
import kotlin.math.abs

/**
 * @author yuchenxue
 * @date 2025/05/11
 */

@RotationPriority(Priority.ROTATION_HIGH)
object ModuleBlockFly : ClientModule(
    "BlockFly",
    ModuleCategory.PLAYER,
    key = GLFW.GLFW_KEY_F,
) {

    private val rotations by mode("Rotation", RotationMode.NORMAL)

    enum class RotationMode(override val modeName: String) : SubMode {
        NORMAL("Normal"),
        REVERSE("Reverse"),
        TWIST("Twist"),
        STEP("Step")
    }

    private val stepMode by mode("StepType", RotationStepMode.FORTY_FIVE) {
        rotations == RotationMode.STEP
    }

    enum class RotationStepMode(override val modeName: String, val step: Float) : SubMode {
        FORTY_FIVE("FortyFive", 45f),
        THIRTY("Thirty", 30f),
        FIFTEEN("Fifteen", 15f)
    }

    private val horizontalSpeed by float("HorizontalSpeed", 180f, 1f..180f, 1f)
    private val verticalSpeed by float("VerticalSpeed", 180f, 1f..180f, 1f)

    private val correction by mode("MovementCorrection", MovementCorrection.NONE)

    private val raytrace by boolean("Raytrace", false)

    private var data: PlaceData? = null

    override fun enable() {
    }

    override fun disable() {
        Slots.reset()

        RotationManager.reset(this)
    }

    @Suppress("unused")
    private val onPlayerTick = handle<PlayerTickEvent> { it: PlayerTickEvent ->
        val world = mc.world ?: return@handle

        findBlock()

        updateRotation()

        place()
    }

    private fun updateRotation() {
        data?.let {

            RotationManager.applyRotation(
                RotationTarget(
                    this,
                    it.rotation,
                    correction,
                    horizontalSpeed,
                    verticalSpeed,
                )
            )
        }
    }

    private fun place() {

        if (!Slots.isBlock()) {
            Slots.select(Slots.findBlockSlot())
        }

        data?.let {
            if (player.eyePos.squaredDistanceTo(it.vec) >= player.blockInteractionRange.squared()) {
                data = null
                return
            }

            val raytrace = RaytraceUtils.raytrace()

            if (raytrace.blockPos != it.pos && ModuleBlockFly.raytrace) {
                return
            }

            if (interactionManager.interactBlock(
                    player,
                    Hand.MAIN_HAND,
                    BlockHitResult(it.vec, it.direction, it.pos, false),
                ).isAccepted) {
                player.swingHand(Hand.MAIN_HAND)
            }
        }
    }

    private fun getRotation(pos: BlockPos, direction: Direction): Rotation {
//        val append = player.velocity.normalize().multiply(0.75)
//        val eyePos = player.pos.add(append).add(.0, player.standingEyeHeight.toDouble(), .0)

        when (rotations) {
            RotationMode.NORMAL -> {
                val hitVec = getHitVec(pos, direction)

                if (direction.opposite == Direction.UP) {
                    return RotationUtils.toRotation(hitVec.add(.0, 0.8, .0)).normalize()
                }

                return RotationUtils.toRotation(hitVec).normalize()
            }

            RotationMode.REVERSE -> {
                val yaw = (player.direction + 180f) % 360f

                var pitch = when {
                    direction.opposite == Direction.UP -> 79f
                    else -> {
                        var default = 75f

                        for (i in generateSequence(-90f) { it + 0.5f }.takeWhile { it in -90f..90f }) {
                            val blockPos = RaytraceUtils.raytrace(rotation = Rotation(yaw, i)).blockPos
                            if (blockPos == pos) {
                                default = i
                                break
                            }
                        }

                        default
                    }
                }

                if (!player.groundCollision) {
                    pitch += 5f
                }

                return Rotation(yaw, pitch)
            }

            RotationMode.TWIST -> {
                val rotation = RotationUtils.toRotation(pos.toCenterPos()).normalize()
                val direct = player.direction
                val moveDirection = MoveDirection.parse(direct, 30f)
                val yaw = if (!moveDirection.diagonal) {
                    if (MathHelper.abs(MathHelper.wrapDegrees(rotation.yaw - direct - 118f))
                        < MathHelper.abs(MathHelper.wrapDegrees(rotation.yaw - direct + 118f))
                        ) {
                        direct + 118f
                    } else {
                        direct - 118f
                    }
                } else {
                    direct + 132
                }

                return Rotation(yaw, rotation.pitch)
            }

            RotationMode.STEP -> {
                val hitVec = getHitVec(pos, direction)

                val rotation = RotationUtils.toRotation(hitVec)

                if (rotations == RotationMode.STEP) {
                    rotation.yaw = rotation.yaw.step(stepMode.step)
                }

                return rotation.normalize()
            }
        }
    }


    /**
     * Get hit vec
     * Notice: hit face not the place face
     *
     * @param pos block pos
     * @param direction direction of aim face
     */
    private fun getHitVec(pos: BlockPos, direction: Direction): Vec3d {
        return pos.toCenterPos().add(direction.doubleVector * 0.5)
    }

    private fun findBlock() {
        val player = mc.player ?: return
        val world = mc.world ?: return

        val position = player.blockPos.down()

        val (horizontal, vertical) = 5 to 3

        position.sequence(
            -horizontal..horizontal,
            -vertical..0,
            -horizontal..horizontal
        ).map {
            it to world.getBlockState(it)
        }.sortedBy { (pos, _) ->
            player.squaredDistanceTo(pos.toCenterPos())
        }.forEach { (pos, state) ->
            if (state.isSolidBlock(world, pos) || search(pos, state)) {
                return
            }
        }
    }

    private fun search(
        pos: BlockPos,
        state: BlockState
    ): Boolean {
        val world = mc.world ?: return false

        if (!state.isReplaceable) {
            return false
        }

        val directions = BlockSearchDirections.NORMAL.directions

        directions.forEach direction@ { direction ->
            val offset = pos.offset(direction.opposite)

            val blockState = world.getBlockState(offset)
            val condition = blockState.isSolidBlock(world, offset) || !blockState.isReplaceable

            if (!condition) {
                return@direction
            }

            val vec = offset.toCenterPos().add(direction.doubleVector * 0.5)

            val rotation = getRotation(offset, direction.opposite)

            data = PlaceData(offset, direction, vec, rotation)

            return true
        }

        return false
    }

    private fun isDiagonal(threshold: Float): Boolean {
        val yaw = player.direction
        val normalizedYaw = abs((yaw + 360) % 360)

        val north = abs(normalizedYaw) < threshold || abs(normalizedYaw - 360) < threshold
        val south = abs(normalizedYaw - 180) < threshold
        val east = abs(normalizedYaw - 90) < threshold
        val west = abs(normalizedYaw - 270) < threshold

        return !north && !south && !east && !west
    }

    data class PlaceData(
        val pos: BlockPos,
        val direction: Direction,
        val vec: Vec3d,
        val rotation: Rotation
    )
}