package net.draycia.minetinkersponge.modifiers.impls.upgrades;

import net.draycia.minetinkersponge.modifiers.ModManager;
import net.draycia.minetinkersponge.modifiers.Modifier;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.recipe.crafting.CraftingRecipe;
import org.spongepowered.api.item.recipe.crafting.Ingredient;
import org.spongepowered.api.item.recipe.crafting.ShapelessCraftingRecipe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class IronUpgrade extends Modifier {

    private ModManager modManager;

    private static HashMap<ItemType, ItemType> conversions = new HashMap<>();

    static {
        conversions.put(ItemTypes.WOODEN_SHOVEL, ItemTypes.IRON_SHOVEL);
        conversions.put(ItemTypes.WOODEN_AXE, ItemTypes.IRON_AXE);
        conversions.put(ItemTypes.WOODEN_HOE, ItemTypes.IRON_HOE);
        conversions.put(ItemTypes.WOODEN_PICKAXE, ItemTypes.IRON_PICKAXE);
        conversions.put(ItemTypes.WOODEN_SWORD, ItemTypes.IRON_SWORD);
        conversions.put(ItemTypes.LEATHER_BOOTS, ItemTypes.IRON_BOOTS);
        conversions.put(ItemTypes.LEATHER_LEGGINGS, ItemTypes.IRON_LEGGINGS);
        conversions.put(ItemTypes.LEATHER_CHESTPLATE, ItemTypes.IRON_CHESTPLATE);
        conversions.put(ItemTypes.LEATHER_HELMET, ItemTypes.IRON_HELMET);
    }

    private static List<ItemType> compatibleTypes = new ArrayList<>(conversions.keySet());

    public IronUpgrade(ModManager modManager) {
        this.modManager = modManager;
    }

    @Override
    public String getName() {
        return "Iron Upgrade";
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
        return ItemTypes.IRON_INGOT;
    }

    @Override
    public List<ItemType> getCompatibleItems() {
        return compatibleTypes;
    }

    @Override
    public ItemStack onModifierApplication(ItemStack itemStack, int level) {
        modManager.incrementItemModifierSlots(itemStack);

        if (level >= getMaxLevel() && conversions.containsKey(itemStack.getType())) {
            return ItemStack.builder()
                    .from(itemStack)
                    .itemType(conversions.get(itemStack.getType()))
                    .build();
        }

        return itemStack;
    }

    @Override
    public String getDescription() {
        return "Upgrades the item type from leather/wood to iron when max level is reached.";
    }

    @Override
    public Optional<CraftingRecipe> getRecipe() {
        ShapelessCraftingRecipe recipe = ShapelessCraftingRecipe.builder()
                .addIngredient(Ingredient.of(ItemTypes.IRON_INGOT))
                .addIngredient(Ingredient.of(ItemTypes.IRON_INGOT))
                .result(getModifierItem())
                .id(getKey())
                .build();

        return Optional.of(recipe);
    }

}
