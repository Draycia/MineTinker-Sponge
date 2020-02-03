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

public class FeatherFalling extends Modifier {

    @Override
    public List<ItemType> getCompatibleItems() {
        return ItemTypeUtils.BOOTS;
    }

    @Override
    public String getCompatibilityString() {
        return "Boots.";
    }

    @Override
    public String getName() {
        return getName("Feather Falling");
    }

    @Override
    public int getMaxLevel() {
        // 7 is the effective limit of the enchantment
        // In vanilla it goes up to level 4
        // But the damage reduction caps at level 7
        return 4;
    }

    @Override
    public int getLevelWeight() {
        return getLevelWeight(1);
    }

    @Override
    public ItemType getModifierItemType() {
        return getModifierItemType(ItemTypes.FEATHER);
    }

    @Override
    public EnchantmentType getAppliedEnchantment() {
        return EnchantmentTypes.FEATHER_FALLING;
    }

    @Override
    public Optional<CraftingRecipe> getRecipe() {
        ShapedCraftingRecipe recipe = ShapedCraftingRecipe.builder()
                .aisle("FSF", "SDS", "FSF")
                .where('F', Ingredient.of(ItemTypes.FEATHER))
                .where('S', Ingredient.of(ItemTypes.SLIME_BALL))
                .where('D', Ingredient.of(ItemTypes.DIAMOND))
                .result(getModifierItem())
                .id(getKey())
                .build();

        return Optional.of(getCraftingRecipe(recipe));
    }

}
