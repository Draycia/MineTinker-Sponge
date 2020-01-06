package net.draycia.minetinkersponge.commands;

import com.google.inject.Inject;
import net.draycia.minetinkersponge.MineTinkerSponge;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;

public class ModifiersCommand implements CommandExecutor {

    @Inject
    private MineTinkerSponge plugin;

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) {
        if (!(src instanceof Player)) {
            return CommandResult.empty();
        }

        plugin.getGuiManager().showViewToPlayer((Player)src);

        return CommandResult.success();
    }
}