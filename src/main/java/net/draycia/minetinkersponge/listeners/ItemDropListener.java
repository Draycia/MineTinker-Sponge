package net.draycia.minetinkersponge.listeners;

import net.draycia.minetinkersponge.modifiers.ModManager;
import net.draycia.minetinkersponge.utils.ItemTypeUtils;
import org.spongepowered.api.data.Keys;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.Item;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.cause.EventContextKeys;
import org.spongepowered.api.event.item.inventory.DropItemEvent;
import org.spongepowered.api.item.inventory.ItemStack;

public class ItemDropListener {

    private ModManager modManager;

    public ItemDropListener(ModManager modManager) {
        this.modManager = modManager;
    }

    @Listener(order = Order.EARLY)
    public void onItemDrop(DropItemEvent.Destruct event) {
        if (!event.getContext().containsKey(EventContextKeys.SPAWN_TYPE)) {
            return;
        }

        for (Entity entity : event.getEntities()) {
            if (!(entity instanceof Item)) {
                continue;
            }

            Item item = (Item)entity;

            if (!ItemTypeUtils.getAllTypes().contains(item.getItemType())) {
                continue;
            }

            ItemStack newItem = item.item().get().createStack();

            modManager.convertItemStack(newItem, true);

            item.offer(Keys.ITEM_STACK_SNAPSHOT, newItem.createSnapshot());
        }
    }

}
