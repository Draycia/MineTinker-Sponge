package net.draycia.minetinkersponge.listeners;

import net.draycia.minetinkersponge.MineTinkerSponge;
import net.draycia.minetinkersponge.managers.ModManager;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.item.inventory.CraftItemEvent;

public class InventoryListener {

    @Listener
    public void onItemCraft(CraftItemEvent.Preview event) {
        event.getPreview().getSlot().peek()
                .filter(itemStack -> MineTinkerSponge.getItemTypeUtils().ALL_TYPES.contains(itemStack.getType()))
                .ifPresent(itemStack -> {

                ModManager.convertItemStack(itemStack, true);
                event.getPreview().setCustom(itemStack);
        });
    }

}
