package net.draycia.minetinkersponge.listeners;

import net.draycia.minetinkersponge.modifiers.ModManager;
import net.draycia.minetinkersponge.utils.ItemTypeUtils;
import org.spongepowered.api.data.Transaction;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.action.FishingEvent;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;

public class FishingListener {

    private ModManager modManager;

    public FishingListener(ModManager modManager) {
        this.modManager = modManager;
    }

    @Listener
    public void onFish(FishingEvent.Stop event) {
        for (Transaction<ItemStackSnapshot> snapshot : event.getTransactions()) {
            ItemStack item = snapshot.getFinal().createStack();

            if (ItemTypeUtils.getAllTypes().contains(item.getType())) {
                modManager.convertItemStack(item, true);
                snapshot.setCustom(item.createSnapshot());
            }
        }
    }

}
