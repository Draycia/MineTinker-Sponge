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
    public EnchantmentType getAppliedEnchantment() {
        return EnchantmentTypes.POWER;
    }

    @Override
    public Optional<CraftingRecipe> getRecipe() {
        ShapedCraftingRecipe recipe = ShapedCraftingRecipe.builder()
                .aisle("RLR", "LDL", "RLR")
                .where('L', Ingredient.of(ItemTypes.IRON_INGOT))
                .where('R', Ingredient.of(ItemTypes.FLINT))
                .where('D', Ingredient.of(ItemTypes.ARROW))
                .result(getModifierItem())
                .name(getKey())
                .build();

        return Optional.of(recipe);
    }

}
