package net.draycia.minetinkersponge.modifiers.impls.enchantments;

import net.draycia.minetinkersponge.modifiers.Modifier;
import net.draycia.minetinkersponge.utils.ItemTypeUtils;
import org.spongepowered.api.data.Keys;
import org.spongepowered.api.data.type.DyeColors;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.enchantment.EnchantmentType;
import org.spongepowered.api.item.enchantment.EnchantmentTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.recipe.crafting.CraftingRecipe;
import org.spongepowered.api.item.recipe.crafting.Ingredient;
import org.spongepowered.api.item.recipe.crafting.ShapedCraftingRecipe;

import java.util.List;
import java.util.Optional;

public class DepthStrider extends Modifier {

    @Override
    public String getName() {
        return "Depth Strider";
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    public int getLevelWeight() {
        return 1;
    }

    @Override
    public ItemType getModifierItemType() {
        return ItemTypes.LILY_PAD;
    }

    @Override
    public List<ItemType> getCompatibleItems() {
        return ItemTypeUtils.getBootTypes();
    }

    @Override
    public EnchantmentType getAppliedEnchantment() {
        return EnchantmentTypes.DEPTH_STRIDER;
    }

    @Override
    public Optional<CraftingRecipe> getRecipe() {
        ItemStack dye = ItemStack.builder().itemType(ItemTypes.LAPIS_LAZULI).build();

        ShapedCraftingRecipe recipe = ShapedCraftingRecipe.builder()
                .aisle("PEP", "ECE", "PEP")
                .where('E', Ingredient.of(dye))
                .where('C', Ingredient.of(ItemTypes.DIAMOND))
                .where('P', Ingredient.of(ItemTypes.PRISMARINE_CRYSTALS))
                .result(getModifierItem())
                .name(getKey())
                .build();

        return Optional.of(recipe);
    }

}
