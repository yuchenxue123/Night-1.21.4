package cute.neko.night.utils.player.inventory

import cute.neko.night.setting.type.mode.SubMode
import cute.neko.night.utils.client.player
import cute.neko.night.utils.client.world
import cute.neko.night.utils.player.inventory.ExtraItemTags.RUBBISH_ITEM_TAG
import net.minecraft.block.Blocks
import net.minecraft.component.DataComponentTypes
import net.minecraft.component.type.AttributeModifiersComponent
import net.minecraft.enchantment.Enchantment
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.enchantment.Enchantments
import net.minecraft.entity.attribute.EntityAttribute
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.registry.tag.ItemTags

/**
 * @author yuchenxue
 * @date 2025/06/20
 */

private val DAMAGE_OLD_VERSION = mapOf<Item, Double>(
    // sword
    Items.WOODEN_SWORD to 4.0,
    Items.STONE_SWORD to 5.0,
    Items.IRON_SWORD to 6.0,
    Items.GOLDEN_SWORD to 4.0,
    Items.DIAMOND_SWORD to 7.0,
    // guess
    Items.NETHERITE_SWORD to 8.0,

    // pickaxe
    Items.WOODEN_PICKAXE to 2.0,
    Items.STONE_PICKAXE to 3.0,
    Items.IRON_PICKAXE to 4.0,
    Items.GOLDEN_PICKAXE to 2.0,
    Items.DIAMOND_PICKAXE to 5.0,
    // guess
    Items.NETHERITE_PICKAXE to 6.0,

    // axe
    Items.WOODEN_AXE to 3.0,
    Items.STONE_AXE to 4.0,
    Items.IRON_AXE to 5.0,
    Items.GOLDEN_AXE to 3.0,
    Items.DIAMOND_AXE to 6.0,
    // guess
    Items.NETHERITE_AXE to 7.0,

    // hoe
    Items.WOODEN_HOE to 1.0,
    Items.STONE_HOE to 1.0,
    Items.IRON_HOE to 1.0,
    Items.GOLDEN_HOE to 1.0,
    Items.DIAMOND_HOE to 1.0,
    // guess
    Items.NETHERITE_HOE to 1.0,

    // diamond
    Items.WOODEN_SHOVEL to 1.0,
    Items.STONE_SHOVEL to 2.0,
    Items.IRON_SHOVEL to 3.0,
    Items.GOLDEN_SHOVEL to 1.0,
    Items.DIAMOND_SHOVEL to 4.0,
    // guess
    Items.NETHERITE_SHOVEL to 5.0,
)

private val DAMAGE_HIGH_VERSION = mapOf<Item, Double>(
    // sword
    Items.WOODEN_SWORD to 4.0,
    Items.STONE_SWORD to 5.0,
    Items.IRON_SWORD to 6.0,
    Items.GOLDEN_SWORD to 4.0,
    Items.DIAMOND_SWORD to 7.0,
    Items.NETHERITE_SWORD to 8.0,

    // pickaxe
    Items.WOODEN_PICKAXE to 2.0,
    Items.STONE_PICKAXE to 3.0,
    Items.IRON_PICKAXE to 4.0,
    Items.GOLDEN_PICKAXE to 2.0,
    Items.DIAMOND_PICKAXE to 5.0,
    Items.NETHERITE_PICKAXE to 6.0,

    // axe
    Items.WOODEN_AXE to 7.0,
    Items.STONE_AXE to 9.0,
    Items.IRON_AXE to 9.0,
    Items.GOLDEN_AXE to 7.0,
    Items.DIAMOND_AXE to 9.0,
    Items.NETHERITE_AXE to 10.0,

    // hoe
    Items.WOODEN_HOE to 1.0,
    Items.STONE_HOE to 1.0,
    Items.IRON_HOE to 1.0,
    Items.GOLDEN_HOE to 1.0,
    Items.DIAMOND_HOE to 1.0,
    Items.NETHERITE_HOE to 1.0,

    // shovel
    Items.WOODEN_SHOVEL to 2.5,
    Items.STONE_SHOVEL to 3.5,
    Items.IRON_SHOVEL to 4.5,
    Items.GOLDEN_SHOVEL to 2.5,
    Items.DIAMOND_SHOVEL to 5.5,
    Items.NETHERITE_SHOVEL to 6.5,
)

