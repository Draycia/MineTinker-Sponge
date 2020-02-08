package net.draycia.minetinkersponge.listeners;

import net.draycia.minetinkersponge.MineTinkerSponge;
import net.draycia.minetinkersponge.data.MTKeys;
import net.draycia.minetinkersponge.managers.ModManager;
import net.draycia.minetinkersponge.utils.MTConfig;
import org.spongepowered.api.data.Transaction;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.action.FishingEvent;
import org.spongepowered.api.event.cause.EventContextKeys;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;

public class FishingListener {

    @Listener
    public void onFish(FishingEvent.Stop event, @First Player player) {
        if (!MTConfig.CONVERT_FISHING_LOOT) {
            return;
        }

        for (Transaction<ItemStackSnapshot> snapshot : event.getTransactions()) {
            ItemStack item = snapshot.getFinal().createStack();

            if (MineTinkerSponge.getItemTypeUtils().ALL_TYPES.contains(item.getType())) {
                ModManager.convertItemStack(item, true);
                snapshot.setCustom(item.createSnapshot());
            }
        }

        event.getContext().get(EventContextKeys.USED_HAND).flatMap(player::getItemInHand).ifPresent(itemUsed -> {
            if (itemUsed.get(MTKeys.IS_MINETINKER).orElse(false)) {
                if (!event.getTransactions().isEmpty()) {
                    if (MTConfig.FISHING_XP_PER_ITEM) {
                        ModManager.addExperience(itemUsed, event.getTransactions().size());
                    } else {
                        ModManager.addExperience(itemUsed, 1);
                    }
                }
            }
        });
    }
}
