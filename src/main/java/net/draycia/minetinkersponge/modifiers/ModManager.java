package net.draycia.minetinkersponge.modifiers;

import net.draycia.minetinkersponge.MineTinkerSponge;
import net.draycia.minetinkersponge.data.MTKeys;
import net.draycia.minetinkersponge.data.interfaces.MineTinkerItemModsData;
import net.draycia.minetinkersponge.utils.StringUtils;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ModManager {

    private HashMap<String, Modifier> modifiers = new HashMap<>();

    public boolean registerModifier(Object plugin, Modifier modifier) {
         if (modifiers.containsKey(modifier.getKey())) {
             return false;
         }

         modifiers.put(modifier.getKey(), modifier);

         modifier.onModifierRegister(plugin);

         return true;
    }

    public static boolean itemHasModifier(ItemStack itemStack, Modifier modifier) {
        if (itemStack.get(MTKeys.IS_MINETINKER).isPresent()) {
            Optional<Map<String, Integer>> modifiers = itemStack.get(MTKeys.ITEM_MODIFIERS);

            if (modifiers.isPresent()) {
                return modifiers.get().containsKey(modifier.getKey());
            }
        }

        return false;
    }
    
    public static boolean applyModifier(ItemStack itemStack, Modifier modifier) {
        if (itemStack.get(MTKeys.IS_MINETINKER).isPresent()) {
            if (modifier.getCompatibleItems() != null && !modifier.getCompatibleItems().contains(itemStack.getType())) {
                return false;
            }

            MineTinkerItemModsData data = itemStack.getOrCreate(MineTinkerItemModsData.class).get();

            if (data.get(modifier.getKey()).isPresent()) {
                data.put(modifier.getKey(), data.get(modifier.getKey()).get() + 1);
            } else {
                data.put(modifier.getKey(), 1);
            }

            Text.Builder lore = Text.builder();

            for (Map.Entry<String, Integer> entry : data.getMapValues()) {
                lore = lore.append(Text.of(entry.getKey() + StringUtils.toRomanNumerals(entry.getValue())))
                        .color(TextColors.GRAY);
            }

            itemStack.offer(Keys.ITEM_LORE, Collections.singletonList(lore.build()));

            return true;
        }

        return false;
    }
}
