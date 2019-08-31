package net.draycia.minetinkersponge.modifiers;

import net.draycia.minetinkersponge.data.MTKeys;
import net.draycia.minetinkersponge.data.interfaces.MineTinkerItemModsData;
import net.draycia.minetinkersponge.data.interfaces.MineTinkerModifierIDData;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.enchantment.Enchantment;
import org.spongepowered.api.item.enchantment.EnchantmentType;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;

import java.util.Collections;
import java.util.List;

public abstract class Modifier {

    public abstract String getName();
    public abstract int getMaxLevel();

    public String getKey() {
        return getName().replace(" ", "-").replace("'", "").toLowerCase();
    }

    /**
     * A list of {@link ItemType ItemTypes} that the modifier can be applied to.
     * @return A list of compatible ItemTypes. Or null if the modifier can go on any item.
     */
    public abstract List<ItemType> getCompatibleItems();

    /**
     * @return The item type used for creattion of the modifier item.
     */
    public abstract ItemType getModifierItemType();

    /**
     * Called when the {@link ModManager} successfully registers the modifier.
     * This should be used for registering event listeners instead of in the constructor.
     * @param plugin The instance of the plugin that owns the modifier that's being registered.
     */
    public void onModifierRegister(Object plugin) {}

    /**
     * @return The enchantment the modifier applies to items. Null if one isn't applied.
     */
    public List<EnchantmentType> getAppliedEnchantments() {
        return Collections.emptyList();
    }

    public ItemStack getModifierItem() {
        return getModifierItem(1);
    }

    public ItemStack getModifierItem(int amount) {
        ItemStack itemStack = ItemStack.builder()
                .itemType(getModifierItemType())
                .quantity(amount)
                .build();

        itemStack.offer(itemStack.getOrCreate(MineTinkerModifierIDData.class).get());
        itemStack.offer(MTKeys.MODIFIER_ID, getKey());

        itemStack.offer(Keys.DISPLAY_NAME, Text.of(getName() + " Modifier"));

        return itemStack;
    }
}
