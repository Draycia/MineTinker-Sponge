package net.draycia.minetinkersponge.commands;

import net.draycia.minetinkersponge.MineTinkerSponge;
import net.draycia.minetinkersponge.utils.ContextUtils;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.cause.EventContext;

import java.util.Optional;

public class ModifiersCommand implements CommandExecutor {

    private MineTinkerSponge mineTinkerSponge;

    public ModifiersCommand(MineTinkerSponge mineTinkerSponge) {
        this.mineTinkerSponge = mineTinkerSponge;
    }

    @Override
    public CommandResult execute(CommandContext context) {
        EventContext eventContext = context.getCause().getContext();
        Optional<Player> player = ContextUtils.getPlayerFromContext(eventContext);

        if (!player.isPresent()) {
            return CommandResult.empty();
        }

        mineTinkerSponge.getInventoryGUIManager().showViewToPlayer(player.get());

        return CommandResult.success();
    }
}