package net.draycia.minetinkersponge.modifiers.impls;

import net.draycia.minetinkersponge.modifiers.Modifier;
import net.draycia.minetinkersponge.utils.ItemTypeUtils;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.enchantment.EnchantmentType;
import org.spongepowered.api.item.enchantment.EnchantmentTypes;

import java.util.Collections;
import java.util.List;

public class Power extends Modifier {

    @Override
    public String getName() {
        return "Power";
    }

    @Override
    public int getMaxLevel() {
        return 5;
    }

    @Override
    public int getLevelWeight() {
        return 1;
    }

    @Override
    public ItemType getModifierItemType() {
        return ItemTypes.QUARTZ;
    }

    @Override
    public List<ItemType> getCompatibleItems() {
        return ItemTypeUtils.getBowTypes();
    }

    @Override
    public List<EnchantmentType> getAppliedEnchantments() {
        return Collections.singletonList(EnchantmentTypes.POWER);
    }

}
