package net.draycia.minetinkersponge.commands;

import net.draycia.minetinkersponge.managers.ModManager;
import net.draycia.minetinkersponge.modifiers.Modifier;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.Optional;

public class AddModifierCommand implements CommandExecutor {

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) {
        if (!(src instanceof Player)) {
            return CommandResult.empty();
        }

        args.<String>getOne("modifier").ifPresent(modifierName -> {
            Optional<Modifier> optionalModifier = ModManager.getModifier(modifierName);
            Optional<ItemStack> mainItem = ((Player)src).getItemInHand(HandTypes.MAIN_HAND);

            mainItem.flatMap(itemStack -> optionalModifier).ifPresent(modifier -> {
                if (args.getOne("amount").isPresent()) {
                    ModManager.applyModifier(mainItem.get(), modifier, true, true, (int)args.getOne("amount").get());
                } else {
                    ModManager.applyModifier(mainItem.get(), modifier, true, true, 1);
                }

                src.sendMessage(Text.of(TextColors.GREEN, "Added modifier ", modifier.getName(), "!"));
            });
        });

        return CommandResult.success();
    }
}
