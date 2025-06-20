package cute.neko.night.module.player

import cute.neko.night.event.events.game.player.PlayerTickEvent
import cute.neko.night.event.handle
import cute.neko.night.module.ClientModule
import cute.neko.night.module.ModuleCategory
import cute.neko.night.utils.player.inventory.getEnchantmentLevel
import cute.neko.night.utils.player.inventory.isRubbish
import cute.neko.night.utils.time.TimeTracker
import net.minecraft.client.gui.screen.ingame.InventoryScreen
import net.minecraft.enchantment.Enchantment
import net.minecraft.enchantment.Enchantments
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.registry.RegistryKey
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

            val sword = whichIsBetter(screenHandler, Enchantments.SHARPNESS) { it.isIn(ItemTags.SWORDS) }
            val pickaxe = whichIsBetter(screenHandler, Enchantments.EFFICIENCY) { it.isIn(ItemTags.PICKAXES) }
            val axe = whichIsBetter(screenHandler, Enchantments.EFFICIENCY) { it.isIn(ItemTags.AXES) }
            val bow = whichIsBetter(screenHandler, Enchantments.POWER) { it.item == Items.BOW }
            val head = whichIsBetter(screenHandler, Enchantments.PROTECTION) { it.isIn(ItemTags.HEAD_ARMOR) }
            val chest = whichIsBetter(screenHandler, Enchantments.PROTECTION) { it.isIn(ItemTags.CHEST_ARMOR) }
            val leg = whichIsBetter(screenHandler, Enchantments.PROTECTION) { it.isIn(ItemTags.LEG_ARMOR) }
            val foot = whichIsBetter(screenHandler, Enchantments.PROTECTION) { it.isIn(ItemTags.FOOT_ARMOR) }

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
                        || stack.isIn(ItemTags.FOOT_ARMOR) && slot.id != foot
                        ) {
                        interactionManager.clickSlot(
                            screenHandler.syncId,
                            slot.id,
                            1,
                            SlotActionType.THROW,
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

    private fun whichIsBetter(screen: ScreenHandler, enchantment: RegistryKey<Enchantment>, filter: (ItemStack) -> Boolean): Int {
        return screen.slots
            .filter { filter.invoke(it.stack) }
            .sortedWith(
                compareBy<Slot> {
                    -map.getOrDefault(it.stack.item, 0f)
                }.thenBy {
                    -it.stack.getEnchantmentLevel(enchantment)
                }
            )
            .firstOrNull()?.id ?: -1
    }

    /**
     * Map item to value
     */
    private val map = mapOf<Item, Float>(
        // weapon and tool
        // golden
        Items.GOLDEN_SWORD to 1f,
        Items.GOLDEN_PICKAXE to 1f,
        Items.GOLDEN_AXE to 1f,
        // wooden
        Items.WOODEN_SWORD to 2f,
        Items.WOODEN_PICKAXE to 2f,
        Items.WOODEN_AXE to 2f,
        // stone
        Items.STONE_SWORD to 3f,
        Items.STONE_PICKAXE to 3f,
        Items.STONE_AXE to 3f,
        // iron
        Items.IRON_SWORD to 4f,
        Items.IRON_PICKAXE to 4f,
        Items.IRON_AXE to 4f,
        // diamond
        Items.DIAMOND_SWORD to 5f,
        Items.DIAMOND_PICKAXE to 5f,
        Items.DIAMOND_AXE to 5f,
        // nether
        Items.NETHERITE_SWORD to 6f,
        Items.NETHERITE_PICKAXE to 6f,
        Items.NETHERITE_AXE to 6f,

        // armor
        // leather
        Items.LEATHER_HELMET to 1f,
        Items.LEATHER_CHESTPLATE to 1f,
        Items.LEATHER_LEGGINGS to 1f,
        Items.LEATHER_BOOTS to 1f,
        // golden
        Items.GOLDEN_HELMET to 1.5f,
        Items.GOLDEN_CHESTPLATE to 1.5f,
        Items.GOLDEN_LEGGINGS to 1.5f,
        // iron
        Items.IRON_HELMET to 2f,
        Items.IRON_CHESTPLATE to 2f,
        Items.IRON_LEGGINGS to 2f,
        Items.IRON_BOOTS to 2f,
        // diamond
        Items.DIAMOND_HELMET to 3f,
        Items.DIAMOND_CHESTPLATE to 3f,
        Items.DIAMOND_LEGGINGS to 3f,
        Items.DIAMOND_BOOTS to 3f,
        // nether
        Items.NETHERITE_HELMET to 4f,
        Items.NETHERITE_CHESTPLATE to 4f,
        Items.NETHERITE_LEGGINGS to 4f,
        Items.NETHERITE_BOOTS to 4f,
    )
}