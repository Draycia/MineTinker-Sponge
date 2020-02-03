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

public class Knockback extends Modifier {

    @Override
    public List<ItemType> getCompatibleItems() {
        return ItemTypeUtils.SWORDS;
    }

    @Override
    public String getCompatibilityString() {
        return "Swords.";
    }

    @Override
    public String getName() {
        return getName("Knockback");
    }

    @Override
    public int getMaxLevel() {
        return getMaxLevel(2);
    }

    @Override
    public int getLevelWeight() {
        return getLevelWeight(1);
    }

    @Override
    public ItemType getModifierItemType() {
        return getModifierItemType(ItemTypes.TNT);
    }

    @Override
    public EnchantmentType getAppliedEnchantment() {
        return EnchantmentTypes.KNOCKBACK;
    }

    @Override
    public Optional<CraftingRecipe> getRecipe() {
        ShapedCraftingRecipe recipe = ShapedCraftingRecipe.builder()
                .aisle("SGS", "GDG", "SGS")
                .where('S', Ingredient.of(ItemTypes.SAND))
                .where('G', Ingredient.of(ItemTypes.GUNPOWDER))
                .where('D', Ingredient.of(ItemTypes.DIAMOND))
                .result(getModifierItem())
                .id(getKey())
                .build();

        return Optional.of(getCraftingRecipe(recipe));
    }

}
