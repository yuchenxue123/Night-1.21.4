package cute.neko.night.ui.screen.click.styles.normal

import cute.neko.night.features.module.ClientModule
import cute.neko.night.features.setting.type.mode.AbstractModeSetting
import cute.neko.night.features.setting.type.mode.MultiEnumSetting
import cute.neko.night.features.setting.type.number.AbstractNumberSetting
import cute.neko.night.features.setting.type.primitive.BooleanSetting
import cute.neko.night.ui.interfaces.Screen
import cute.neko.night.ui.isHovered
import cute.neko.night.ui.screen.click.styles.normal.StyleNormal.MAIN_PANEL_WIDTH
import cute.neko.night.ui.screen.click.styles.normal.StyleNormal.MIDDLE_PANEL_WIDTH
import cute.neko.night.ui.screen.click.styles.normal.StyleNormal.PANEL_HEIGHT
import cute.neko.night.ui.screen.click.styles.normal.StyleNormal.PANEL_RADIUS
import cute.neko.night.ui.screen.click.styles.normal.StyleNormal.PANEL_SPACE
import cute.neko.night.ui.screen.click.styles.normal.StyleNormal.SIDE_PANEL_WIDTH
import cute.neko.night.ui.screen.click.styles.normal.setting.*
import cute.neko.night.ui.screen.special.CheckBox
import cute.neko.night.utils.animation.AnimationType
import cute.neko.night.utils.animation.SimpleAnimation
import cute.neko.night.utils.extensions.sum
import cute.neko.night.utils.render.nano.NanoUtils
import cute.neko.night.utils.render.nano.NanoFontManager
import net.minecraft.client.gui.DrawContext
import java.awt.Color
import kotlin.math.min

/**
 * @author yuchenxue
 * @date 2025/05/05
 */

object NormalMain : Screen {
    val font = NanoFontManager.GENSHIN

    val renderX: Float
        get() = StyleNormal.renderX + SIDE_PANEL_WIDTH + PANEL_SPACE + MIDDLE_PANEL_WIDTH + PANEL_SPACE
    val renderY: Float
        get() = StyleNormal.renderY

    const val TOP_BOTTOM_SPACE = 20f
    const val SETTING_SPACE = 16f

    private const val MODULE_INFO_HEIGHT = 60f

    var module: ClientModule? = null
        set(value) {
            if (value == field) return
            field = value
            reset()
        }

    private val settings = mutableListOf<AbstractSettingButton<*>>()

    // setting button addition y offset
    var addition = 0f
    private val animation = SimpleAnimation.create().type(AnimationType.QUAD_OUT).duration(100f)

    // check box for module
    private val checkbox = CheckBox.create()
        .size(60f, 30f, 6f)
        .receive {
            module?.state ?: false
        }
        .callback { state ->
            module?.let {
                if (it.locked) return@let
                it.state = state
            }
        }

    init {
        build()
    }

    override fun render(context: DrawContext, mouseX: Int, mouseY: Int, delta: Float) {
        addition = animation.animate()

        refresh()

        // background
        NanoUtils.drawRoundRect(
            renderX,
            renderY,
            MAIN_PANEL_WIDTH,
            PANEL_HEIGHT,
            PANEL_RADIUS,
            Color(40, 40, 40, 120)
        )

        // module info
        module?.let {
            val text = it.description

            val padding = 16f

            font.drawText(
                text,
                renderX + padding,
                renderY + TOP_BOTTOM_SPACE + (MODULE_INFO_HEIGHT - font.height(text)) / 2,
                Color(200, 200, 200, 255)
            )

            checkbox.position(
                renderX + MAIN_PANEL_WIDTH - padding- checkbox.width,
                renderY + TOP_BOTTOM_SPACE + (MODULE_INFO_HEIGHT - checkbox.height) / 2
            ).render(context, mouseX, mouseY, delta)
        }

        NanoUtils.drawHorizontalLine(
            renderX,
            renderY + TOP_BOTTOM_SPACE + MODULE_INFO_HEIGHT,
            MAIN_PANEL_WIDTH,
            0.5f,
            Color(200, 200, 200, 120)
        )

        // empty
        if (module != null && settings.isEmpty()) {
            val text = "Empty"
            font.drawText(
                text,
                renderX + (MAIN_PANEL_WIDTH - font.width(text)) / 2,
                renderY + TOP_BOTTOM_SPACE + MODULE_INFO_HEIGHT
                        + (PANEL_HEIGHT - TOP_BOTTOM_SPACE * 2 - MODULE_INFO_HEIGHT - font.height(text)) / 2,
                Color(200, 200, 200, 255)
            )
            return
        }

        // buttons
        NanoUtils.scissor(
            renderX,
            renderY + TOP_BOTTOM_SPACE + MODULE_INFO_HEIGHT,
            MAIN_PANEL_WIDTH,
            PANEL_HEIGHT - TOP_BOTTOM_SPACE * 2 - MODULE_INFO_HEIGHT
        ) {
            settings.forEach {
                if (!it.visible()) return@forEach

                it.render(context, mouseX, mouseY, delta)
            }
        }
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int) {
        module?.let {
            checkbox.mouseClicked(mouseX, mouseY, button)
        }

        settings.forEach {
            if (!it.visible()) return@forEach

            it.mouseClicked(mouseX, mouseY, button)
        }
    }

    override fun mouseReleased(mouseX: Double, mouseY: Double, button: Int) {
        settings.forEach {
            if (!it.visible()) return@forEach

            it.mouseReleased(mouseX, mouseY, button)
        }
    }

    override fun mouseScrolled(mouseX: Double, mouseY: Double, horizontal: Double, vertical: Double) {
        val hovered = isHovered(
            renderX, renderY + TOP_BOTTOM_SPACE + MODULE_INFO_HEIGHT,
            MAIN_PANEL_WIDTH, PANEL_HEIGHT - TOP_BOTTOM_SPACE * 2 - MODULE_INFO_HEIGHT,
            mouseX, mouseY
        )

        if (!hovered) return

        if (vertical == .0) {
            return
        }

        val height = if (settings.isNotEmpty()) settings.sum { it.getFinalHeight() + SETTING_SPACE } + SETTING_SPACE else 0f

        val minScroll = PANEL_HEIGHT - TOP_BOTTOM_SPACE * 2 - MODULE_INFO_HEIGHT - height

        val newScroll = (addition + vertical.toFloat() * 80f).coerceIn(min(minScroll, 0f), 0f)
        animation.start(addition).target(newScroll).reset()
    }

    private fun reset() {
        animation.target(0f).finish()
        build()
    }

    private fun refresh() {
        var offset = TOP_BOTTOM_SPACE + MODULE_INFO_HEIGHT + SETTING_SPACE

        settings.forEach {
            if (!it.visible()) return@forEach

            it.offset(offset)
            offset += it.getFinalHeight() + SETTING_SPACE
        }
    }

    private fun build() {
        module?.let {
            settings.clear()

            it.settings.forEach { v ->
                when (v) {
                    is BooleanSetting -> settings.add(BooleanSettingButton(v))
                    is AbstractNumberSetting -> settings.add(NumberSettingButton(v))
                    is AbstractModeSetting -> settings.add(ModeSettingButton(v))
                    is MultiEnumSetting -> settings.add(MultiEnumSettingButton(v))
                }
            }
        } ?: run {
            settings.clear()
        }

        refresh()
    }
}