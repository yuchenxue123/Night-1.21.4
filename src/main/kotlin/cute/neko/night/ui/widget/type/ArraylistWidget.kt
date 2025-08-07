package cute.neko.night.ui.widget.type

import cute.neko.night.features.module.ClientModule
import cute.neko.night.features.module.ModuleManager
import cute.neko.night.features.module.render.ModuleInterface
import cute.neko.night.ui.interfaces.Drawable
import cute.neko.night.ui.widget.LockedWidget
import cute.neko.night.ui.widget.WidgetType
import cute.neko.night.utils.animation.AnimationType
import cute.neko.night.utils.animation.SimpleAnimation
import cute.neko.night.utils.render.nano.NanoUtils
import cute.neko.night.utils.render.nano.NanoFontManager
import net.minecraft.client.gui.DrawContext
import net.minecraft.text.Text
import java.awt.Color

/**
 * @author yuchenxue
 * @date 2025/06/01
 */

object ArraylistWidget : LockedWidget(WidgetType.ARRAYLIST) {

    private const val X_POSITION = 0f
    private const val Y_POSITION = 40f

    private val drawables = mutableListOf<TextDrawable>()

    init {
        build()
    }

    override fun render(context: DrawContext) {
        // update position
        refresh()

        // draw each text
        drawables.forEach {
            it.render(context)
        }
    }

    private fun refresh() {
        var offset = 0f

        drawables.forEach {
            if (it.hidden) return@forEach

            it.offset(offset)
            offset += it.height
        }

        sort()
    }

    private fun sort() {
        drawables.sortWith(compareBy { -it.width })
    }

    private fun build() {
        drawables.clear()

        var currentY = Y_POSITION

        ModuleManager.modules.forEach {
            val drawable = TextDrawable(it)
            drawables.add(drawable)
            currentY += drawable.height
        }
    }

    class TextDrawable(
        val module: ClientModule
    ) : Drawable {
        val font = NanoFontManager.GENSHIN

        companion object {
            private const val SIDELINE_WIDTH = 2f
            private const val SPACE_LEFT_RIGHT = 4f
            private const val SPACE_NAME_SUFFIX = 6f
            private const val SPACE_TOP_BOTTOM = 4f
        }

        // get module name
        private val name: String
            get() = module.showName

        // get module suffix
        private val suffix: String
            get() = module.suffix

        // get width
        val width: Float
            get() = when {
                ModuleInterface.arraySuffix && suffix.isNotEmpty() -> {
                    font.width(name) + SPACE_NAME_SUFFIX + font.width(Text.of(suffix)) + SPACE_LEFT_RIGHT * 2
                }

                else -> font.width(name) + SPACE_LEFT_RIGHT * 2
            }

        // get height
        val height: Float
            get() = when {
                ModuleInterface.arraySuffix && suffix.isNotEmpty() -> {
                    maxOf(font.height(name), font.height(suffix)) + SPACE_TOP_BOTTOM * 2
                }
                else -> font.height(name) + SPACE_TOP_BOTTOM * 2
            }

        // x position when in hide
        private val hideX: Float
            get() = -width - 4f

        // x position when show
        private val showX: Float
            get() = X_POSITION

        // hide and show distance, it is below zero
        private val distance: Float
            get() = hideX - showX

        // x offset
        private var offsetX = 0f
        // y offset
        private var offsetY = 0f

        // animation
        private val animationOffsetX = SimpleAnimation.create().type(AnimationType.QUAD_OUT)
        private val animationOffsetY = SimpleAnimation.create().type(AnimationType.QUAD_OUT)

        // state
        private var state = false

        // true render position
        private val renderX: Float get() = X_POSITION + offsetX
        private val renderY: Float get() = Y_POSITION + offsetY

        override fun render(context: DrawContext) {
            // refresh module state
            refresh()

            // smart
            if (offsetX <= distance) {
                animationOffsetY.finish()
            }

            // animation
            offsetX = animationOffsetX.animate()
            offsetY = animationOffsetY.animate()

            // side offset
            var side = 0f

            // sideline
            if (ModuleInterface.sideline) {
                NanoUtils.drawRect(
                    renderX , renderY,
                    SIDELINE_WIDTH, height,
                    Color(255, 255, 255, 255)
                )

                // add side width
                side += SIDELINE_WIDTH
            }

            // background
            NanoUtils.drawRect(
                renderX + side, renderY,
                width, height,
                Color(0, 0, 0, 80)
            )

            // add left space
            side += SPACE_LEFT_RIGHT

            // name
            font.drawText(
                name,
                renderX + side,
                renderY + SPACE_TOP_BOTTOM,
                Color(255, 255, 255, 255)
            )

            // add name width and space of name and suffix
            side += font.width(name) + SPACE_NAME_SUFFIX

            // suffix
            if (ModuleInterface.arraySuffix) {
                font.drawText(
                    suffix,
                    renderX + side,
                    renderY + SPACE_TOP_BOTTOM,
                    Color(200, 200, 200, 255)
                )
            }
        }

        // should draw this
        val hidden: Boolean
            get() = !module.state && offsetX <= distance

        private fun refresh() {
            val newState = module.state

            if (newState == state) {
                when {
                    state -> animationOffsetX.target(0f)
                    else -> animationOffsetX.target(distance)
                }
                return
            }

            when {
                newState && animationOffsetX.target != 0f -> {
                    animationOffsetX.type(AnimationType.QUAD_OUT).start(offsetX).target(0f).reset()
                }

                !newState && animationOffsetX.target != distance -> {
                    animationOffsetX.type(AnimationType.QUAD_IN).start(offsetX).target(distance).reset()
                }
            }

            this.state = newState
        }

        fun offset(offset: Float) = apply {
            if (offset == animationOffsetY.target || offset == offsetY) {
                return this
            }

            animationOffsetY.start(offsetY).target(offset).reset()
        }
    }
}