package net.draycia.minetinkersponge.listeners;

import net.draycia.minetinkersponge.data.MTKeys;
import net.draycia.minetinkersponge.managers.ModManager;
import org.spongepowered.api.data.type.HandType;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.ChangeBlockEvent;
import org.spongepowered.api.event.cause.EventContextKeys;
import org.spongepowered.api.event.filter.cause.Root;
import org.spongepowered.api.item.inventory.ItemStack;

import java.util.Optional;

public class BlockBreakListener {

    private ModManager modManager;

    public BlockBreakListener(ModManager modManager) {
        this.modManager = modManager;
    }

    @Listener
    public void onBlockBreak(ChangeBlockEvent.Break event, @Root Player player) {
        event.getContext().get(EventContextKeys.USED_HAND)
                .flatMap(player::getItemInHand)
                .filter(itemStack -> itemStack.get(MTKeys.IS_MINETINKER).orElse(false))
                .ifPresent(itemStack -> modManager.addExperience(itemStack, 1));
    }

}
