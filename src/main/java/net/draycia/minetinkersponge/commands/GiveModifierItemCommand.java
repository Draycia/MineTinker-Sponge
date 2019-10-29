package net.draycia.minetinkersponge.commands;

import net.draycia.minetinkersponge.modifiers.ModManager;
import net.draycia.minetinkersponge.modifiers.Modifier;
import net.draycia.minetinkersponge.utils.ContextUtils;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.CommandExecutor;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.cause.EventContext;

import java.util.Optional;

public class GiveModifierItemCommand implements CommandExecutor {

    private ModManager modManager;

    public GiveModifierItemCommand(ModManager modManager) {
        this.modManager = modManager;
    }

    @Override
    public CommandResult execute(CommandContext context) {
        EventContext eventContext = context.getCause().getContext();
        Optional<Player> player = ContextUtils.getPlayerFromContext(eventContext);

        if (!player.isPresent()) {
            return CommandResult.empty();
        }

        Optional<String> modifierName = context.getOne(Parameter.key("modifier", String.class));

        if (modifierName.isPresent()) {
            Optional<Modifier> modifier = modManager.getModifier(modifierName.get());

            if (modifier.isPresent()) {
                player.get().getInventory().offer(modifier.get().getModifierItem());
            }
        }

        return CommandResult.success();
    }
}