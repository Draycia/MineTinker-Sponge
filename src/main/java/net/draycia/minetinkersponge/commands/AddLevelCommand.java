package net.draycia.minetinkersponge.commands;

import net.draycia.minetinkersponge.managers.ModManager;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.inventory.ItemStack;

import java.util.Optional;

public class AddLevelCommand implements CommandExecutor {

    private ModManager modManager;

    public AddLevelCommand(ModManager modManager) {
        this.modManager = modManager;
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) {
        if (!(src instanceof Player)) {
            return CommandResult.empty();
        }

        ((Player)src).getItemInHand(HandTypes.MAIN_HAND).ifPresent(itemStack -> {
            modManager.incrementItemLevel(itemStack);
            modManager.rewriteItemLore(itemStack);
        });

        return CommandResult.success();
    }
}