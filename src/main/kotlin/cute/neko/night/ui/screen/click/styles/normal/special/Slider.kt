package cute.neko.night.ui.screen.click.styles.normal.special

import cute.neko.night.ui.interfaces.Screen
import cute.neko.night.ui.isHovered
import cute.neko.night.utils.animation.AnimationType
import cute.neko.night.utils.animation.SimpleAnimation
import cute.neko.night.utils.render.nano.NanoFontManager
import cute.neko.night.utils.render.nano.NanoUtils
import net.minecraft.client.gui.DrawContext
import org.joml.Vector2f
import java.awt.Color
import kotlin.math.max

/**
 * @author yuchenxue
 * @date 2025/05/06
 */

class Slider : Screen {

    companion object {
        fun create(): Slider {
            return Slider()
        }
    }

    private var renderX = 0f
    private var renderY = 0f

    var width = 100f
        private set
    var height = 10f
        private set

    private val radius: Float
        get() = height / 2

    // process
    private var receive: () -> Float = { 0f }
    private var callback: (process: Float) -> Unit = {}
    private var process = 0f
    private val animation = SimpleAnimation.create().type(AnimationType.QUAD_OUT).duration(100f).target(process).finish()

    // dragging
    private var dragging = false

    // float tag
    private var tag = false
    private var tagDistance = 0f
    private val tagAnimation = SimpleAnimation.create().type(AnimationType.QUAD_IN).duration(200f).target(tagDistance).finish()
    private var tagText: (() -> String) = { "ERROR" }

    override fun render(context: DrawContext, mouseX: Int, mouseY: Int, delta: Float) {
        process = animation.animate()
        tagDistance = tagAnimation.animate()

        if (dragging) {
            val dx = (mouseX * mc.window.scaleFactor.toFloat() - renderX).coerceIn(0f, width)

            // send out the process of slider
            callback.invoke((dx / (width - radius * 2)).coerceIn(0f, 1f))

            // update process
            update()
        }

        // background
        NanoUtils.drawRoundRect(
            renderX,
            renderY,
            width,
            height,
            radius,
            Color(40, 40, 40, 40)
        )

        // process
        NanoUtils.drawRoundRect(
            renderX,
            renderY,
            (width - radius * 2) * process + radius * 2,
            height,
            radius,
            Color(180, 180, 180, 80)
        )

        NanoUtils.drawCircle(
            renderX + ((width - radius * 2) * process) + radius,
            renderY + radius,
            radius,
            Color(180, 180, 180, 100)
        )

        // float tag
        if (dragging && tag || !tagAnimation.hasFinished()) {
            val text = tagText.invoke()

            val font = NanoFontManager.GENSHIN_15

            val pw = max(font.width(text) + 8f, 16f) / 2

            val start = Vector2f(
                renderX + ((width - radius * 2) * process) + radius,
                renderY - tagDistance
            )

            NanoUtils.drawPolygon(
                start.x,
                start.y,
                arrayOf(
                    Vector2f(start.x + pw, start.y - 8f),
                    Vector2f(start.x + pw, start.y - 32f),
                    Vector2f(start.x - pw, start.y - 32f),
                    Vector2f(start.x - pw, start.y - 8f)
                ),
                Color(180, 180, 180, ((tagDistance / 8f) * 80).toInt())
            )

            font.drawText(
                text,
                start.x - pw + (pw * 2 - font.width(text)) / 2,
                start.y - 32f + (24f - font.height(text)) / 2,
                Color(200, 200, 200, ((tagDistance / 8f) * 255).toInt()),
            )
        }
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int) {
        val hovered = isHovered(renderX, renderY, width, height, mouseX, mouseY)

        if (hovered && button == 0) {
            dragging = true
            tagAnimation.start(tagDistance).target(8f).reset()
        }
    }

    override fun mouseReleased(mouseX: Double, mouseY: Double, button: Int) {
        if (dragging && button == 0) {
            dragging = false
            tagAnimation.start(tagDistance).target(2f).reset()
        }
    }

    private fun update() {
        val process = receive.invoke()

        if (process == this.process || process == animation.target) {
            return
        }

        animation.start(this.process).target(process).reset()
    }

    fun tag(show: Boolean = true) = apply {
        this.tag = show
    }

    fun position(x: Float, y: Float) = apply {
        this.renderX = x
        this.renderY = y
    }

    fun size(width: Float, height: Float) = apply {
        this.width = width
        this.height = height
    }

    fun size(width: Float) = apply {
        this.width = width
    }

    fun receive(receive: () -> Float) = apply {
        this.receive = receive
        update()
    }

    fun callback(callback: (process: Float) -> Unit) = apply {
        this.callback = callback
    }

    fun tagText(tag: () -> String) = apply {
        this.tagText = tag
    }
}