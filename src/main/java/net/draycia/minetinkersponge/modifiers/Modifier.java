package net.draycia.minetinkersponge.modifiers;

import net.draycia.minetinkersponge.data.MTKeys;
import net.draycia.minetinkersponge.data.impl.ModifierIdentifierData;
import ninja.leaping.configurate.ConfigurationNode;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.enchantment.EnchantmentType;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.recipe.crafting.CraftingRecipe;
import org.spongepowered.api.text.Text;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public abstract class Modifier {

    /**
     *
     * @return The display friendly name of this modifier
     */
    public abstract String getName();

    /**
     *
     * @return The highest level this modifier can be on items
     */
    public abstract int getMaxLevel();

    /**
     *
     * @return An int describing the value of the modifier, used to determine effective level and combat level
     */
    public abstract int getLevelWeight();

    /**
     * Returns the number of slots the item must have and will be reduced by when the modifier is applied.
     * May return different slot costs depending on the modifier level, may also return the same cost for all levels.
     * @return The number of slots the modifier costs at the given level.
     */
    public int getModifierSlotCost(int modifierLevel) {
        return 1;
    }

    /**
     *
     * @return The chance that the modifier will successfully apply, in a range of 0-100.
     */
    public int getApplicationChance() {
        return 100;
    }

    /**
     *
     * @return A string describing the modifier
     */
    public String getDescription() {
        return "";
    }

    /**
     *
     * @return A string able to be used to obtain the modifier in commands and in the API
     */
    public String getKey() {
        return getName().replace(" ", "-").replace("'", "").toLowerCase();
    }

    /**
     * A list of {@link ItemType ItemTypes} that the modifier can be applied to.
     * @return A list of compatible ItemTypes. Or null if the modifier can go on any item.
     */
    public abstract List<ItemType> getCompatibleItems();

    public String getCompatibilityString() {
        return "Everything";
    }

    /**
     * @return The item type used for creation of the modifier item.
     */
    public abstract ItemType getModifierItemType();

    /**
     * @return The list of modifiers that this one is incompatible with.
     */
    public List<Class<? extends Modifier>> getIncompatibleModifiers() {
        return Collections.emptyList();
    }

    /**
     * Called when the modifier is applied to an item
     * @param itemStack The item the modifier is being applied to
     * @param level The new level of the modifier
     * @return The new item stack after the modifier is applied and changes are made.
     */
    public ItemStack onModifierApplication(ItemStack itemStack, int level) {
        return itemStack;
    }

    /**
     * Called when the configuration for the modifier is being created and/or saved.
     * @param modifierNode The ConfigurationNode that extra options should be saved to
     */
    public void onConfigurationSave(ConfigurationNode modifierNode) {}

    /**
     * Called when the configuration for the modifier is being loaded and read from.
     * @param modifierNode The ConfigurationNode that extra options should be obtained from
     */
    public void onConfigurationLoad(ConfigurationNode modifierNode) {}

    /**
     * Called when the {@link ModManager} successfully registers the modifier.
     * This should be used for registering event listeners instead of in the constructor.
     * @param plugin The instance of the plugin that owns the modifier that's being registered.
     */
    public void onModifierRegister(Object plugin) {}

    public Optional<CraftingRecipe> getRecipe() {
        return Optional.empty();
    }

    /**
     * @return The enchantment the modifier applies to items. Null if one isn't applied.
     */
    public EnchantmentType getAppliedEnchantment() {
        return null;
    }

    /**
     *
     * @return An item with a stack size of 1 used to apply the modifier to items
     */
    public ItemStack getModifierItem() {
        return getModifierItem(1);
    }

    /**
     *
     * @param amount The stack size (amount of the item) to return
     * @return An item used to apply the modifier to items
     */
    public ItemStack getModifierItem(int amount) {
        ItemStack itemStack = ItemStack.builder()
                .itemType(getModifierItemType())
                .quantity(amount)
                .build();

        itemStack.offer(itemStack.getOrCreate(ModifierIdentifierData.class).get());
        itemStack.offer(MTKeys.MODIFIER_ID, getKey());

        itemStack.offer(Keys.DISPLAY_NAME, Text.of(getName() + " Modifier"));

        return itemStack;
    }
}
