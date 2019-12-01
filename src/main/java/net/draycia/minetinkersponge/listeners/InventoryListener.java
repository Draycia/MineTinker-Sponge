package net.draycia.minetinkersponge.listeners;

import net.draycia.minetinkersponge.managers.ModManager;
import net.draycia.minetinkersponge.utils.ItemTypeUtils;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.item.inventory.CraftItemEvent;
import org.spongepowered.api.item.inventory.ItemStack;

import java.util.Optional;

public class InventoryListener {

    private ModManager modManager;

    public InventoryListener(ModManager modManager) {
        this.modManager = modManager;
    }

    @Listener
    public void onItemCraft(CraftItemEvent.Preview event) {
        Optional<ItemStack> snapshot = event.getPreview().getSlot().peek();

        if (snapshot.isPresent()) {
            ItemStack itemStack = snapshot.get();

            if (ItemTypeUtils.ALL_TYPES.contains(itemStack.getType())) {
                modManager.convertItemStack(itemStack, true);
                event.getPreview().setCustom(itemStack);
            }
        }
    }

}
