package net.draycia.minetinkersponge.modifiers.impls.enchantments;

import net.draycia.minetinkersponge.modifiers.Modifier;
import net.draycia.minetinkersponge.utils.ItemTypeUtils;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.enchantment.EnchantmentType;
import org.spongepowered.api.item.enchantment.EnchantmentTypes;
import org.spongepowered.api.item.recipe.crafting.CraftingRecipe;
import org.spongepowered.api.item.recipe.crafting.Ingredient;
import org.spongepowered.api.item.recipe.crafting.ShapedCraftingRecipe;

import java.util.List;
import java.util.Optional;

public class Efficiency extends Modifier {

    @Override
    public String getName() {
        return getName("Efficiency");
    }

    @Override
    public int getMaxLevel() {
        return getMaxLevel(5);
    }

    @Override
    public int getLevelWeight() {
        return getLevelWeight(1);
    }

    @Override
    public ItemType getModifierItemType() {
        return getModifierItemType(ItemTypes.REDSTONE);
    }

    @Override
    public List<ItemType> getCompatibleItems() {
        return ItemTypeUtils.getToolTypes();
    }

    @Override
    public EnchantmentType getAppliedEnchantment() {
        return EnchantmentTypes.EFFICIENCY;
    }

    @Override
    public Optional<CraftingRecipe> getRecipe() {
        ShapedCraftingRecipe recipe = ShapedCraftingRecipe.builder()
                .aisle("FDF", "DRD", "FDF")
                .where('F', Ingredient.of(ItemTypes.FIREWORKS))
                .where('D', Ingredient.of(ItemTypes.DIAMOND))
                .where('R', Ingredient.of(ItemTypes.REDSTONE_BLOCK))
                .result(getModifierItem())
                .id(getKey())
                .build();

        return Optional.of(getCraftingRecipe(recipe));
    }

}
