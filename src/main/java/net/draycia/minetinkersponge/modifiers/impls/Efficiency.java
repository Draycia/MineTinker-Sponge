package net.draycia.minetinkersponge.modifiers.impls;

import net.draycia.minetinkersponge.modifiers.Modifier;
import net.draycia.minetinkersponge.utils.CompositeUnmodifiableList;
import net.draycia.minetinkersponge.utils.ItemTypeUtils;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.enchantment.EnchantmentType;
import org.spongepowered.api.item.enchantment.EnchantmentTypes;

import java.util.Collections;
import java.util.List;

public class Efficiency extends Modifier {

    @Override
    public String getName() {
        return "Efficiency";
    }

    @Override
    public int getMaxLevel() {
        return 5;
    }

    @Override
    public ItemType getModifierItemType() {
        return ItemTypes.REDSTONE;
    }

    @Override
    public List<ItemType> getCompatibleItems() {
        // Lol
        return new CompositeUnmodifiableList<>(
                new CompositeUnmodifiableList<>(
                        ItemTypeUtils.getPickaxeTypes(),
                        ItemTypeUtils.getAxeTypes()),
                        ItemTypeUtils.getShovelTypes());
    }

    @Override
    public List<EnchantmentType> getAppliedEnchantments() {
        return Collections.singletonList(EnchantmentTypes.EFFICIENCY);
    }

}
