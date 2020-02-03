package net.draycia.minetinkersponge.modifiers.impls.enchantments;

import com.google.common.collect.ImmutableList;
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

public class SilkTouch extends Modifier {

    private static List<ItemType> compatibleTypes;

    static {
        compatibleTypes = ImmutableList.<ItemType>builder()
                .addAll(ItemTypeUtils.PICKAXES)
                .addAll(ItemTypeUtils.AXES)
                .addAll(ItemTypeUtils.SHOVELS)
                .build();
    }

    @Override
    public List<ItemType> getCompatibleItems() {
        return compatibleTypes;
    }

    @Override
    public String getCompatibilityString() {
        return "Pickaxes, Axes, and Shovels.";
    }

    @Override
    public String getName() {
        return getName("Silk Touch");
    }

    @Override
    public int getMaxLevel() {
        return getMaxLevel(1);
    }

    @Override
    public int getLevelWeight() {
        return getLevelWeight(1);
    }

    @Override
    public ItemType getModifierItemType() {
        return getModifierItemType(ItemTypes.STRING);
    }

    @Override
    public EnchantmentType getAppliedEnchantment() {
        return EnchantmentTypes.SILK_TOUCH;
    }

    @Override
    public Optional<CraftingRecipe> getRecipe() {
        ShapedCraftingRecipe recipe = ShapedCraftingRecipe.builder()
                .aisle("RLR", "LDL", "RLR")
                .where('L', Ingredient.of(ItemTypes.STRING))
                .where('R', Ingredient.of(ItemTypes.WEB))
                .where('D', Ingredient.of(ItemTypes.DIAMOND))
                .result(getModifierItem())
                .id(getKey())
                .build();

        return Optional.of(getCraftingRecipe(recipe));
    }

}
