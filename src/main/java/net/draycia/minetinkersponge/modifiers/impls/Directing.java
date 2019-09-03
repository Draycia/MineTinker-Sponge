package net.draycia.minetinkersponge.modifiers.impls;

import net.draycia.minetinkersponge.modifiers.ModManager;
import net.draycia.minetinkersponge.modifiers.Modifier;
import net.draycia.minetinkersponge.utils.CompositeUnmodifiableList;
import net.draycia.minetinkersponge.utils.ItemTypeUtils;
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
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;

import java.util.List;

public class Directing extends Modifier {

    private ModManager modManager;

    @Override
    public String getName() {
        return "Directing";
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public int getLevelWeight() {
        return 1;
    }

    @Override
    public ItemType getModifierItemType() {
        return ItemTypes.COMPASS;
    }

    @Override
    public List<ItemType> getCompatibleItems() {
        return new CompositeUnmodifiableList<>(ItemTypeUtils.getToolTypes(), ItemTypeUtils.getWeaponTypes());
    }

    @Override
    public void onModifierRegister(Object plugin) {
        Sponge.getEventManager().registerListeners(plugin, this);
    }

    public Directing(ModManager modManager) {
        this.modManager = modManager;
    }

    @Listener
    public void onItemDrop(DropItemEvent.Destruct event) {
        EventContext context = event.getContext();

        if (context.containsKey(EventContextKeys.BLOCK_HIT) || context.containsKey(EventContextKeys.SPAWN_TYPE)) {
            event.getCause().first(Player.class).ifPresent(player -> {
                player.getItemInHand(HandTypes.MAIN_HAND).ifPresent(itemStack -> {
                    if (modManager.itemHasModifier(itemStack, this)) {
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
