package cute.neko.night.features.module.player

import cute.neko.night.event.handler
import cute.neko.night.event.events.game.player.PlayerTickEvent
import cute.neko.night.features.module.ClientModule
import cute.neko.night.features.module.ModuleCategory
import cute.neko.night.features.module.movement.speed.ModuleSpeed
import cute.neko.night.features.setting.type.mode.SubMode
import cute.neko.night.utils.entity.direction
import cute.neko.night.utils.extensions.*
import cute.neko.night.utils.kotlin.Priority
import cute.neko.night.utils.movement.MoveDirection
import cute.neko.night.utils.player.inventory.Slots
import cute.neko.night.utils.rotation.RaytraceUtils
import cute.neko.night.utils.rotation.RotationManager
import cute.neko.night.utils.rotation.RotationUtils
import cute.neko.night.utils.rotation.data.Rotation
import cute.neko.night.utils.rotation.data.RotationRequest
import cute.neko.night.utils.rotation.features.MovementCorrection
import cute.neko.night.utils.rotation.util.BlockSearchDirections
import net.minecraft.block.BlockState
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.math.Vec3d
import org.lwjgl.glfw.GLFW

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

    private val speedKeep by boolean("SpeedKeep", true)

    private var data: PlaceData? = null

    override fun disable() {
        Slots.reset()

        ground = false

        RotationManager.remove(this)
    }

    private var ground = false

    @Suppress("unused")
    private val onPlayerTick = handler<PlayerTickEvent> {
        findBlock()

        updateRotation()

        place()
    }

    private fun updateRotation() {
        data?.let {

            RotationManager.request(
                RotationRequest(
                    this,
                    it.rotation,
                    Priority.ROTATION_IMPORTANT,
                    horizontalSpeed,
                    verticalSpeed,
                    correction
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
                        findPitch(pos, yaw) ?: 85f
                    }
                }

                if (!player.groundCollision) {
                    pitch += 5f
                }

                return Rotation(yaw, pitch)
            }

            RotationMode.TWIST -> {
                // skid
                val direct = player.direction + 180f

                val yaw = direct % 360f

                val north = yaw < 80f || yaw > 280f
                val south = yaw > 100f && yaw < 260f
                val east = yaw > 10f && yaw < 170f
                val west = yaw > 190f && yaw < 350f

                val yawF = if (player.blockPos.down().getState()?.isAir == true)
                {
                    direct + 70f
                }
                else if (MoveDirection.parse(direct, 6f).diagonal)
                {
                    when {
                        north && west -> 45f
                        north && east -> 135f
                        south && east -> 225f
                        south && west -> 315f
                        else -> (direct + 60f) % 360
                    }
                }
                else
                {
                    (direct + 60f) % 360
                }

                val pitchF = if (player.isOnGround) 78f else 88f

                return Rotation(yawF, pitchF)
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

    private fun findPitch(pos: BlockPos, yaw: Float): Float? {
        for (i in generateSequence(-90f) { it + 1f }.takeWhile { it in -90f..90f }) {
            val blockPos = RaytraceUtils.raytrace(rotation = Rotation(yaw, i)).blockPos
            if (blockPos == pos) {
                return i
            }
        }

        return null
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

        val directions = when {
            speedKeep && ModuleSpeed.running -> BlockSearchDirections.HORIZONTAL.directions
            else -> BlockSearchDirections.NORMAL.directions
        }

        directions.forEach direction@ { direction ->
            val offset = pos.offset(direction.opposite)

            val blockState = world.getBlockState(offset)
            val condition = blockState.isSolidBlock(world, offset) || !blockState.isReplaceable

            if (!condition) {
                return@direction
            }

            val vec = offset.toCenterPos().add(direction.opposite.doubleVector * 0.5)

            val rotation = getRotation(offset, direction.opposite)

            data = PlaceData(offset, direction, vec, rotation)

            return true
        }

        return false
    }

    data class PlaceData(
        val pos: BlockPos,
        val direction: Direction,
        val vec: Vec3d,
        val rotation: Rotation
    )
}