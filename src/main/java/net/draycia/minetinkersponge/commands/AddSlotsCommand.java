package net.draycia.minetinkersponge.commands;

import net.draycia.minetinkersponge.managers.ModManager;
import net.draycia.minetinkersponge.utils.MTTranslations;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class AddSlotsCommand implements CommandExecutor {

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) {
        if (!(src instanceof Player)) {
            return CommandResult.empty();
        }

        ((Player)src).getItemInHand(HandTypes.MAIN_HAND).ifPresent(itemStack -> {
            Integer amount = args.<Integer>getOne(Text.of("amount")).orElse(1);

            ModManager.setItemModifierSlots(itemStack, ModManager.getItemModifierSlots(itemStack) + amount);
            ModManager.rewriteItemLore(itemStack);

            src.sendMessage(Text.of(TextColors.GREEN, MTTranslations.ADDED_SLOTS));
        });

        return CommandResult.success();
    }
}