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

public class DiamondUpgrade extends Modifier {

    private ModManager modManager;

    private static HashMap<ItemType, ItemType> conversions = new HashMap<>();

    static {
        conversions.put(ItemTypes.GOLDEN_SHOVEL, ItemTypes.DIAMOND_SHOVEL);
        conversions.put(ItemTypes.GOLDEN_AXE, ItemTypes.DIAMOND_AXE);
        conversions.put(ItemTypes.GOLDEN_HOE, ItemTypes.DIAMOND_HOE);
        conversions.put(ItemTypes.GOLDEN_PICKAXE, ItemTypes.DIAMOND_PICKAXE);
        conversions.put(ItemTypes.GOLDEN_SWORD, ItemTypes.DIAMOND_SWORD);
        conversions.put(ItemTypes.GOLDEN_BOOTS, ItemTypes.DIAMOND_BOOTS);
        conversions.put(ItemTypes.GOLDEN_LEGGINGS, ItemTypes.DIAMOND_LEGGINGS);
        conversions.put(ItemTypes.GOLDEN_CHESTPLATE, ItemTypes.DIAMOND_CHESTPLATE);
        conversions.put(ItemTypes.GOLDEN_HELMET, ItemTypes.DIAMOND_HELMET);
    }

    private static List<ItemType> compatibleTypes = new ArrayList<>(conversions.keySet());

    public DiamondUpgrade(ModManager modManager) {
        this.modManager = modManager;
    }

    @Override
    public String getName() {
        return "Diamond Upgrade";
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
        return ItemTypes.DIAMOND;
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
        return "Upgrades the item type from gold to diamond when max level is reached.";
    }

    @Override
    public Optional<CraftingRecipe> getRecipe() {
        ShapelessCraftingRecipe recipe = ShapelessCraftingRecipe.builder()
                .addIngredient(Ingredient.of(ItemTypes.DIAMOND))
                .addIngredient(Ingredient.of(ItemTypes.DIAMOND))
                .result(getModifierItem())
                .id(getKey())
                .build();

        return Optional.of(recipe);
    }

}
