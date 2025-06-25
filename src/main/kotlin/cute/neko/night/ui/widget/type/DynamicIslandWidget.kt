package cute.neko.night.ui.widget.type

import cute.neko.night.ui.interfaces.Drawable
import cute.neko.night.ui.widget.AbstractWidget
import cute.neko.night.ui.widget.WidgetType
import net.minecraft.client.gui.DrawContext

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

    private val drawables = mutableListOf<IslandDrawable>()
        .apply {
            addAll(arrayOf(
                StaticDrawable,
                BlockCounterDrawable
            ))
        }
//
//    private val activeDrawable: IslandDrawable
//        get() = drawables.filter {  }
//
    override fun render(context: DrawContext) {
//        activeDrawable.render(context)
    }

    object StaticDrawable : IslandDrawable() {
        override val width: Float
            get() = 0f
        override val height: Float
            get() = 0f

        override fun render(context: DrawContext) {
        }

    }

    object BlockCounterDrawable : IslandDrawable() {
        override val width: Float
            get() = 0f
        override val height: Float
            get() = 0f

        override fun render(context: DrawContext) {

        }
    }

    abstract class IslandDrawable : Drawable {
        abstract val width: Float
        abstract val height: Float
    }
}