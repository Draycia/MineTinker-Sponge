package net.draycia.minetinkersponge.modifiers;

import net.draycia.minetinkersponge.data.MTKeys;
import net.draycia.minetinkersponge.data.impl.*;
import net.draycia.minetinkersponge.utils.ItemTypeUtils;
import net.draycia.minetinkersponge.utils.MTConfig;
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
        // Only allow one modifier to have the same key, first come first serve.
         if (modifiers.containsKey(modifier.getKey())) {
             return false;
         }

         modifiers.put(modifier.getKey(), modifier);

         // Allow the modifier to register event listeners etc when successfully registered.
         // Passes in the plugin instance the method caller supplies.
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
     */
    public void removeModifier(ItemStack itemStack, Modifier modifier) {
        Map<String, Integer> modifiers = getItemModifierLevels(itemStack);

        modifiers.remove(modifier.getKey());

        itemStack.offer(MTKeys.ITEM_MODIFIERS, modifiers);
    }

    /**
     *
     * @param itemStack
     * @param modifier
     * @param amount
     * @return
     */
    public ModifierApplicationResult applyModifier(ItemStack itemStack, Modifier modifier, boolean ignoreSlots, int amount) {
        // Check if the modifier is compatible with the item
        if (modifier.getCompatibleItems() != null && !modifier.getCompatibleItems().contains(itemStack.getType())) {
            return new ModifierApplicationResult(null);
        }

        // Check if the item has enough modifier slots
        if (!ignoreSlots && getItemModifierSlots(itemStack) < amount) {
            return new ModifierApplicationResult(null);
        }

        // Ensure the modifier level doesn't exceed the modifier's level cap
        int targetLevel = getModifierLevel(itemStack, modifier) + amount;

        if (targetLevel > modifier.getMaxLevel()) {
            return new ModifierApplicationResult(null);
        }

        // Check if the item has any modifiers that are incompatible with the one being applied
        for (Modifier appliedModifier : getItemAppliedModifiers(itemStack)) {
            for (Class<? extends Modifier> modClass : modifier.getIncompatibleModifiers()) {
                if (appliedModifier.getClass() == modClass) {
                    return new ModifierApplicationResult(null);
                }
            }
        }

        // If the modifier applies any enchantments to the item, do so
        for (EnchantmentType type : modifier.getAppliedEnchantments()) {
            Optional<List<Enchantment>> enchantments = itemStack.get(Keys.ITEM_ENCHANTMENTS);
            int level = getModifierLevel(itemStack, modifier) + amount;

            List<Enchantment> enchantmentList = enchantments.orElseGet(ArrayList::new);
            enchantmentList.add(Enchantment.builder().type(type).level(level).build());

            itemStack.offer(Keys.ITEM_ENCHANTMENTS, enchantmentList);
        }

        // Sets the level of the modifier on the item
        setModifierLevel(itemStack, modifier, getModifierLevel(itemStack, modifier) + amount);

        // Modifies the item's modifier slots
        if (!ignoreSlots) {
            setItemModifierSlots(itemStack, getItemModifierSlots(itemStack) - amount);
        }

        // And update its lore
        rewriteItemLore(itemStack);

        // Toss back the result of the item
        ItemStack newItem = modifier.onModifierApplication(itemStack, targetLevel);

        return new ModifierApplicationResult(newItem);
    }

    /**
     *
     * @param itemStack
     */
    public void convertItemStack(ItemStack itemStack, boolean canExceedMaxLevel) {
        // Check if the item is compatible with the plugin
        if (!ItemTypeUtils.getAllTypes().contains(itemStack.getType())) {
            return;
        }

        // Data Updaters need to be offered before the data is
        itemStack.offer(itemStack.getOrCreate(ItemCompatibleData.class).get());
        itemStack.offer(itemStack.getOrCreate(ItemExperienceData.class).get());
        itemStack.offer(itemStack.getOrCreate(ItemLevelData.class).get());
        itemStack.offer(itemStack.getOrCreate(ItemModifierListData.class).get());
        itemStack.offer(itemStack.getOrCreate(ModifierSlotData.class).get());

        // Offer the actual data
        itemStack.offer(MTKeys.IS_MINETINKER, true);
        itemStack.offer(MTKeys.IS_MT_TOOL, true);
        itemStack.offer(MTKeys.MINETINKER_XP, 0);
        itemStack.offer(MTKeys.MINETINKER_LEVEL, 1);
        itemStack.offer(MTKeys.MINETINKER_SLOTS, 1);

        // Optionally hide enchantments
        if (MTConfig.HIDE_ENCHANTMENTS) {
            itemStack.offer(Keys.HIDE_ENCHANTMENTS, true);
        }

        // Optionally make item unbreakable
        if (MTConfig.MAKE_UNBREAKABLE) {
            itemStack.offer(Keys.UNBREAKABLE, true);
        }

        // Convert vanilla enchantments into modifiers
        if (MTConfig.CONVERT_TRANSFERS_ENCHANTMENTS || canExceedMaxLevel) {
            Optional<List<Enchantment>> enchantments = itemStack.get(Keys.ITEM_ENCHANTMENTS);

            if (enchantments.isPresent()) {
                for (Enchantment enchantment : enchantments.get()) {
                    Optional<Modifier> modifier = getFirstModifierByEnchantment(enchantment.getType());

                    if (modifier.isPresent()) {
                        int level;

                        if (MTConfig.CONVERT_EXCEEDS_MAX_LEVEL) {
                            level = enchantment.getLevel();
                        } else {
                            level = Math.min(enchantment.getLevel(), modifier.get().getMaxLevel());
                        }

                        applyModifier(itemStack, modifier.get(), true, level);
                    }
                }
            }
        }

        // And update the lore
        rewriteItemLore(itemStack);
    }

    public Optional<Modifier> getFirstModifierByEnchantment(EnchantmentType enchantment) {
        for (Modifier modifier : modifiers.values()) {
            if (modifier.getAppliedEnchantments().contains(enchantment)) {
                return Optional.of(modifier);
            }
        }

        return Optional.empty();
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

        itemModifierLevels.put(modifier.getKey(), amount);

        itemStack.offer(MTKeys.ITEM_MODIFIERS, itemModifierLevels);
    }

    /**
     *
     * @param itemStack
     * @return
     */
    public Map<String, Integer> getItemModifierLevels(ItemStack itemStack) {
        return itemStack.get(MTKeys.ITEM_MODIFIERS).orElse(new HashMap<>());
    }

    public List<Modifier> getItemAppliedModifiers(ItemStack itemStack) {
        ArrayList<Modifier> modifiers = new ArrayList<>();

        for (Map.Entry<String, Integer> entry : getItemModifierLevels(itemStack).entrySet()) {
            modifiers.add(getModifier(entry.getKey()).orElseThrow(NullPointerException::new));
        }

        return modifiers;
    }

    /**
     *
     * @param itemStack The item to have its lore updated
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
     * @param itemStack The item to give experience to
     * @param experience The amount of experience to give.
     */
    public void addExperience(ItemStack itemStack, int experience) {
        Optional<Integer> current = itemStack.get(MTKeys.MINETINKER_XP);

        if (current.isPresent()) {
            if (current.get() >= getExperienceRequiredToLevel(itemStack)) {
                if (incrementItemLevel(itemStack)) {
                    itemStack.offer(MTKeys.MINETINKER_XP, 0);
                }
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
     * @param itemStack The item to increment the level of
     */
    public boolean incrementItemLevel(ItemStack itemStack) {
        Optional<Integer> level = itemStack.get(MTKeys.MINETINKER_LEVEL);

        if (level.isPresent()) {
            if (MTConfig.GLOBAL_MAX_LEVEL > 0) {
                if (level.get() >= MTConfig.GLOBAL_MAX_LEVEL) {
                    return false;
                }
            }

            itemStack.offer(MTKeys.MINETINKER_LEVEL, level.get() + 1);
        } else {
            itemStack.offer(MTKeys.MINETINKER_LEVEL, 1);
        }

        incrementItemModifierSlots(itemStack);

        return true;
    }

    /**
     *
     * @param itemStack The item to increment the modifier slot amount of
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
     * Sets the amount of modifier slots
     * @param itemStack The item to set the modifier slot count of
     * @param level The amount of slots the item will have
     */
    public void setItemModifierSlots(ItemStack itemStack, int level) {
        itemStack.offer(MTKeys.MINETINKER_SLOTS, level);
    }

    /**
     *
     * @param itemStack The item to get the number of slots from
     * @return The amount of slots the item has
     */
    public int getItemModifierSlots(ItemStack itemStack) {
        return itemStack.get(MTKeys.MINETINKER_SLOTS).orElse(0);
    }

    /**
     *
     * @param itemStack The item to get the remaining experience from
     * @return The amount of experience the item needs to get in order to level up
     */
    public double getExperienceRequiredToLevel(ItemStack itemStack) {
        Optional<Integer> level = itemStack.get(MTKeys.MINETINKER_LEVEL);

        if (level.isPresent()) {
            return 100 * (Math.pow(2, level.get() - 1));
        }

        return 0;
    }
}
