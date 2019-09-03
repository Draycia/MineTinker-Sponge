package net.draycia.minetinkersponge.modifiers.impls;

import net.draycia.minetinkersponge.modifiers.Modifier;
import net.draycia.minetinkersponge.utils.ItemTypeUtils;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.enchantment.EnchantmentType;
import org.spongepowered.api.item.enchantment.EnchantmentTypes;

import java.util.Collections;
import java.util.List;

public class Mending extends Modifier {

    @Override
    public String getName() {
        return "Mending";
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public int getLevelWeight() {
        return 1;
    }

    @Override
    public ItemType getModifierItemType() {
        return ItemTypes.EXPERIENCE_BOTTLE;
    }

    @Override
    public List<ItemType> getCompatibleItems() {
        // Some entries in getAllTypes isn't repairable (and plugin makes things unbreakable)
        return ItemTypeUtils.getAllTypes();
    }

    @Override
    public List<EnchantmentType> getAppliedEnchantments() {
        return Collections.singletonList(EnchantmentTypes.MENDING);
    }

}
