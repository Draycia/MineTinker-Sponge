package net.draycia.minetinkersponge.commands;

import net.draycia.minetinkersponge.MineTinkerSponge;
import net.draycia.minetinkersponge.utils.InventoryGUIManager;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;

public class ModifiersCommand implements CommandExecutor {

    private MineTinkerSponge mineTinkerSponge;

    public ModifiersCommand(MineTinkerSponge mineTinkerSponge) {
        this.mineTinkerSponge = mineTinkerSponge;
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) {
        if (!(src instanceof Player)) {
            return CommandResult.empty();
        }

        mineTinkerSponge.getInventoryGUIManager().showViewToPlayer((Player)src);

        return CommandResult.success();
    }
}