package net.draycia.minetinkersponge.modifiers.impls;

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
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.TranslatableText;

import java.util.Collections;
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
    public Text getDisplayName() {
        return getName(TranslatableText.of(type.getTranslation()));
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
    public Text getCompatibilityString() {
        return Text.of("Anything that can hold the ",  TranslatableText.of(type.getTranslation()), " enchantment");
    }

    @Override
    public List<Class<? extends Modifier>> getIncompatibleModifiers() {
        return ModManager.getAllModifiers().values().stream()
                .filter(a -> !a.equals(this) && a.getAppliedEnchantment() != null && !a.getAppliedEnchantment().isCompatibleWith(type))
                .map(Modifier::getClass)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<CraftingRecipe> getRecipe() {
        ItemStack book = ItemStack.builder().itemType(ItemTypes.ENCHANTED_BOOK).build();
        book.offer(Keys.STORED_ENCHANTMENTS, Collections.singletonList(Enchantment.builder().type(type).level(1).build()));

        ShapelessCraftingRecipe recipe = ShapelessCraftingRecipe.builder()
                .addIngredient(Ingredient.builder().with(book).build())
                .result(getModifierItem())
                .id(getId())
                .build();

        return Optional.of(getCraftingRecipe(recipe));
    }
}
