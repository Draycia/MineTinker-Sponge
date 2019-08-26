package net.draycia.minetinkersponge.modifiers;

import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.enchantment.Enchantment;

import java.util.List;

public abstract class Modifier {

    public abstract String getName();
    public abstract String getKey();
    public abstract int getMaxLevel();

    /**
     * A list of {@link ItemType ItemTypes} that the modifier can be applied to.
     * @return A list of compatible ItemTypes. Or null if the modifier can go on any item.
     */
    public abstract List<ItemType> getCompatibleItems();

    /**
     * Called when the {@link ModManager} successfully registers the modifier.
     * This should be used for registering event listeners instead of in the constructor.
     * @param plugin The instance of the plugin that owns the modifier that's being registered.
     */
    public void onModifierRegister(Object plugin) {}

    /**
     * @return The enchantment the modifier applies to items. Null if one isn't applied.
     */
    public Enchantment getAppliedEnchantment() {
        return null;
    }
}
