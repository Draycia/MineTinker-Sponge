package net.draycia.minetinkersponge.modifiers.impls.enchantments;

import net.draycia.minetinkersponge.modifiers.Modifier;
import net.draycia.minetinkersponge.utils.ItemTypeUtils;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.enchantment.EnchantmentType;
import org.spongepowered.api.item.enchantment.EnchantmentTypes;

import java.util.Collections;
import java.util.List;

public class AquaAffinity extends Modifier {

    @Override
    public String getName() {
        return "Aqua Affinity";
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
        return ItemTypes.PRISMARINE_SHARD;
    }

    @Override
    public List<ItemType> getCompatibleItems() {
        return ItemTypeUtils.getHelmetTypes();
    }

    @Override
    public List<EnchantmentType> getAppliedEnchantments() {
        return Collections.singletonList(EnchantmentTypes.AQUA_AFFINITY);
    }

}
