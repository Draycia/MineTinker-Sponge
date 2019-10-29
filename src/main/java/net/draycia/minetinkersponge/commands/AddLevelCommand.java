package net.draycia.minetinkersponge.commands;

import net.draycia.minetinkersponge.modifiers.ModManager;
import net.draycia.minetinkersponge.utils.ContextUtils;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandExecutor;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.cause.EventContext;
import org.spongepowered.api.item.inventory.ItemStack;

import java.util.Optional;

public class AddLevelCommand implements CommandExecutor {

    private ModManager modManager;

    public AddLevelCommand(ModManager modManager) {
        this.modManager = modManager;
    }

    @Override
    public CommandResult execute(CommandContext context) {
        EventContext eventContext = context.getCause().getContext();
        Optional<Player> player = ContextUtils.getPlayerFromContext(eventContext);

        if (!player.isPresent()) {
            return CommandResult.empty();
        }

        ItemStack mainItem = player.get().getItemInHand(HandTypes.MAIN_HAND);

        modManager.incrementItemLevel(mainItem);
        modManager.rewriteItemLore(mainItem);

        return CommandResult.success();
    }
}