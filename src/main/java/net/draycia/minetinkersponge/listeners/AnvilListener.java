package net.draycia.minetinkersponge.listeners;

import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.InteractBlockEvent;
import org.spongepowered.api.event.filter.cause.First;

public class AnvilListener {

    @Listener
    public void onAnvilClick(InteractBlockEvent.Secondary event, @First Player player) {
        if (event.getTargetBlock().getState().getType() == BlockTypes.ANVIL) {
            event.setCancelled(true);
        }
    }
}
