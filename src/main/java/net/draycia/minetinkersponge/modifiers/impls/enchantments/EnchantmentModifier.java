package net.draycia.minetinkersponge.modifiers.impls.enchantments;

import com.google.common.collect.Lists;
import com.google.common.collect.Streams;
import net.draycia.minetinkersponge.managers.ModManager;
import net.draycia.minetinkersponge.modifiers.Modifier;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.enchantment.Enchantment;
import org.spongepowered.api.item.enchantment.EnchantmentType;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.recipe.crafting.CraftingRecipe;
import org.spongepowered.api.item.recipe.crafting.Ingredient;
import org.spongepowered.api.item.recipe.crafting.ShapelessCraftingRecipe;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class EnchantmentModifier extends Modifier {
    private EnchantmentType type;
    private List<ItemType> compatibleItems;

    public EnchantmentModifier(EnchantmentType type) {
        this.type = type;
        compatibleItems = Sponge.getRegistry().getAllOf(ItemType.class).stream().filter(itemType -> type.canBeAppliedToStack(ItemStack.of(itemType))).collect(Collectors.toList());
    }

    @Override
    public String getName() {
        return getName(type.getName());
    }

    @Override
    public int getMaxLevel() {
        return getMaxLevel(type.getMaximumLevel());
    }

    @Override
    public int getLevelWeight() {
        return getLevelWeight(type.getWeight());
    }

    @Override
    public List<ItemType> getCompatibleItems() {
        return compatibleItems;
    }

    @Override
    public ItemType getModifierItemType() {
        return getModifierItemType(ItemTypes.ENCHANTED_BOOK);
    }

    @Override
    public EnchantmentType getAppliedEnchantment() {
        return type;
    }

    @Override
    public String getCompatibilityString() {
        return "Anything that can hold the " + type.getName() + " enchantment";
    }

    @Override
    public List<Class<? extends Modifier>> getIncompatibleModifiers() {
        return ModManager.getAllModifiers().values().stream().filter(a -> getAppliedEnchantment() != null && !getAppliedEnchantment().isCompatibleWith(type)).map(Modifier::getClass).collect(Collectors.toList());
    }

    @Override
    public Optional<CraftingRecipe> getRecipe() {
        ShapelessCraftingRecipe recipe = ShapelessCraftingRecipe.builder()
                .addIngredient(Ingredient.builder().with(ingredient -> {
                    if(ingredient.getType() == ItemTypes.ENCHANTED_BOOK)
                        return ingredient.get(Keys.ITEM_ENCHANTMENTS).orElse(Lists.newArrayList()).stream().map(Enchantment::getType).anyMatch(type::equals);
                    else {
                        return false;
                    }
                }).build())
                .result(getModifierItem())
                .id(getKey())
                .build();


        return Optional.of(getCraftingRecipe(recipe));
    }
}
