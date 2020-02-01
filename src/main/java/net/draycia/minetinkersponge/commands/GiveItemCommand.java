package net.draycia.minetinkersponge.commands;

import net.draycia.minetinkersponge.managers.ModManager;
import net.draycia.minetinkersponge.modifiers.Modifier;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.Optional;

public class GiveItemCommand implements CommandExecutor {

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) {
        Optional<Player> target = args.getOne("player");
        Optional<ItemType> item = args.getOne("item");
        Optional<String> mods = args.getOne("modifier");

        Player player;

        if (!target.isPresent()) {
            if (src instanceof Player) {
                player = (Player) src;
            } else {
                return CommandResult.success();
            }
        } else {
            player = target.get();
        }

        item.ifPresent(itemType -> {
            ItemStack itemStack = ItemStack.of(itemType);
            ModManager.convertItemStack(itemStack, true);

            for (String modEntry : mods.orElse("").split(" ")) {
                String[] pieces = modEntry.split(":");

                if (pieces.length != 2) {
                    continue;
                }

                int level = Integer.parseInt(pieces[1]);

                if (pieces[0].equals("slots")) {
                    ModManager.setItemModifierSlots(itemStack, level);
                } else if (pieces[0].equals("level")) {
                    ModManager.setItemLevel(itemStack, level);
                } else {
                    Optional<Modifier> modifier = ModManager.getModifier(pieces[0]);

                    modifier.ifPresent(mod -> {
                        ModManager.applyModifier(itemStack, mod, true, true, level);
                    });
                }
            }

            player.getInventory().offer(itemStack);
            src.sendMessage(Text.of(TextColors.GREEN, "Item created!"));
        });

        return CommandResult.success();
    }

}
