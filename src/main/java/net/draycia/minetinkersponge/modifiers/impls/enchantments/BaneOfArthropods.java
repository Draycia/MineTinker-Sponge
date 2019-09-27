package net.draycia.minetinkersponge.modifiers.impls.enchantments;

import net.draycia.minetinkersponge.modifiers.Modifier;
import net.draycia.minetinkersponge.utils.CompositeUnmodifiableList;
import net.draycia.minetinkersponge.utils.ItemTypeUtils;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.enchantment.EnchantmentType;
import org.spongepowered.api.item.enchantment.EnchantmentTypes;
import org.spongepowered.api.item.recipe.crafting.CraftingRecipe;
import org.spongepowered.api.item.recipe.crafting.Ingredient;
import org.spongepowered.api.item.recipe.crafting.ShapedCraftingRecipe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class BaneOfArthropods extends Modifier {

    private static List<Class<? extends Modifier>> incompatibleTypes = new ArrayList<>();
    private static List<ItemType> compatibleItems;

    static {
        incompatibleTypes.add(Sharpness.class);
        incompatibleTypes.add(Smite.class);

        compatibleItems = new CompositeUnmodifiableList<>(ItemTypeUtils.getSwordTypes(), ItemTypeUtils.getAxeTypes());
    }

    @Override
    public List<Class<? extends Modifier>> getIncompatibleModifiers() {
        return incompatibleTypes;
    }

    @Override
    public List<ItemType> getCompatibleItems() {
        return compatibleItems;
    }

    @Override
    public String getName() {
        return "Bane Of Arthropods";
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
        return ItemTypes.FERMENTED_SPIDER_EYE;
    }

    @Override
    public List<EnchantmentType> getAppliedEnchantments() {
        return Collections.singletonList(EnchantmentTypes.BANE_OF_ARTHROPODS);
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

        return Optional.of(recipe);
    }

}
