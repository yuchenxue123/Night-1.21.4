package cute.neko.night.mod.mousetweaks

import cute.neko.night.event.handler
import cute.neko.night.features.module.ClientModule
import cute.neko.night.features.module.ModuleCategory
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen
import net.minecraft.client.gui.screen.ingame.HandledScreen
import net.minecraft.screen.slot.SlotActionType
import org.lwjgl.glfw.GLFW

object MouseTweaks : ClientModule("MouseTweaks", ModuleCategory.MOD) {

    @Suppress("unused")
    private val onHandledScreenRender = handler<HandledScreenRenderEvent> { event ->
        if (dragging) {
            val screenHandler = when (val screen = mc.currentScreen) {
                is CreativeInventoryScreen -> GuiContainerCreativeScreenHandler(screen)
                is HandledScreen<*> -> GuiContainerScreenHandler(screen)
                else -> return@handler
            }

            val slot = screenHandler.getOverSlot(event.mouseX, event.mouseY) ?: return@handler

            if (shifted) {
                screenHandler.click(slot, SlotActionType.QUICK_MOVE)
            }
        }
    }

    private var dragging = false

    @Suppress("unused")
    private val onHandledScreenMouseInput = handler<HandledScreenMouseInputEvent> { event ->
        when (event.type) {
            HandledScreenMouseInputEvent.Type.CLICKED -> {
                if (event.button == MouseButton.LEFT) {
                    dragging = true
                }
            }

            HandledScreenMouseInputEvent.Type.RELEASED -> {
                dragging = false
            }

            else -> return@handler
        }
    }

    private var shifted = false

    @Suppress("unused")
    private val onHandledScreenKeyboardInput = handler<HandledScreenKeyboardInputEvent> { event ->
        val mc = MinecraftClient.getInstance()

        if (mc.currentScreen !is HandledScreen<*>) return@handler

        shifted = event.matches(GLFW.GLFW_KEY_LEFT_SHIFT) || event.matches(GLFW.GLFW_KEY_RIGHT_SHIFT)
    }
}