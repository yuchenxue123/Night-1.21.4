package cute.neko.night.features.module.player

import cute.neko.night.event.events.game.player.PlayerTickEvent
import cute.neko.night.event.handle
import cute.neko.night.features.module.ClientModule
import cute.neko.night.features.module.ModuleCategory
import cute.neko.night.utils.client.chat
import cute.neko.night.utils.player.inventory.*
import cute.neko.night.utils.time.TimeTracker
import net.minecraft.client.gui.screen.ingame.InventoryScreen
import net.minecraft.enchantment.Enchantments
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.registry.tag.ItemTags
import net.minecraft.screen.ScreenHandler
import net.minecraft.screen.slot.Slot
import net.minecraft.screen.slot.SlotActionType
import org.lwjgl.glfw.GLFW

/**
 * @author yuchenxue
 * @date 2025/06/14
 */

object ModuleInvManager : ClientModule(
    "InvManager",
    ModuleCategory.PLAYER,
    key = GLFW.GLFW_KEY_G
) {

    private val delay by int("Delay", 50, 0..3000, 50)

    val version by mode("Version", DamageVersion.OLD)

    private var active = false
    private var slotIndex = 0

    private val tracker = TimeTracker()

    override fun disable() {
        active = false
        slotIndex = 0
    }

    private val onPlayerTick = handle<PlayerTickEvent> {
        val screen = mc.currentScreen ?: return@handle

        if (screen !is InventoryScreen) {
            stop()
        }

        if (screen is InventoryScreen && !active) {
            active = true
        }

        if (active && screen is InventoryScreen) {
            val screenHandler = screen.screenHandler
            val size = screenHandler.slots.size

            val sword = whichIsBetter(screenHandler, ItemType.SWORD)
            val pickaxe = whichIsBetter(screenHandler, ItemType.PICKAXES)
            val axe = whichIsBetter(screenHandler, ItemType.AXES)
            val bow = whichIsBetter(screenHandler, ItemType.BOW)
            val head = whichIsBetter(screenHandler, ItemType.HEAD)
            val chest = whichIsBetter(screenHandler, ItemType.CHEST)
            val leg = whichIsBetter(screenHandler, ItemType.LEG)
            val foot = whichIsBetter(screenHandler, ItemType.FOOT)

            if (tracker.hasPassedTime(delay.toLong())) {
                // skip empty slot
                while (!screenHandler.getSlot(slotIndex).hasStack() && slotIndex < size - 1) {
                    slotIndex++
                }

                val slot = screenHandler.getSlot(slotIndex)

                if (slot.hasStack()) {
                    val stack = slot.stack

                    if (stack.isRubbish()) {
                        interactionManager.clickSlot(
                            screenHandler.syncId,
                            slot.id,
                            1,
                            SlotActionType.THROW,
                            player
                        )
                    }

                    if (stack.isIn(ItemTags.SWORDS) && slot.id != sword
                        || stack.isIn(ItemTags.PICKAXES) && slot.id != pickaxe
                        || stack.isIn(ItemTags.AXES) && slot.id != axe
                        || stack.item == Items.BOW && slot.id != bow
                        || stack.isIn(ItemTags.HEAD_ARMOR) && slot.id != head
                        || stack.isIn(ItemTags.CHEST_ARMOR) && slot.id != chest
                        || stack.isIn(ItemTags.LEG_ARMOR) && slot.id != leg
                        || stack.isIn(ItemTags.FOOT_ARMOR) && slot.id != foot)
                    {
                        interactionManager.clickSlot(
                            screenHandler.syncId,
                            slot.id,
                            1,
                            SlotActionType.THROW,
                            player
                        )
                    }

                    // 1-4 craft
                    // 5-8 armor
                    if ((slot.id == head && slot.id != 5)
                        || (slot.id == chest && slot.id != 6)
                        || (slot.id == leg && slot.id != 7)
                        || (slot.id == foot && slot.id != 8)
                        )
                    {
                        interactionManager.clickSlot(
                            screenHandler.syncId,
                            slot.id,
                            0,
                            SlotActionType.QUICK_MOVE,
                            player
                        )
                    }
                }

                slotIndex++
                tracker.reset()

                if (slotIndex >= size) {
                    stop()
                }
            }
        }
    }

    private fun stop() {
        active = false
        slotIndex = 0
    }

    private fun whichIsBetter(screen: ScreenHandler, type: ItemType): Int {
        return screen.slots
            .filter { type.condition.invoke(it.stack) }
            .sortedWith(type.compare)
            .firstOrNull()?.id ?: -1
    }

    private enum class ItemType(val condition: (ItemStack) -> Boolean, val compare: Comparator<Slot>) {
        SWORD(
            {stack -> stack.isIn(ItemTags.SWORDS)},
            compareByDescending<Slot> {
                it.stack.getDamage(version)
            }.thenBy {
                it.stack.damage
            }
        ),
        PICKAXES(
            {stack -> stack.isIn(ItemTags.PICKAXES)},
            compareByDescending<Slot> {
                it.stack.getDigSpeed()
            }.thenBy {
                it.stack.damage
            }
        ),
        AXES(
            {stack -> stack.isIn(ItemTags.AXES)},
            compareByDescending<Slot> {
                it.stack.getDigSpeed()
            }.thenBy {
                it.stack.damage
            }
        ),
        BOW(
            {stack -> stack.item == Items.BOW },
            compareByDescending<Slot> {
                it.stack.getEnchantmentLevel(Enchantments.POWER)
            }.thenBy {
                it.stack.damage
            }
        ),

        HEAD(
            {stack -> stack.isIn(ItemTags.HEAD_ARMOR)},
            compareByDescending<Slot> {
                it.stack.getProtection()
            }.thenBy {
                it.stack.damage
            }
        ),
        CHEST(
            {stack -> stack.isIn(ItemTags.CHEST_ARMOR)},
            compareByDescending<Slot> {
                it.stack.getProtection()
            }.thenBy {
                it.stack.damage
            }
        ),
        LEG(
            {stack -> stack.isIn(ItemTags.LEG_ARMOR)},
            compareByDescending<Slot> {
                it.stack.getProtection()
            }.thenBy {
                it.stack.damage
            }
        ),
        FOOT(
            {stack -> stack.isIn(ItemTags.FOOT_ARMOR)},
            compareByDescending<Slot> {
                it.stack.getProtection()
            }.thenBy{
                it.stack.damage
            }
        )
    }
}