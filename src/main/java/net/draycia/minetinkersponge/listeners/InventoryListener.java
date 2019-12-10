package net.draycia.minetinkersponge.listeners;

import net.draycia.minetinkersponge.managers.ModManager;
import net.draycia.minetinkersponge.utils.ItemTypeUtils;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.item.inventory.CraftItemEvent;

public class InventoryListener {

    private ModManager modManager;

    public InventoryListener(ModManager modManager) {
        this.modManager = modManager;
    }

    @Listener
    public void onItemCraft(CraftItemEvent.Preview event) {
        event.getPreview().getSlot().peek()
                .filter(itemStack -> ItemTypeUtils.ALL_TYPES.contains(itemStack.getType()))
                .ifPresent(itemStack -> {

                modManager.convertItemStack(itemStack, true);
                event.getPreview().setCustom(itemStack);
        });
    }

}
