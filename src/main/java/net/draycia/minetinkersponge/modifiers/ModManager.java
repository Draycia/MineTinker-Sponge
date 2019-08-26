package net.draycia.minetinkersponge.modifiers;

import net.draycia.minetinkersponge.MineTinkerSponge;
import net.draycia.minetinkersponge.data.MTKeys;
import net.draycia.minetinkersponge.data.interfaces.IsMineTinkerArmorData;
import net.draycia.minetinkersponge.data.interfaces.IsMineTinkerData;
import net.draycia.minetinkersponge.data.interfaces.IsMineTinkerToolData;
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

    public Optional<Modifier> getModifier(String key) {
        return Optional.ofNullable(modifiers.get(key));
    }

    public boolean itemHasModifier(ItemStack itemStack, Modifier modifier) {
        if (itemStack.get(MTKeys.IS_MINETINKER).isPresent()) {
            Optional<Map<String, Integer>> modifiers = itemStack.get(MTKeys.ITEM_MODIFIERS);

            if (modifiers.isPresent()) {
                return modifiers.get().containsKey(modifier.getKey());
            }
        }

        return false;
    }
    
    public boolean applyModifier(ItemStack itemStack, Modifier modifier) {
        if (itemStack.get(MTKeys.IS_MINETINKER).isPresent()) {
            if (modifier.getCompatibleItems() != null && !modifier.getCompatibleItems().contains(itemStack.getType())) {
                return false;
            }

            if (getModifierLevel(itemStack, modifier) >= modifier.getMaxLevel()) {
                return false;
            }

            incrementModifierLevel(itemStack, modifier);

            Map<String, Integer> itemModifierLevels = getItemModifierLevels(itemStack);

            Text.Builder lore = Text.builder();

            for (Map.Entry<String, Integer> entry : itemModifierLevels.entrySet()) {
                Optional<Modifier> mod = getModifier(entry.getKey());

                if (mod.isPresent()) {
                    lore = lore.append(Text.of(mod.get().getName() + " " + StringUtils.toRomanNumerals(entry.getValue())))
                            .color(TextColors.GRAY);
                }
            }

            itemStack.offer(Keys.ITEM_LORE, Collections.singletonList(lore.build()));

            return true;
        }

        return false;
    }

    public boolean convertItemStack(ItemStack itemStack) {
        if (!itemStack.offer(itemStack.getOrCreate(IsMineTinkerData.class).get()).isSuccessful()) {
            System.out.println("Cannot offer IsMineTinkerData!");
        }

        if (!itemStack.offer(itemStack.getOrCreate(IsMineTinkerToolData.class).get()).isSuccessful()) {
            System.out.println("Cannot offer IsMineTinkerToolData!");
        }

        if (!itemStack.offer(itemStack.getOrCreate(IsMineTinkerArmorData.class).get()).isSuccessful()) {
            System.out.println("Cannot offer IsMineTinkerArmorData!");
        }

        if (!itemStack.offer(itemStack.getOrCreate(MineTinkerItemModsData.class).get()).isSuccessful()) {
            System.out.println("Cannot offer MineTinkerItemModsData!");
        }

        if (!itemStack.offer(MTKeys.IS_MINETINKER, true).isSuccessful()) {
            System.out.println("Cannot offer MTKeys.IS_MINETINKER!");
            return false;
        }

        if (!itemStack.offer(MTKeys.IS_MT_TOOL, true).isSuccessful()) {
            System.out.println("Cannot offer MTKeys.IS_MT_TOOL!");
            return false;
        }

        return true;
    }

    public int getModifierLevel(ItemStack itemStack, Modifier modifier) {
        return getItemModifierLevels(itemStack).getOrDefault(modifier.getKey(), 0);
    }

    public void incrementModifierLevel(ItemStack itemStack, Modifier modifier) {
        Map<String, Integer> itemModifierLevels = getItemModifierLevels(itemStack);

        if (itemModifierLevels.get(modifier.getKey()) != null) {
            itemModifierLevels.put(modifier.getKey(), itemModifierLevels.get(modifier.getKey()) + 1);
        } else {
            itemModifierLevels.put(modifier.getKey(), 1);
        }

        itemStack.offer(MTKeys.ITEM_MODIFIERS, itemModifierLevels);
    }

    public Map<String, Integer> getItemModifierLevels(ItemStack itemStack) {
        return itemStack.getOrCreate(MineTinkerItemModsData.class).get().asMap();
    }
}
