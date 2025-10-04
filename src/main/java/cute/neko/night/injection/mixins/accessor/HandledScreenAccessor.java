package cute.neko.night.injection.mixins.accessor;

import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(HandledScreen.class)
public interface HandledScreenAccessor {

    @Invoker("getSlotAt")
    Slot neko$getOverSlot(double mouseX, double mouseY);

    @Invoker("onMouseClick")
    void neko$clickSlot(Slot slot, int slotId, int button, SlotActionType actionType);
}
