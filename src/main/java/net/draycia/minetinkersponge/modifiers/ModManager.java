package net.draycia.minetinkersponge.modifiers;

import net.draycia.minetinkersponge.data.MTKeys;
import net.draycia.minetinkersponge.data.interfaces.*;
import net.draycia.minetinkersponge.utils.ItemTypeUtils;
import net.draycia.minetinkersponge.utils.StringUtils;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.item.enchantment.Enchantment;
import org.spongepowered.api.item.enchantment.EnchantmentType;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.*;

public class ModManager {

    private HashMap<String, Modifier> modifiers = new HashMap<>();

    /**
     * Registers the modifier and adds it to MineTinker's internal modifier map
     * @param plugin The plugin that's registering the modifier
     * @param modifier The modifier instance to register
     * @return If the registration was a success. My return false if a modifier with the key already exists.
     */
    public boolean registerModifier(Object plugin, Modifier modifier) {
         if (modifiers.containsKey(modifier.getKey())) {
             return false;
         }

         modifiers.put(modifier.getKey(), modifier);

         modifier.onModifierRegister(plugin);

         return true;
    }

    /**
     * Gets the modifier with the matching key
     * @param key The key of the modifier
     * @return The modifier
     */
    public Optional<Modifier> getModifier(String key) {
        return Optional.ofNullable(modifiers.get(key));
    }

    /**
     *
     * @param itemStack
     * @param modifier
     * @return
     */
    public boolean itemHasModifier(ItemStack itemStack, Modifier modifier) {
        if (itemStack.get(MTKeys.IS_MINETINKER).isPresent()) {
            Optional<Map<String, Integer>> modifiers = itemStack.get(MTKeys.ITEM_MODIFIERS);

            if (modifiers.isPresent()) {
                return modifiers.get().containsKey(modifier.getKey());
            }
        }

        return false;
    }

    /**
     *
     * @param itemStack
     * @param modifier
     * @return
     */
    public boolean applyModifier(ItemStack itemStack, Modifier modifier, boolean ignoreSlots) {
        if (itemStack.get(MTKeys.IS_MINETINKER).isPresent()) {
            if (modifier.getCompatibleItems() != null && !modifier.getCompatibleItems().contains(itemStack.getType())) {
                return false;
            }

            if (!ignoreSlots && getItemModifierSlots(itemStack) < 1) {
                return false;
            }

            if (getModifierLevel(itemStack, modifier) >= modifier.getMaxLevel()) {
                return false;
            }

            for (EnchantmentType type : modifier.getAppliedEnchantments()) {
                System.out.println("This is called");
                Optional<List<Enchantment>> enchantments = itemStack.get(Keys.ITEM_ENCHANTMENTS);
                int level = getModifierLevel(itemStack, modifier);

                if (enchantments.isPresent()) {
                    System.out.println("This is also called");
                    List<Enchantment> enchantmentList = enchantments.get();
                    enchantmentList.add(Enchantment.builder().type(type).level(level).build());
                    itemStack.offer(Keys.ITEM_ENCHANTMENTS, enchantmentList);
                }
            }

            setModifierLevel(itemStack, modifier, getModifierLevel(itemStack, modifier) - 1);

            if (!ignoreSlots) {
                setItemModifierSlots(itemStack, getItemModifierSlots(itemStack) - 1);
            }

            rewriteItemLore(itemStack);

            return true;
        }

        return false;
    }

    /**
     *
     * @param itemStack
     * @param modifier
     * @param amount
     * @return
     */
    public boolean applyModifier(ItemStack itemStack, Modifier modifier, boolean ignoreSlots, int amount) {
        if (modifier.getCompatibleItems() != null && !modifier.getCompatibleItems().contains(itemStack.getType())) {
            return false;
        }

        if (!ignoreSlots && getItemModifierSlots(itemStack) < amount) {
            return false;
        }

        if (getModifierLevel(itemStack, modifier) + amount > modifier.getMaxLevel()) {
            return false;
        }

        for (EnchantmentType type : modifier.getAppliedEnchantments()) {
            Optional<List<Enchantment>> enchantments = itemStack.get(Keys.ITEM_ENCHANTMENTS);
            int level = getModifierLevel(itemStack, modifier) + amount;

            if (enchantments.isPresent()) {
                List<Enchantment> enchantmentList = enchantments.get();
                enchantmentList.add(Enchantment.builder().type(type).level(level).build());
                itemStack.offer(Keys.ITEM_ENCHANTMENTS, enchantmentList);
            }
        }

        setModifierLevel(itemStack, modifier, getModifierLevel(itemStack, modifier) + amount);

        if (!ignoreSlots) {
            setItemModifierSlots(itemStack, getItemModifierSlots(itemStack) - amount);
        }

        rewriteItemLore(itemStack);

        return true;
    }

    /**
     *
     * @param itemStack
     */
    public void convertItemStack(ItemStack itemStack) {
        if (!ItemTypeUtils.getAllTypes().contains(itemStack.getType())) {
            return;
        }

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
        }

