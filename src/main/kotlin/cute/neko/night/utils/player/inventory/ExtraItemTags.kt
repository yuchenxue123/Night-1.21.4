package cute.neko.night.utils.player.inventory

import cute.neko.night.Night
import cute.neko.night.utils.interfaces.Accessor
import net.minecraft.item.Item
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.Identifier

object ExtraItemTags : Accessor {
    @JvmField
    val RUBBISH_ITEM_TAG: TagKey<Item> = TagKey.of(RegistryKeys.ITEM, Identifier.of(Night.MOD_ID, "rubbish_item"))
}