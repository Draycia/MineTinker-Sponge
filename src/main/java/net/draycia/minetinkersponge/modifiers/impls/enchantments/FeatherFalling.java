package net.draycia.minetinkersponge.modifiers.impls.enchantments;

import net.draycia.minetinkersponge.modifiers.Modifier;
import net.draycia.minetinkersponge.utils.ItemTypeUtils;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.enchantment.EnchantmentType;
import org.spongepowered.api.item.enchantment.EnchantmentTypes;

import java.util.Collections;
import java.util.List;

public class FeatherFalling extends Modifier {

    @Override
    public String getName() {
        return "Feather Falling";
    }

    @Override
    public int getMaxLevel() {
        // This is the effective limit of the enchantment
        // In vanilla it goes up to level 4
        // But the damage reduction caps at level 7
        return 7;
    }

    @Override
    public int getLevelWeight() {
        return 1;
    }

    @Override
    public ItemType getModifierItemType() {
        return ItemTypes.FEATHER;
    }

    @Override
    public List<ItemType> getCompatibleItems() {
        return ItemTypeUtils.getBootTypes();
    }

    @Override
    public List<EnchantmentType> getAppliedEnchantments() {
        return Collections.singletonList(EnchantmentTypes.FEATHER_FALLING);
    }

}
