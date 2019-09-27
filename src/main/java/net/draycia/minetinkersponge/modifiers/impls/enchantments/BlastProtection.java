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

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class BlastProtection extends Modifier {

    @Override
    public String getName() {
        return "Blast Protection";
    }

    @Override
    public int getMaxLevel() {
        return 4;
    }

    @Override
    public int getLevelWeight() {
        return 1;
    }

    @Override
    public ItemType getModifierItemType() {
        return ItemTypes.GUNPOWDER;
    }

    @Override
    public List<ItemType> getCompatibleItems() {
        return new CompositeUnmodifiableList<>(ItemTypeUtils.getArmorTypes(), ItemTypeUtils.getMiscArmorTypes());
    }

    @Override
    public List<EnchantmentType> getAppliedEnchantments() {
        return Collections.singletonList(EnchantmentTypes.BLAST_PROTECTION);
    }

    @Override
    public Optional<CraftingRecipe> getRecipe() {
        ShapedCraftingRecipe recipe = ShapedCraftingRecipe.builder()
                .aisle("PEP", "ECE", "PEP")
                .where('E', Ingredient.of(ItemTypes.TNT))
                .where('C', Ingredient.of(ItemTypes.DIAMOND))
                .where('P', Ingredient.of(ItemTypes.IRON_BLOCK))
                .result(getModifierItem())
                .id(getKey())
                .build();

        return Optional.of(recipe);
    }

}
