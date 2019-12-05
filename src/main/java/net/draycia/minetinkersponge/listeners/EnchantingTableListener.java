package net.draycia.minetinkersponge.listeners;

import net.draycia.minetinkersponge.utils.MTConfig;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.InteractBlockEvent;

public class EnchantingTableListener {

    @Listener
    public void onInteract(InteractBlockEvent.Secondary event) {
        if (!MTConfig.DISABLE_ENCHANTING_TABLES) {
            return;
        }

        if (event.getTargetBlock().getState().getType() == BlockTypes.ENCHANTING_TABLE) {
            event.setCancelled(true);
        }
    }

}
