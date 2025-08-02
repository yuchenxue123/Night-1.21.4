package cute.neko.night.ui.screen.click.styles.normal.setting

import cute.neko.night.features.setting.AbstractSetting
import cute.neko.night.ui.interfaces.Screen
import cute.neko.night.ui.screen.click.styles.normal.NormalMain
import cute.neko.night.ui.screen.click.styles.normal.StyleNormal.MAIN_PANEL_WIDTH
import cute.neko.night.utils.animation.AnimationType
import cute.neko.night.utils.animation.SimpleAnimation
import cute.neko.night.utils.render.nano.NanoFontManager
import net.minecraft.client.gui.DrawContext

/**
 * @author yuchenxue
 * @date 2025/05/05
 */

abstract class AbstractSettingButton<T : AbstractSetting<*>>(
    protected val setting: T
) : Screen {

    protected val font = NanoFontManager.GENSHIN_15

    protected val padding = 12f

    val renderX: Float
        get() = NormalMain.renderX + padding
    val renderY: Float
        get() = NormalMain.renderY + NormalMain.addition

    protected val width: Float = MAIN_PANEL_WIDTH - padding * 2

    open val height: Float
        get() = 60f

    open fun getFinalHeight(): Float = height

    override fun render(context: DrawContext, mouseX: Int, mouseY: Int, delta: Float) {
        offset = offsetAnimation.animate()
    }

    fun visible(): Boolean = setting.visible

    // offset
    protected var offset = 0f
    private val offsetAnimation = SimpleAnimation.create().type(AnimationType.QUAD_OUT).duration(100f).target(0f)

    fun offset(offset: Float) = apply {
        // first set direct to target
        if (this.offset == 0f && offset != 0f) {
            offsetAnimation.target(offset).finish()
        }

        if (offset == this.offset || offset == offsetAnimation.target) {
            return@apply
        }

        offsetAnimation.start(this.offset).target(offset).reset()
    }
}