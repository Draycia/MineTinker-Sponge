package net.draycia.minetinkersponge.modifiers;

import net.draycia.minetinkersponge.data.MTKeys;
import net.draycia.minetinkersponge.data.interfaces.*;
import net.draycia.minetinkersponge.utils.StringUtils;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.*;

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
            rewriteItemLore(itemStack);

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

        if (!itemStack.offer(itemStack.getOrCreate(MineTinkerItemSlotData.class).get()).isSuccessful()) {
            System.out.println("Cannot offer MineTinkerItemIntegerData!");
        }

        if (!itemStack.offer(itemStack.getOrCreate(MineTinkerItemXPData.class).get()).isSuccessful()) {
            System.out.println("Cannot offer MineTinkerItemXPData!");
        }

        if (!itemStack.offer(itemStack.getOrCreate(MineTinkerItemLevelData.class).get()).isSuccessful()) {
            System.out.println("Cannot offer MineTinkerItemLevelData!");
        }

        if (!itemStack.offer(MTKeys.IS_MINETINKER, true).isSuccessful()) {
            System.out.println("Cannot offer MTKeys.IS_MINETINKER!");
            return false;
        }

        if (!itemStack.offer(MTKeys.IS_MT_TOOL, true).isSuccessful()) {
            System.out.println("Cannot offer MTKeys.IS_MT_TOOL!");
            return false;
        }

        if (!itemStack.offer(MTKeys.MINETINKER_XP, 0).isSuccessful()) {
            System.out.println("Cannot offer MTKeys.MINETINKER_XP!");
            return false;
        }

        if (!itemStack.offer(MTKeys.MINETINKER_LEVEL, 1).isSuccessful()) {
            System.out.println("Cannot offer MTKeys.MINETINKER_LEVEL!");
            return false;
        }

        if (!itemStack.offer(MTKeys.MINETINKER_SLOTS, 1).isSuccessful()) {
            System.out.println("Cannot offer MTKeys.MINETINKER_SLOTS!");
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

    public void rewriteItemLore(ItemStack itemStack) {
        Map<String, Integer> itemModifierLevels = getItemModifierLevels(itemStack);

        ArrayList<Text> lore = new ArrayList<>();

        for (Map.Entry<String, Integer> entry : itemModifierLevels.entrySet()) {
            Optional<Modifier> mod = getModifier(entry.getKey());

            if (mod.isPresent()) {
                lore.add(Text.builder().append(Text.of(mod.get().getName() + " " + StringUtils.toRomanNumerals(entry.getValue())))
                        .color(TextColors.GRAY).build());
            }
        }

        Optional<Integer> level = itemStack.get(MTKeys.MINETINKER_LEVEL);

        if (level.isPresent()) {
            lore.add(Text.builder()
                    .append(Text.builder().append(Text.of("Item Level: ")).color(TextColors.GOLD).build())
                    .append(Text.builder().append(Text.of(level.get())).color(TextColors.WHITE).build())
                    .build());
        }

        Optional<Integer> experience = itemStack.get(MTKeys.MINETINKER_XP);

        if (experience.isPresent()) {
            double required = getExperienceRequiredToLevel(itemStack);

            lore.add(Text.builder()
                    .append(Text.builder().append(Text.of("Item XP: ")).color(TextColors.GOLD).build())
                    .append(Text.builder().append(Text.of(experience.get())).color(TextColors.WHITE).build())
                    .append(Text.builder().append(Text.of(" / ")).color(TextColors.WHITE).build())
                    .append(Text.builder().append(Text.of(String.valueOf(required).split("\\.")[0])).color(TextColors.WHITE).build())
                    .build());
        }

        Optional<Integer> slots = itemStack.get(MTKeys.MINETINKER_SLOTS);

        if (slots.isPresent()) {
            lore.add(Text.builder()
                    .append(Text.builder().append(Text.of("Mod Slots: ")).color(TextColors.GOLD).build())
                    .append(Text.builder().append(Text.of(slots.get())).color(TextColors.WHITE).build())
                    .build());
        }

        itemStack.offer(Keys.ITEM_LORE, lore);
    }

    public void addExperience(ItemStack itemStack, int experience) {
        Optional<Integer> current = itemStack.get(MTKeys.MINETINKER_XP);

        if (current.isPresent()) {
            if (current.get() >= getExperienceRequiredToLevel(itemStack)) {
                itemStack.offer(MTKeys.MINETINKER_XP, 0);
                incrementItemLevel(itemStack);
            } else {
                itemStack.offer(MTKeys.MINETINKER_XP, current.get() + experience);
            }
        } else {
            itemStack.offer(MTKeys.MINETINKER_XP, experience);
        }

        rewriteItemLore(itemStack);
    }

    public void incrementItemLevel(ItemStack itemStack) {
        Optional<Integer> level = itemStack.get(MTKeys.MINETINKER_LEVEL);

        if (level.isPresent()) {
            itemStack.offer(MTKeys.MINETINKER_LEVEL, level.get() + 1);
        } else {
            itemStack.offer(MTKeys.MINETINKER_LEVEL, 1);
        }

        incrementItemModifierSlots(itemStack);
    }

    public void incrementItemModifierSlots(ItemStack itemStack) {
        Optional<Integer> slots = itemStack.get(MTKeys.MINETINKER_SLOTS);

        if (slots.isPresent()) {
            itemStack.offer(MTKeys.MINETINKER_SLOTS, slots.get() + 1);
        } else {
            itemStack.offer(MTKeys.MINETINKER_SLOTS, 1);
        }
    }

    public double getExperienceRequiredToLevel(ItemStack itemStack) {
        Optional<Integer> level = itemStack.get(MTKeys.MINETINKER_LEVEL);

        if (level.isPresent()) {
            return 100 * (Math.pow(2, level.get() - 1));
        }

        return 0;
    }
}
