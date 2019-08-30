package net.draycia.minetinkersponge.modifiers.impls;

import net.draycia.minetinkersponge.modifiers.ModManager;
import net.draycia.minetinkersponge.modifiers.Modifier;
import net.draycia.minetinkersponge.utils.ItemTypeUtils;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.enchantment.EnchantmentType;
import org.spongepowered.api.item.enchantment.EnchantmentTypes;

import java.util.Collections;
import java.util.List;

public class Sharpness extends Modifier {

    private ModManager modManager;

    @Override
    public String getName() {
        return "Sharpness";
    }

    @Override
    public String getKey() {
        return "sharpness";
    }

    @Override
    public int getMaxLevel() {
        return 5;
    }

    @Override
    public ItemType getModifierItemType() {
        return ItemTypes.FLINT;
    }

    @Override
    public List<ItemType> getCompatibleItems() {
        return ItemTypeUtils.getSwordTypes();
    }

    public Sharpness(ModManager modManager) {
        this.modManager = modManager;
    }

    @Override
    public List<EnchantmentType> getAppliedEnchantments() {
        return Collections.singletonList(EnchantmentTypes.SHARPNESS);
    }
}
