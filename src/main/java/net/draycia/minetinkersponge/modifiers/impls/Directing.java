package net.draycia.minetinkersponge.modifiers.impls;

import net.draycia.minetinkersponge.modifiers.ModManager;
import net.draycia.minetinkersponge.modifiers.Modifier;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.Item;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.cause.EventContext;
import org.spongepowered.api.event.cause.EventContextKeys;
import org.spongepowered.api.event.item.inventory.DropItemEvent;
import org.spongepowered.api.item.ItemType;

import java.util.List;

public class Directing extends Modifier {

    @Override
    public String getName() {
        return "Directing";
    }

    @Override
    public String getKey() {
        return "directing";
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public List<ItemType> getCompatibleItems() {
        return null; // Null = all, empty list = nothing
    }

    @Override
    public void onModifierRegister(Object plugin) {
        Sponge.getEventManager().registerListeners(plugin, this);
    }

    @Listener
    public void onItemDrop(DropItemEvent.Destruct event) {
        EventContext context = event.getContext();

        if (context.containsKey(EventContextKeys.BLOCK_HIT) || context.containsKey(EventContextKeys.SPAWN_TYPE)) {
            event.getCause().first(Player.class).ifPresent(player -> {
                player.getItemInHand(HandTypes.MAIN_HAND).ifPresent(itemStack -> {
                    if (ModManager.itemHasModifier(itemStack, this)) {
                        for (Entity entity : event.getEntities()) {
                            if (entity instanceof Item) {
                                Item item = (Item) entity;

                                if (item.item().exists()) {
                                    player.getInventory().offer(item.item().get().createStack());
                                }
                            }
                        }

                        event.setCancelled(true);
                    }
                });
            });
        }
    }
}
