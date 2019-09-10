package net.draycia.minetinkersponge.commands;

import net.draycia.minetinkersponge.modifiers.ModManager;
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

        Optional<ItemStack> mainItem = ((Player)src).getItemInHand(HandTypes.MAIN_HAND);

        if (mainItem.isPresent()) {
            modManager.incrementItemLevel(mainItem.get());
            modManager.rewriteItemLore(mainItem.get());

        }

        return CommandResult.success();
    }
}