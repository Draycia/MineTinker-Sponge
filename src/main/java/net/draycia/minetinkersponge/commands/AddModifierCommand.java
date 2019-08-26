package net.draycia.minetinkersponge.commands;

import net.draycia.minetinkersponge.utils.StringUtils;
import net.draycia.minetinkersponge.data.MTKeys;
import net.draycia.minetinkersponge.data.interfaces.MineTinkerItemModsData;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.Collections;
import java.util.Optional;

public class AddModifierCommand implements CommandExecutor {

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) {
        if (!(src instanceof Player)) {
            return CommandResult.empty();
        }

        Player player = (Player)src;

        Optional<String> modifier = args.getOne("modifier");

        Optional<ItemStack> mainItem = player.getItemInHand(HandTypes.MAIN_HAND);

        if (mainItem.isPresent() && modifier.isPresent()) {
            if (mainItem.get().get(MTKeys.IS_MINETINKER).isPresent()) {
                MineTinkerItemModsData data = mainItem.get().getOrCreate(MineTinkerItemModsData.class).get();

                if (data.get(modifier.get()).isPresent()) {
                    data.put(modifier.get(), data.get(modifier.get()).get() + 1);
                } else {
                    data.put(modifier.get(), 1);
                }

                Text lore = Text.builder()
                        .append(Text.of(modifier.get() + StringUtils.toRomanNumerals(data.get(modifier.get()).get())))
                        .color(TextColors.GRAY)
                        .build();

                mainItem.get().offer(Keys.ITEM_LORE, Collections.singletonList(lore));
            }
        }

        return CommandResult.success();
    }
}
