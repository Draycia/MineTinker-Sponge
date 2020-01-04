package net.draycia.minetinkersponge.listeners;

import net.draycia.minetinkersponge.managers.ModManager;
import net.draycia.minetinkersponge.utils.ItemTypeUtils;
import net.draycia.minetinkersponge.utils.MTConfig;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.Item;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.cause.EventContextKeys;
import org.spongepowered.api.event.item.inventory.DropItemEvent;
import org.spongepowered.api.item.inventory.ItemStack;

public class ItemDropListener {

    @Listener(order = Order.EARLY)
    public void onItemDrop(DropItemEvent.Destruct event) {
        if (!MTConfig.CONVERT_MOB_DROPS) {
            return;
        }

        if (!event.getContext().containsKey(EventContextKeys.SPAWN_TYPE)) {
            return;
        }

        // Prevent player equipment from being dropped
        if (event.getContext().containsKey(EventContextKeys.OWNER)) {
            return;
        }

        for (Entity entity : event.getEntities()) {
            if (!(entity instanceof Item)) {
                continue;
            }

            Item item = (Item)entity;

            if (!ItemTypeUtils.ALL_TYPES.contains(item.getItemType())) {
                continue;
            }

            ItemStack newItem = item.item().get().createStack();

            ModManager.convertItemStack(newItem, true);

            entity.offer(Keys.REPRESENTED_ITEM, newItem.createSnapshot());
        }
    }

}
