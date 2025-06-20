package cute.neko.night.utils.extensions

import net.minecraft.text.MutableText
import net.minecraft.text.Text

/**
 * @author yuchenxue
 * @date 2025/05/04
 */

fun MutableText.space(): MutableText = append(Text.of(" "))