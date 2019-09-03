package net.draycia.minetinkersponge.commands;

import net.draycia.minetinkersponge.modifiers.ModManager;
import net.draycia.minetinkersponge.modifiers.Modifier;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.inventory.ItemStack;

import java.util.Optional;

public class AddModifierCommand implements CommandExecutor {

    private ModManager modManager;

    public AddModifierCommand(ModManager modManager) {
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
            Optional<ItemStack> mainItem = player.getItemInHand(HandTypes.MAIN_HAND);

            if (mainItem.isPresent() && modifier.isPresent()) {
                if (args.getOne("amount").isPresent()) {
                    modManager.applyModifier(mainItem.get(), modifier.get(), true, (int)args.getOne("amount").get());
                } else {
                    modManager.applyModifier(mainItem.get(), modifier.get(), true);
                }
            }
        });

        return CommandResult.success();
    }
}
