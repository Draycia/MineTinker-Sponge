package net.draycia.minetinkersponge.utils;

import com.mcsimonflash.sponge.teslalibs.inventory.Element;
import com.mcsimonflash.sponge.teslalibs.inventory.Layout;
import com.mcsimonflash.sponge.teslalibs.inventory.View;
import net.draycia.minetinkersponge.MineTinkerSponge;
import net.draycia.minetinkersponge.modifiers.Modifier;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.enchantment.EnchantmentType;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.property.InventoryDimension;
import org.spongepowered.api.item.inventory.property.InventoryTitle;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.TranslatableText;
import org.spongepowered.api.text.format.TextColors;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class InventoryGUIManager {

    private View view;

    public InventoryGUIManager(MineTinkerSponge mineTinkerSponge) {
        Layout.Builder layout = Layout.builder()
                .dimension(InventoryDimension.of(9, 6));

        int index = 0;

        for (Map.Entry<String, Modifier> entry : mineTinkerSponge.getModManager().getAllModifiers().entrySet()) {
            Modifier modifier = entry.getValue();

            ItemStack itemStack = ItemStack.of(modifier.getModifierItemType());

            List<Text> lore = new ArrayList<>();

            lore.add(Text.of(""));

            if (!modifier.getDescription().isEmpty()) {
                for (String line : StringUtils.splitString(modifier.getDescription(), 30)) {
                    lore.add(Text.of(line));
                }
            }

            lore.add(Text.of(""));

            lore.add(Text.of(TextColors.GOLD, "Max Level: ", TextColors.WHITE, modifier.getMaxLevel()));
            lore.add(Text.of(""));
            lore.add(Text.of(TextColors.BLUE, "Applicable On: ", TextColors.WHITE, modifier.getCompatibilityString()));

            if (modifier.getAppliedEnchantment() != null) {
                lore.add(Text.of(""));
                lore.add(Text.of(TextColors.YELLOW, "Applied Enchantments: ", TextColors.WHITE,
                        TranslatableText.of(modifier.getAppliedEnchantment().getTranslation())));
            }

            itemStack.offer(Keys.ITEM_LORE, lore);
            itemStack.offer(Keys.DISPLAY_NAME, Text.of(TextColors.GREEN, modifier.getName()));

            layout = layout.set(Element.of(itemStack), index);

            index += 1;
        }

        view = View.builder()
                .property(InventoryTitle.of(Text.of(TextColors.GOLD, "MineTinker Modifiers")))
                .build(mineTinkerSponge.getContainer());

        view.define(layout.build());
    }

    public void showViewToPlayer(Player player) {
        view.open(player);
    }

}
