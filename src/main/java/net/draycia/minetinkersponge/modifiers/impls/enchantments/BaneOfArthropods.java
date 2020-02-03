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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BaneOfArthropods extends Modifier {

    private static List<Class<? extends Modifier>> incompatibleTypes = new ArrayList<>();
    private static List<ItemType> compatibleTypes;

    static {
        incompatibleTypes.add(Sharpness.class);
        incompatibleTypes.add(Smite.class);

        compatibleTypes = ImmutableList.<ItemType>builder()
                .addAll(ItemTypeUtils.SWORDS)
                .addAll(ItemTypeUtils.AXES)
                .build();
    }

    @Override
    public String getCompatibilityString() {
        return "Swords and Axes.";
    }

    @Override
    public List<Class<? extends Modifier>> getIncompatibleModifiers() {
        return incompatibleTypes;
    }

    @Override
    public List<ItemType> getCompatibleItems() {
        return compatibleTypes;
    }

    @Override
    public String getName() {
        return getName("Bane Of Arthropods");
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
        return getModifierItemType(ItemTypes.FERMENTED_SPIDER_EYE);
    }

    @Override
    public EnchantmentType getAppliedEnchantment() {
        return EnchantmentTypes.BANE_OF_ARTHROPODS;
    }

    @Override
    public Optional<CraftingRecipe> getRecipe() {
        ShapedCraftingRecipe recipe = ShapedCraftingRecipe.builder()
                .aisle("PEP", "ECE", "PEP")
                .where('E', Ingredient.of(ItemTypes.STRING))
                .where('C', Ingredient.of(ItemTypes.FERMENTED_SPIDER_EYE))
                .where('P', Ingredient.of(ItemTypes.SPIDER_EYE))
                .result(getModifierItem())
                .id(getKey())
                .build();

        return Optional.of(getCraftingRecipe(recipe));
    }

}
