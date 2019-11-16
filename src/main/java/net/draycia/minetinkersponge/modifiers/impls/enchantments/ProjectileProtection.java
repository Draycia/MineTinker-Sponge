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
import java.util.List;
import java.util.Optional;

public class ProjectileProtection extends Modifier {

    private static List<Class<? extends Modifier>> incompatibleTypes = new ArrayList<>();

    static {
        incompatibleTypes.add(Protection.class);
        incompatibleTypes.add(FireProtection.class);
    }

    @Override
    public List<Class<? extends Modifier>> getIncompatibleModifiers() {
        return incompatibleTypes;
    }

    @Override
    public String getName() {
        return getName("Projectile Protection");
    }

    @Override
    public int getMaxLevel() {
        return getMaxLevel(4);
    }

    @Override
    public int getLevelWeight() {
        return getLevelWeight(1);
    }

    @Override
    public ItemType getModifierItemType() {
        return getModifierItemType(ItemTypes.GOLD_BLOCK);
    }

    @Override
    public List<ItemType> getCompatibleItems() {
        return new CompositeUnmodifiableList<>(ItemTypeUtils.getArmorTypes(), ItemTypeUtils.getMiscArmorTypes());
    }

    @Override
    public EnchantmentType getAppliedEnchantment() {
        return EnchantmentTypes.PROJECTILE_PROTECTION;
    }

    @Override
    public Optional<CraftingRecipe> getRecipe() {
        ShapedCraftingRecipe recipe = ShapedCraftingRecipe.builder()
                .aisle("RLR", "LDL", "RLR")
                .where('L', Ingredient.of(ItemTypes.ARROW))
                .where('R', Ingredient.of(ItemTypes.IRON_BLOCK))
                .where('D', Ingredient.of(ItemTypes.DIAMOND))
                .result(getModifierItem())
                .id(getKey())
                .build();

        return Optional.of(getCraftingRecipe(recipe));
    }

}
