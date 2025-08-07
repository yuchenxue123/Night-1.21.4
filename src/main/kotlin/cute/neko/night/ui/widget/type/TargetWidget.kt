package cute.neko.night.ui.widget.type

import cute.neko.night.features.module.combat.killaura.features.KillAuraTargetTracker
import cute.neko.night.ui.widget.DraggableWidget
import cute.neko.night.ui.widget.WidgetType
import cute.neko.night.utils.animation.AnimationType
import cute.neko.night.utils.animation.SimpleAnimation
import cute.neko.night.utils.misc.option.FloatOption
import cute.neko.night.utils.render.nano.NanoFontManager
import cute.neko.night.utils.render.nano.NanoUtils
import cute.neko.night.utils.render.nano.image.NanoImageManager
import cute.neko.night.utils.render.utils.TextureScissor
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.network.AbstractClientPlayerEntity
import net.minecraft.entity.LivingEntity
import net.minecraft.text.Text
import java.awt.Color
import java.util.*

/**
 * @author yuchenxue
 * @date 2025/06/03
 */

// Test
object TargetWidget : DraggableWidget(
    WidgetType.TARGET,
    200f,
    200f,
    100f,
    60f
) {
    private val font = NanoFontManager.GENSHIN

    // Target
    private var target: LivingEntity? = null

    private const val SIDE_WIDTH = 4f
    private const val PADDING = 8f
    private const val SKIN_SIZE = 64f

    private val _height: Float = SKIN_SIZE + PADDING * 2
    private val _width: Float
        get() {
            if (target !is AbstractClientPlayerEntity && target != null) {
                return SIDE_WIDTH + PADDING + maxOf(font.width(target!!.name!!), 100f) + PADDING
            }

            return SIDE_WIDTH + PADDING + SKIN_SIZE + PADDING + maxOf(font.width(target?.name ?: Text.of("NoName")), 100f) + PADDING
        }

    // Animation
    private val width_ = FloatOption(0f)
    private val height_ = FloatOption(0f)

    private val width_animation = SimpleAnimation.create()
        .type(AnimationType.QUAD_OUT)
        .start(width_.default)
        .target(_width)
        .duration(200f)

    private val height_animation = SimpleAnimation.create()
        .type(AnimationType.QUAD_OUT)
        .start(height_.default)
        .target(_height)
        .duration(200f)

    private val map = mutableMapOf<UUID, Int>()

    override fun render(context: DrawContext) {
        update()

        val (width, height) = width_.animate(width_animation).get() to height_.animate(height_animation).get()

        NanoUtils.scissor(x, y, width, height) {

            NanoUtils.drawRect(x, y, SIDE_WIDTH, _height, Color.WHITE)

            NanoUtils.drawRect(x + SIDE_WIDTH, y, _width - SIDE_WIDTH, _height, Color(40, 40, 40, 120))

            (target as? AbstractClientPlayerEntity)?.let { target ->
                target.skinTextures?.texture()?.let { texture ->

                    val head = if (map.containsKey(target.uuid)) {
                        map[target.uuid]!!
                    } else {
                        val create = TextureScissor.scissor(
                            mc.textureManager.getTexture(texture).glId,
                            8, 8,
                            8,8
                        )

                        map[target.uuid] = create

                        create
                    }

                    NanoUtils.drawImage(
                        NanoImageManager.getOrCreate(head, 8, 8),
                        x + SIDE_WIDTH + PADDING,
                        y + PADDING,
                        SKIN_SIZE,
                        SKIN_SIZE
                    )

                }
            }

            if (target is AbstractClientPlayerEntity) {
                font.drawText(
                    target!!.name,
                    x + SIDE_WIDTH + PADDING +SKIN_SIZE + PADDING,
                    y + PADDING,
                    Color.WHITE
                )
            } else if (target != null) {
                font.drawText(
                    target!!.name,
                    x + SIDE_WIDTH + PADDING,
                    y + PADDING,
                    Color.WHITE
                )
            }
        }

    }

    private fun update() {
        val newTarget = KillAuraTargetTracker.target

        if (newTarget == target) {
            return
        }

        if (newTarget != null && target != null) {
            target = newTarget
            return
        }

        when (newTarget) {
            null -> {
                width_animation
                    .type(AnimationType.QUAD_IN)
                    .start(width_.get())
                    .target(0f)
                    .reset()

                height_animation
                    .type(AnimationType.QUAD_IN)
                    .start(height_.get())
                    .target(0f)
                    .reset()
            }

            else -> {
                width_animation
                    .type(AnimationType.QUAD_OUT)
                    .start(width_.get())
                    .target(_width)
                    .reset()

                height_animation
                    .type(AnimationType.QUAD_OUT)
                    .start(height_.get())
                    .target(_height)
                    .reset()
            }
        }

        target = newTarget
    }

    private fun getPlayerData(): PlayerData? {
        val entity = target ?: return null

        if (entity is AbstractClientPlayerEntity) {
            val health = (entity.health / entity.maxHealth).coerceIn(0f, 1f)

            return PlayerData(entity.name ?: Text.of("NoName"), health)
        }

        return null
    }

    data class PlayerData(
        val name: Text,
        val heathProcess: Float,
//        val protectionProcess: Float
    )
}