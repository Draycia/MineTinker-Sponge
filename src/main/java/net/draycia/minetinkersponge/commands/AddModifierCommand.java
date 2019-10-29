package net.draycia.minetinkersponge.commands;

import net.draycia.minetinkersponge.modifiers.ModManager;
import net.draycia.minetinkersponge.modifiers.Modifier;
import net.draycia.minetinkersponge.utils.ContextUtils;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.CommandExecutor;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.cause.EventContext;
import org.spongepowered.api.item.inventory.ItemStack;

import java.util.Optional;

public class AddModifierCommand implements CommandExecutor {

    private ModManager modManager;

    public AddModifierCommand(ModManager modManager) {
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
            Optional<Modifier> modifier = modManager.getModifier((modifierName.get()));
            ItemStack mainItem = player.get().getItemInHand(HandTypes.MAIN_HAND);

            if (modifier.isPresent()) {
                Optional<Integer> amount = context.getOne(Parameter.key("amount", Integer.class));

                if (amount.isPresent()) {
                    modManager.applyModifier(mainItem, modifier.get(), true, true, amount.get());
                } else {
                    modManager.applyModifier(mainItem, modifier.get(), true, true, 1);
                }
            }
        }

        return CommandResult.success();
    }
}
