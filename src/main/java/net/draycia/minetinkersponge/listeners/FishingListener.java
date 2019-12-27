package net.draycia.minetinkersponge.listeners;

import net.draycia.minetinkersponge.managers.ModManager;
import net.draycia.minetinkersponge.utils.ItemTypeUtils;
import net.draycia.minetinkersponge.utils.MTConfig;
import org.spongepowered.api.data.Transaction;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.action.FishingEvent;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;

public class FishingListener {

    private ModManager modManager = ModManager.getInstance();

    @Listener
    public void onFish(FishingEvent.Stop event) {
        if (!MTConfig.CONVERT_FISHING_LOOT) {
            return;
        }

        for (Transaction<ItemStackSnapshot> snapshot : event.getTransactions()) {
            ItemStack item = snapshot.getFinal().createStack();

            if (ItemTypeUtils.ALL_TYPES.contains(item.getType())) {
                modManager.convertItemStack(item, true);
                snapshot.setCustom(item.createSnapshot());
            }
        }
    }

}
