package cute.neko.generator

import cute.neko.generator.types.ItemTagsProvider
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator

/**
 * @author yuchenxue
 * @date 2025/06/19
 */

class DataGenerator : DataGeneratorEntrypoint {

    override fun onInitializeDataGenerator(fabricDataGenerator: FabricDataGenerator) {
        val pack = fabricDataGenerator.createPack()

        pack.addProvider { output, completableFuture ->
            ItemTagsProvider(output, completableFuture)
        }
    }
}
