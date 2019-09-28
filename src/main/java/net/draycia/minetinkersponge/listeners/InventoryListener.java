package net.draycia.minetinkersponge.listeners;

import net.draycia.minetinkersponge.data.MTKeys;
import net.draycia.minetinkersponge.modifiers.ModManager;
import net.draycia.minetinkersponge.utils.ItemTypeUtils;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.item.inventory.InteractInventoryEvent;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.ItemStack;

import java.util.Optional;

public class InventoryListener {

    private ModManager modManager;

    public InventoryListener(ModManager modManager) {
        this.modManager = modManager;
    }

    @Listener
    public void onInventoryOpen(InteractInventoryEvent.Open event) {
        for (Inventory inventory : event.getTargetInventory().slots()) {
            Optional<ItemStack> itemStack = inventory.peek();

            if (!itemStack.isPresent()) {
                continue;
            }

            if (ItemTypeUtils.getAllTypes().contains(itemStack.get().getType())) {
                if (!itemStack.get().get(MTKeys.IS_MINETINKER).orElse(false)) {
                    modManager.convertItemStack(itemStack.get(), true);
                }
            }
        }
    }

}
