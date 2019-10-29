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

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class Mending extends Modifier {

    private static List<Class<? extends Modifier>> incompatibleTypes = Collections.singletonList(Infinity.class);

    @Override
    public List<Class<? extends Modifier>> getIncompatibleModifiers() {
        return incompatibleTypes;
    }

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
        // Some entries in getAllTypes aren't repairable (and plugin makes things unbreakable)
        return ItemTypeUtils.getAllTypes();
    }

    @Override
    public EnchantmentType getAppliedEnchantment() {
        return EnchantmentTypes.MENDING;
    }

    @Override
    public Optional<CraftingRecipe> getRecipe() {
        ShapedCraftingRecipe recipe = ShapedCraftingRecipe.builder()
                .aisle("RLR", "LDL", "RLR")
                .where('L', Ingredient.of(ItemTypes.EXPERIENCE_BOTTLE))
                .where('R', Ingredient.of(ItemTypes.MOSSY_COBBLESTONE))
                .where('D', Ingredient.of(ItemTypes.DIAMOND))
                .result(getModifierItem())
                .name(getKey())
                .build();

        return Optional.of(recipe);
    }

}
