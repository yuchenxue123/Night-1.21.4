package cute.neko.night.ui.widget.type

import cute.neko.night.Night
import cute.neko.night.features.module.ClientModule
import cute.neko.night.ui.widget.AbstractWidget
import cute.neko.night.ui.widget.WidgetType
import cute.neko.night.utils.animation.AnimationType
import cute.neko.night.utils.animation.SimpleAnimation
import cute.neko.night.utils.extensions.sum
import cute.neko.night.utils.nano.NanoUtils
import cute.neko.night.utils.nano.font.NanoFontManager
import cute.neko.night.utils.time.TimeTracker
import net.minecraft.client.gui.DrawContext
import java.awt.Color
import java.util.*
import java.util.concurrent.CopyOnWriteArrayList

/**
 * @author yuchenxue
 * @date 2025/06/12
 */

object DynamicIslandWidget : AbstractWidget(
    WidgetType.DYNAMIC_ISLAND,
    0f,
    0f,
    0f,
    0f
) {

    private val font = NanoFontManager.GENSHIN

    private val contexts = PriorityQueue<IslandContext>(
        compareByDescending { it.priority }
    )

    private val activeContext: IslandContext
        get() {
            if (contexts.isEmpty()) return StaticContext
            return contexts.peek()
        }

    private var dynamic_width = StaticContext.width
    private val animation_width = SimpleAnimation.create()
        .type(AnimationType.BACK_OUT)
        .target(StaticContext.width)
        .duration(600f)
        .finish()

    private var dynamic_height = StaticContext.height
    private val animation_height = SimpleAnimation.create()
        .type(AnimationType.BACK_OUT)
        .target(StaticContext.height)
        .duration(600f)
        .finish()

    override fun render(context: DrawContext) {
        update()

        dynamic_width = animation_width.animate()
        dynamic_height = animation_height.animate()

        val radius = StaticContext.height / 2f

        val centerX = mc.window.width / 2f
        val centerY = 40f

        val startX =  centerX - dynamic_width / 2f

        NanoUtils.drawIsland(
            startX,
            centerY,
            dynamic_width,
            dynamic_height,
            Color.BLACK,
            radius
        )

        NanoUtils.scissor(startX, centerY, dynamic_width, dynamic_height) {
            activeContext.render(context, startX, centerY)
        }
    }

    fun push(modules: List<ClientModule>) {
        NotificationContext.put(
            modules.map { "模块 ${it.showName} 被 ${if (it.state) "打开 !" else "关闭 !"}"},
            1500f
        )
        contexts.add(NotificationContext)
    }

    private fun update() {
        val width = activeContext.width
        if (width != animation_width.target) {
            when (activeContext) {
                NotificationContext -> {
                    if (width < dynamic_width)
                    {
                        if (animation_width.type != AnimationType.BACK_IN || animation_width.hasFinished()) {
                            animation_width
                                .type(AnimationType.BACK_IN)
                                .duration(400f)
                                .start(dynamic_width)
                                .target(width)
                                .reset()
                        }
                    }
                    else
                    {
                        animation_width
                            .type(AnimationType.BACK_OUT)
                            .duration(400f)
                            .start(dynamic_width)
                            .target(width)
                            .reset()
                    }
                }

                else -> {
                    if (width < dynamic_width)
                    {
                        animation_width
                            .type(AnimationType.BACK_IN)
                            .duration(400f)
                            .start(dynamic_width)
                            .target(width)
                            .reset()
                    }
                    else
                    {
                        animation_width
                            .type(AnimationType.BACK_OUT)
                            .duration(400f)
                            .start(dynamic_width)
                            .target(width)
                            .reset()
                    }
                }
            }
        }

        val height = activeContext.height
        if (height != animation_height.target) {
            when (activeContext) {
                NotificationContext -> {
                    if (height < dynamic_height)
                    {
                        if (animation_height.type != AnimationType.BACK_IN || animation_height.hasFinished()) {
                            animation_height
                                .type(AnimationType.BACK_IN)
                                .duration(400f)
                                .start(dynamic_height)
                                .target(height)
                                .reset()
                        }
                    }
                    else
                    {
                        animation_height
                            .type(AnimationType.BACK_OUT)
                            .duration(400f)
                            .start(dynamic_height)
                            .target(height)
                            .reset()
                    }
                }

                else -> {
                    if (height < dynamic_height)
                    {
                        animation_height
                            .type(AnimationType.BACK_IN)
                            .duration(400f)
                            .start(dynamic_height)
                            .target(height)
                            .reset()
                    }
                    else
                    {
                        animation_height
                            .type(AnimationType.BACK_OUT)
                            .duration(400f)
                            .start(dynamic_height)
                            .target(height)
                            .reset()
                    }
                }
            }
        }
    }

    fun pushBlockCounter() {
        if (contexts.contains(BlockCounterContext)) {
            return
        }
        contexts.add(BlockCounterContext)
    }

    fun popBlockCounter() {
        if (contexts.contains(BlockCounterContext)) {
            contexts.remove(BlockCounterContext)
        }
    }

    private const val UP_BOTTOM_SPACE = 16f
    private const val LEFT_RIGHT_SPACE = 8f

    data object StaticContext : IslandContext() {
        override val priority: Int
            get() = 0

        override val width: Float
            get() = font.width(text) + LEFT_RIGHT_SPACE * 2f

        override val height: Float
            get() = font.height(text) + UP_BOTTOM_SPACE * 2f

        private val text: String
            get() {
                if (mc.player == null || mc.world == null) return "ERROR"

                val split = " | "
                val builder = StringBuilder()
                builder.append(Night.CLIENT_NAME)
                    .append(split)
                    .append(player.name.string)
                    .append(split)
                    .append("${mc.currentFps}fps")

                return builder.toString()
            }

        override fun render(context: DrawContext, renderX: Float, renderY: Float) {
            font.render(
                context,
                text,
                renderX + LEFT_RIGHT_SPACE,
                renderY + UP_BOTTOM_SPACE,
                Color.WHITE
            )
        }
    }

    data object NotificationContext : IslandContext() {
        override val priority: Int
            get() = 50

        override val width: Float
            get() {
                if (notifications.isEmpty()) return StaticContext.width
                return notifications.maxOf { it.width } + LEFT_RIGHT_SPACE * 2f
            }

        override val height: Float
            get() {
                if (notifications.isEmpty()) return StaticContext.height
                return notifications.sum { it.height + VERTICAL_SPACE } - VERTICAL_SPACE + UP_BOTTOM_SPACE * 2f
            }

        private const val VERTICAL_SPACE = 8f

        private val notifications = CopyOnWriteArrayList<Notification>()

        override fun render(context: DrawContext, renderX: Float, renderY: Float) {
            if (notifications.isEmpty()) {
                contexts.remove(this)
                update()
                return
            }

            notifications.forEach {
                it.check()
                it.render(context, renderX, renderY)
            }
        }

        fun put(text: String, keepTime: Float) {
            notifications.add(Notification(listOf(NotificationInfo(text)), keepTime))
        }

        fun put(texts: List<String>, keepTime: Float) {
            notifications.add(Notification(texts.map { NotificationInfo(it) }, keepTime))
        }

        data class NotificationInfo(
            val text: String
        ) {
            val width = font.width(text)
            val height = font.height(text)
        }

        class Notification(
            private val infos: List<NotificationInfo>,
            private val keepTime: Float
        ) {

            companion object {
                private const val PADDING = 4f
            }

            val width: Float
                get() = infos.maxOf { it.width } + PADDING * 2f

            val height: Float
                get() = infos.sum { it.height + PADDING * 2f }

            private val tracker = TimeTracker()

            fun render(context: DrawContext, renderX: Float, renderY: Float) {
                val index = notifications.indexOf(this)

                if (index == -1) {
                    return
                }

                val offset = notifications.subList(fromIndex = 0, toIndex = index)
                    .sum { it.height + VERTICAL_SPACE }

                var interOffset = 0f
                infos.forEach { info ->
                    font.render(
                        context,
                        info.text,
                        renderX + LEFT_RIGHT_SPACE + PADDING,
                        renderY + UP_BOTTOM_SPACE + offset + interOffset + PADDING,
                        Color.WHITE
                    )

                    interOffset += info.height + PADDING * 2f
                }

//                font.render(
//                    context,
//                    text,
//                    renderX + LEFT_RIGHT_SPACE + PADDING,
//                    renderY + UP_BOTTOM_SPACE + offset + PADDING,
//                    Color.WHITE
//                )
            }

            fun check() {
                if (tracker.hasPassedTime(keepTime.toLong()) || activeContext != NotificationContext) {
                    notifications.remove(this)
                }
            }
        }
    }

    data object BlockCounterContext : IslandContext() {
        override val priority: Int
            get() = 100

        override val width: Float
            get() = 0f

        override val height: Float
            get() = 0f

        override fun render(context: DrawContext, renderX: Float, renderY: Float) {
        }
    }

    sealed class IslandContext {
        abstract val width: Float
        abstract val height: Float

        abstract val priority: Int

        abstract fun render(context: DrawContext, renderX: Float, renderY: Float)
    }
}