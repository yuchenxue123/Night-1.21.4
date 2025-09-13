package cute.neko.night.utils.player.inventory

import net.minecraft.enchantment.Enchantment
import net.minecraft.item.ItemStack
import net.minecraft.registry.RegistryKey

class EnchantmentWeight(private vararg val pairs: Pair<RegistryKey<Enchantment>, Double>) {
    fun calculate(stack: ItemStack): Double = pairs
        .sumOf { (enchantment, weight) ->
            stack.getEnchantmentLevel(enchantment) * weight
        }
}