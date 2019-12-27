package net.draycia.minetinkersponge.commands;

import net.draycia.minetinkersponge.managers.ModManager;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;

import java.util.Optional;

public class AddSlotsCommand implements CommandExecutor {

    private ModManager modManager = ModManager.getInstance();

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) {
        if (!(src instanceof Player)) {
            return CommandResult.empty();
        }

        ((Player)src).getItemInHand(HandTypes.MAIN_HAND).ifPresent(itemStack -> {
            Integer amount = args.<Integer>getOne(Text.of("amount")).orElse(1);

            modManager.setItemModifierSlots(itemStack, modManager.getItemModifierSlots(itemStack) + amount);
            modManager.rewriteItemLore(itemStack);
        });

        return CommandResult.success();
    }
}