        if (!itemStack.offer(MTKeys.IS_MT_TOOL, true).isSuccessful()) {
            System.out.println("Cannot offer MTKeys.IS_MT_TOOL!");
        }

        if (!itemStack.offer(MTKeys.MINETINKER_XP, 0).isSuccessful()) {
            System.out.println("Cannot offer MTKeys.MINETINKER_XP!");
        }

        if (!itemStack.offer(MTKeys.MINETINKER_LEVEL, 1).isSuccessful()) {
            System.out.println("Cannot offer MTKeys.MINETINKER_LEVEL!");
        }

        if (!itemStack.offer(MTKeys.MINETINKER_SLOTS, 1).isSuccessful()) {
            System.out.println("Cannot offer MTKeys.MINETINKER_SLOTS!");
        }

        itemStack.offer(Keys.HIDE_ENCHANTMENTS, true);
        itemStack.offer(Keys.UNBREAKABLE, true);

        rewriteItemLore(itemStack);
    }

    /**
     *
     * @param itemStack
     * @param modifier
     * @return
     */
    public int getModifierLevel(ItemStack itemStack, Modifier modifier) {
        return getItemModifierLevels(itemStack).getOrDefault(modifier.getKey(), 0);
    }

    /**
     *
     * @param itemStack
     * @param modifier
     * @param amount
     */
    public void setModifierLevel(ItemStack itemStack, Modifier modifier, int amount) {
        Map<String, Integer> itemModifierLevels = getItemModifierLevels(itemStack);

        if (itemModifierLevels == null) {
            return;
        }

        itemModifierLevels.put(modifier.getKey(), amount);

        itemStack.offer(MTKeys.ITEM_MODIFIERS, itemModifierLevels);
    }

    /**
     *
     * @param itemStack
     * @return
     */
    public Map<String, Integer> getItemModifierLevels(ItemStack itemStack) {
        return itemStack.get(MTKeys.ITEM_MODIFIERS).orElse(Collections.emptyMap());
    }

    /**
     *
     * @param itemStack
     */
    public void rewriteItemLore(ItemStack itemStack) {
        Map<String, Integer> itemModifierLevels = getItemModifierLevels(itemStack);

        ArrayList<Text> lore = new ArrayList<>();

        long itemLevel = 0;

        for (Map.Entry<String, Integer> entry : itemModifierLevels.entrySet()) {
            Optional<Modifier> mod = getModifier(entry.getKey());

            if (mod.isPresent()) {
                Modifier modifier = mod.get();

                itemLevel += modifier.getLevelWeight() * entry.getValue();

                lore.add(Text.builder().append(Text.of(modifier.getName() + " " + StringUtils.toRomanNumerals(entry.getValue())))
                        .color(TextColors.GRAY).build());
            }
        }

        lore.add(Text.builder()
                .append(Text.builder().append(Text.of("Effective Level: ")).color(TextColors.GOLD).build())
                .append(Text.builder().append(Text.of(itemLevel)).color(TextColors.WHITE).build())
                .build());

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

    /**
     *
     * @param itemStack
     * @param experience
     */
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

    /**
     *
     * @param itemStack
     */
    public void incrementItemLevel(ItemStack itemStack) {
        Optional<Integer> level = itemStack.get(MTKeys.MINETINKER_LEVEL);

        if (level.isPresent()) {
            itemStack.offer(MTKeys.MINETINKER_LEVEL, level.get() + 1);
        } else {
            itemStack.offer(MTKeys.MINETINKER_LEVEL, 1);
        }

        incrementItemModifierSlots(itemStack);
    }

    /**
     *
     * @param itemStack
     */
    public void incrementItemModifierSlots(ItemStack itemStack) {
        Optional<Integer> slots = itemStack.get(MTKeys.MINETINKER_SLOTS);

        if (slots.isPresent()) {
            itemStack.offer(MTKeys.MINETINKER_SLOTS, slots.get() + 1);
        } else {
            itemStack.offer(MTKeys.MINETINKER_SLOTS, 1);
        }
    }

    /**
     *
     * @param itemStack
     * @param level
     */
    public void setItemModifierSlots(ItemStack itemStack, int level) {
        itemStack.offer(MTKeys.MINETINKER_SLOTS, level);
    }

    /**
     *
     * @param itemStack
     * @return
     */
    public int getItemModifierSlots(ItemStack itemStack) {
        return itemStack.get(MTKeys.MINETINKER_SLOTS).orElse(0);
    }

    /**
     *
     * @param itemStack
     * @return
     */
    public double getExperienceRequiredToLevel(ItemStack itemStack) {
        Optional<Integer> level = itemStack.get(MTKeys.MINETINKER_LEVEL);

        if (level.isPresent()) {
            return 100 * (Math.pow(2, level.get() - 1));
        }

        return 0;
    }
}
