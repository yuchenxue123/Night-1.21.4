package cute.neko.generator.types

import cute.neko.night.utils.player.inventory.ExtraItemTags
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider
import net.minecraft.item.Items
import net.minecraft.registry.RegistryWrapper.WrapperLookup
import java.util.concurrent.CompletableFuture

/**
 * @author yuchenxue
 * @date 2025/06/19
 */

class ItemTagsProvider(
    output: FabricDataOutput,
    completableFuture: CompletableFuture<WrapperLookup>
) : FabricTagProvider.ItemTagProvider(output, completableFuture) {

    override fun configure(wrapper: WrapperLookup) {
        getOrCreateTagBuilder(ExtraItemTags.RUBBISH_ITEM_TAG)
            // utility blocks
            .add(Items.CRAFTING_TABLE)
            .add(Items.FURNACE)
            .add(Items.BREWING_STAND)
            .add(Items.CHEST)
            .add(Items.ENDER_CHEST)
            .add(Items.TRAPPED_CHEST)
            .add(Items.SHULKER_BOX)
            .add(Items.BARREL)
            .add(Items.ANVIL)
            .add(Items.TORCH)
            .add(Items.REDSTONE_TORCH)
            .add(Items.LADDER)

            // oak blocks
            .add(Items.OAK_BUTTON)
            .add(Items.OAK_DOOR)
            .add(Items.OAK_SIGN)
            .add(Items.OAK_PRESSURE_PLATE)
            .add(Items.OAK_SLAB)
            .add(Items.OAK_STAIRS)
            .add(Items.OAK_FENCE)

            // stone blocks
            .add(Items.STONE_BUTTON)
            .add(Items.STONE_PRESSURE_PLATE)
            .add(Items.STONE_SLAB)
            .add(Items.STONE_STAIRS)
            .add(Items.COBBLESTONE_SLAB)
            .add(Items.COBBLESTONE_STAIRS)
            .add(Items.COBBLESTONE_WALL)

            // drop blocks
            .add(Items.SAND)
            .add(Items.RED_SAND)
            .add(Items.GRAVEL)

            // misc
            .add(Items.GLASS_BOTTLE)
            .add(Items.WHEAT_SEEDS)
            .add(Items.WHEAT)
            .add(Items.STICK)
    }
}
