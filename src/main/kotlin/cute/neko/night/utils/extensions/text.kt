package cute.neko.night.utils.extensions

import net.minecraft.text.MutableText
import net.minecraft.text.Text

fun MutableText.space(): MutableText = append(Text.of(" "))