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

public class BindingCurse extends Modifier {

    private static List<ItemType> compatibleTypes;

    static {
        compatibleTypes = ImmutableList.<ItemType>builder()
                .addAll(ItemTypeUtils.HELMETS)
                .addAll(ItemTypeUtils.CHESTPLATES)
                .addAll(ItemTypeUtils.LEGGINGS)
                .addAll(ItemTypeUtils.BOOTS)
                .build();
    }

    @Override
    public List<ItemType> getCompatibleItems() {
        return compatibleTypes;
    }

    @Override
    public String getCompatibilityString() {
        return "All armor.";
    }

    @Override
    public String getName() {
        return getName("Curse of Binding");
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
        return getModifierItemType(ItemTypes.STICK);
    }

    @Override
    public EnchantmentType getAppliedEnchantment() {
        return EnchantmentTypes.BINDING_CURSE;
    }

    @Override
    public Optional<CraftingRecipe> getRecipe() {
        ShapedCraftingRecipe recipe = ShapedCraftingRecipe.builder()
                .aisle("PEP", "ECE", "PEP")
                .where('E', Ingredient.of(ItemTypes.COAL))
                .where('C', Ingredient.of(ItemTypes.DIAMOND))
                .where('P', Ingredient.of(ItemTypes.BONE))
                .result(getModifierItem())
                .id(getKey())
                .build();

        return Optional.of(getCraftingRecipe(recipe));
    }

}
