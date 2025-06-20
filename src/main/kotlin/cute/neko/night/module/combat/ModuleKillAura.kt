package cute.neko.night.module.combat

import cute.neko.night.event.events.game.player.PlayerTickEvent
import cute.neko.night.event.handle
import cute.neko.night.module.ClientModule
import cute.neko.night.module.ModuleCategory
import cute.neko.night.setting.config.types.TargetOption
import cute.neko.night.setting.type.mode.SubMode
import cute.neko.night.utils.client.Priority
import cute.neko.night.utils.entity.box
import cute.neko.night.utils.entity.rotation
import cute.neko.night.utils.misc.RandomUtils
import cute.neko.night.utils.rotation.RaytraceUtils
import cute.neko.night.utils.rotation.RotationManager
import cute.neko.night.utils.rotation.RotationUtils
import cute.neko.night.utils.rotation.api.RotationPriority
import cute.neko.night.utils.rotation.data.RotationTarget
import cute.neko.night.utils.rotation.features.MovementCorrection
import net.minecraft.entity.LivingEntity
import net.minecraft.util.Hand
import org.lwjgl.glfw.GLFW

/**
 * @author yuchenxue
 * @date 2025/05/10
 */

@RotationPriority(Priority.ROTATION_NORMAL)
object ModuleKillAura : ClientModule(
    "KillAura",
    ModuleCategory.COMBAT,
    key = GLFW.GLFW_KEY_R
) {

    private val clickMode by mode("ClickMode", ClickMode.RANDOM)

    enum class ClickMode(override val modeName: String) : SubMode {
        COOLDOWN("Cooldown"),
        STABILIZED("Stabilized"),
        RANDOM("Random")
    }

    private val cps by int("CPS", 12, 1..20) {
        clickMode != ClickMode.COOLDOWN
    }

    private val randomRange by int("RandomRange", 3, 1..5) {
        clickMode == ClickMode.RANDOM
    }

    private val swingMode by mode("SwingMode", SwingMode.POST)

    enum class SwingMode(override val modeName: String) : SubMode {
        PRE("Pre"),
        POST("Post"),
        NONE("None")
    }

    private val range by float("Range", 3f, 1f..7f, 0.1f)
    private val scanRange by float("ScanExpandRange", 0.5f, 0.1f..2f, 0.1f)
    private val wallRange by float("WallRange", 1f, 0f..3f, 0.1f)

    private val fov by float("FOV", 180f, 1f..180f, 1f)

    private val horizontalSpeed by float("HorizontalSpeed", 180f, 1f..180f, 1f)
    private val verticalSpeed by float("VerticalSpeed", 180f, 1f..180f, 1f)

    private val correction by mode("MovementCorrection", MovementCorrection.NONE)

    private val raytrace by boolean("Raytrace", false)

    private val targets = tree(TargetOption())

    private var target: LivingEntity? = null

    override fun disable() {
        RotationManager.reset(this)
    }

    @Suppress("unused")
    private val onPlayerTick = handle<PlayerTickEvent> {
        update()

        target?.let { target ->
            val entity = when {
                raytrace -> {
                    val result = RaytraceUtils.raytraceEntity(
                        range.toDouble(),
                    ) { it is LivingEntity && isTargetEntity(it) }

                    result?.entity ?: target
                }

                else -> target
            }

            // I consider this entity is LivingEntity
            attack(entity as LivingEntity)
        }
    }

    private fun update() {
        target?.let {
            if (!isTargetEntity(it)) {
                target = null
            }
        }

        val targets = probablyTargets

        if (target == null && targets.isNotEmpty()) {
            target = targets[0]
        }

        target?.let {
            val rotation = RotationUtils.toRotation(it.box.center).normalize()

            RotationManager.applyRotation(
                RotationTarget(
                    this,
                    rotation,
                    correction,
                    horizontalSpeed,
                    verticalSpeed
                )
            )
        } ?: run {
            RotationManager.reset(this)
        }
    }

    private fun attack(entity: LivingEntity) {

        // check is facing entity
        if (!RaytraceUtils.faceEntity(
                toEntity = entity,
                rotation = RotationManager.currentRotation ?: player.rotation,
                range = range.toDouble(),
                wallsRange = wallRange.toDouble(),
            )
        ) {
            return
        }

        if (!shouldAttack()) {
            return
        }

        when (swingMode) {
            SwingMode.PRE -> {
                player.swingHand(Hand.MAIN_HAND)
                interactionManager.attackEntity(player, entity)
            }

            SwingMode.POST -> {
                interactionManager.attackEntity(player, entity)
                player.swingHand(Hand.MAIN_HAND)
            }

            SwingMode.NONE -> {
                interactionManager.attackEntity(player, entity)
            }
        }
    }


    private var lastClickTime = 0L

    private fun shouldAttack(): Boolean {
        val player = mc.player ?: return false

        return when (clickMode) {
            ClickMode.COOLDOWN -> {
                player.getAttackCooldownProgress(0f) >= 1f
            }

            ClickMode.STABILIZED -> {
                val delay = 1000L / cps

                if (System.currentTimeMillis() - lastClickTime > delay) {
                    lastClickTime = System.currentTimeMillis()
                    return true
                }

                false
            }

            ClickMode.RANDOM -> {
                val random = RandomUtils.int(cps, randomRange).coerceIn(1, 20)
                val delay = 1000L / random

                if (System.currentTimeMillis() - lastClickTime > delay) {
                    lastClickTime = System.currentTimeMillis()
                    return true
                }

                false
            }
        }
    }

    private val probablyTargets: List<LivingEntity>
        get() {
            val player = mc.player ?: return emptyList()
            val world = mc.world ?: return emptyList()

            return world.entities.filterIsInstance<LivingEntity>()
                .filter { isTargetEntity(it) }
                .sortedBy { player.distanceTo(it) }
        }

    private fun isTargetEntity(entity: LivingEntity): Boolean {
        val player = mc.player ?: return false

        return targets.isTarget(entity)
                && player.distanceTo(entity) <= range + scanRange
                && RotationUtils.rotationDifference(entity) <= fov
                && entity.isAlive
    }
}