private val ARMOR_PROTECTION = mapOf<Item, Double>(
    // leather
    Items.LEATHER_HELMET to 1.0,
    Items.LEATHER_CHESTPLATE to 3.0,
    Items.LEATHER_LEGGINGS to 2.0,
    Items.LEATHER_BOOTS to 1.0,

    // golden
    Items.GOLDEN_HELMET to 2.0,
    Items.GOLDEN_CHESTPLATE to 5.0,
    Items.GOLDEN_LEGGINGS to 3.0,
    Items.GOLDEN_BOOTS to 1.0,

    // chainmail
    Items.CHAINMAIL_HELMET to 2.0,
    Items.CHAINMAIL_CHESTPLATE to 5.0,
    Items.CHAINMAIL_LEGGINGS to 4.0,
    Items.CHAINMAIL_BOOTS to 1.0,

    // iron
    Items.IRON_HELMET to 2.0,
    Items.IRON_CHESTPLATE to 6.0,
    Items.IRON_LEGGINGS to 5.0,
    Items.IRON_BOOTS to 2.0,

    // diamond
    Items.DIAMOND_HELMET to 3.0,
    Items.DIAMOND_CHESTPLATE to 8.0,
    Items.DIAMOND_LEGGINGS to 6.0,
    Items.DIAMOND_BOOTS to 3.0,

    // nether
    Items.NETHERITE_HELMET to 3.1,
    Items.NETHERITE_CHESTPLATE to 8.1,
    Items.NETHERITE_LEGGINGS to 6.1,
    Items.NETHERITE_BOOTS to 3.1,
)

enum class DamageVersion(override val modeName: String) : SubMode {
    OLD("Old"),
    HIGH("High")
}

fun ItemStack.getEnchantmentLevel(key: RegistryKey<Enchantment>): Int {
    return world.registryManager.getOptional(RegistryKeys.ENCHANTMENT)
        .flatMap { registry -> registry.getEntry(key.value) }
        .map { enchantment -> EnchantmentHelper.getLevel(enchantment, this) }
        .orElse(0)

//        val optional = world.registryManager.getOptional(RegistryKeys.ENCHANTMENT)
//        if (optional.isPresent) {
//            val registry = optional.get()
//            val sharpness = registry.getEntry(key.value)
//
//            if (sharpness.isPresent) {
//                return EnchantmentHelper.getLevel(sharpness.get(), stack)
//            }
//        }
//        return 0
}

fun ItemStack.getAttributeValue(attribute: RegistryEntry<EntityAttribute>) = item.components
    .getOrDefault(
        DataComponentTypes.ATTRIBUTE_MODIFIERS,
        AttributeModifiersComponent.DEFAULT
    )
    .modifiers()
    .filter { modifier -> modifier.attribute() == attribute }
    .firstNotNullOfOrNull { modifier -> modifier.modifier().value() }

fun ItemStack.getSharpnessDamage(level: Int = getEnchantmentLevel(Enchantments.SHARPNESS)) =
    if (level == 0) 0.0 else 0.5 * level + 0.5

// calculate the attack damage
val ItemStack.attackDamage: Double
    get() {
        val entityDamage = player.getAttributeValue(EntityAttributes.ATTACK_DAMAGE)
        val baseDamage = getAttributeValue(EntityAttributes.ATTACK_DAMAGE) ?: return 0.0

        return entityDamage + baseDamage + getSharpnessDamage()
    }

fun ItemStack.getDamage(version: DamageVersion): Double  {
    val damageWithNoEnchantment = when (version) {
        DamageVersion.OLD -> DAMAGE_OLD_VERSION.getOrDefault(this.item, 0.0)
        DamageVersion.HIGH -> DAMAGE_HIGH_VERSION.getOrDefault(this.item, 0.0)
    }

    return damageWithNoEnchantment + getSharpnessDamage()
}

fun ItemStack.getProtection(): Double {
    val protectionWithNoEnchantment = ARMOR_PROTECTION.getOrDefault(this.item, 0.0)

    val enchantmentAddition= EnchantmentWeight(
        Enchantments.PROTECTION to 0.98,
        Enchantments.FIRE_PROTECTION to 0.5,
        Enchantments.BLAST_PROTECTION to 0.25,
        Enchantments.PROJECTILE_PROTECTION to 0.1,
    ).calculate(this)

    return protectionWithNoEnchantment + enchantmentAddition
}

fun ItemStack.getDigSpeed(): Float {
    return when {
        isIn(ItemTags.AXES) -> return getMiningSpeedMultiplier(Blocks.OAK_PLANKS.defaultState)
        isIn(ItemTags.PICKAXES) -> return getMiningSpeedMultiplier(Blocks.STONE.defaultState)
        isIn(ItemTags.SHOVELS) -> return getMiningSpeedMultiplier(Blocks.DIRT.defaultState)
        else -> 0f
    }
}

fun ItemStack.isRubbish(): Boolean =
    this.isIn(RUBBISH_ITEM_TAG) || this.isIn(ItemTags.HOES) || this.isIn(ItemTags.SHOVELS)

fun Item.isRubbish(): Boolean = this.defaultStack.isRubbish()

