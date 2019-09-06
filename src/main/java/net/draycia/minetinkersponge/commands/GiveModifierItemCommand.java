package net.draycia.minetinkersponge.commands;

import net.draycia.minetinkersponge.modifiers.ModManager;
import net.draycia.minetinkersponge.modifiers.Modifier;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;

import java.util.Optional;

public class GiveModifierItemCommand implements CommandExecutor {

    private ModManager modManager;

    public GiveModifierItemCommand(ModManager modManager) {
        this.modManager = modManager;
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) {
        if (!(src instanceof Player)) {
            return CommandResult.empty();
        }

        Player player = (Player)src;

        args.getOne("modifier").ifPresent(modifierName -> {
            Optional<Modifier> modifier = modManager.getModifier((String)modifierName);

            if (modifier.isPresent()) {
                player.getInventory().offer(modifier.get().getModifierItem());
            }
        });


        return CommandResult.success();
    }
}