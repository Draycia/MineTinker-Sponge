package net.draycia.minetinkersponge.listeners;

import net.draycia.minetinkersponge.data.MTKeys;
import net.draycia.minetinkersponge.modifiers.ModManager;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.ChangeBlockEvent;
import org.spongepowered.api.event.cause.EventContextKeys;
import org.spongepowered.api.event.filter.cause.Root;

public class BlockBreakListener {

    private ModManager modManager;

    public BlockBreakListener(ModManager modManager) {
        this.modManager = modManager;
    }

    @Listener
    public void onBlockBreak(ChangeBlockEvent.Break event, @Root Player player) {
        event.getCause().getContext().get(EventContextKeys.USED_HAND).ifPresent(usedHand -> {
            player.getItemInHand(usedHand).ifPresent(itemStack -> {
                if (itemStack.get(MTKeys.IS_MINETINKER).isPresent()) {
                    modManager.addExperience(itemStack, 1);
                }
            });
        });
    }
}
