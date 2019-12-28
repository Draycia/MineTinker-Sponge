package net.draycia.minetinkersponge.modifiers.impls.upgrades;

import net.draycia.minetinkersponge.managers.ModManager;
import net.draycia.minetinkersponge.modifiers.Modifier;
import net.draycia.minetinkersponge.utils.ItemTypeUtils;
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

public class GoldUpgrade extends Modifier {

    private ModManager modManager;

    private static HashMap<ItemType, ItemType> conversions = new HashMap<>();

    static {
        // TODO: Mod support?
        conversions.put(ItemTypes.IRON_SHOVEL, ItemTypes.GOLDEN_SHOVEL);
        conversions.put(ItemTypes.IRON_AXE, ItemTypes.GOLDEN_AXE);
        conversions.put(ItemTypes.IRON_HOE, ItemTypes.GOLDEN_HOE);
        conversions.put(ItemTypes.IRON_PICKAXE, ItemTypes.GOLDEN_PICKAXE);
        conversions.put(ItemTypes.IRON_SWORD, ItemTypes.GOLDEN_SWORD);
        conversions.put(ItemTypes.IRON_BOOTS, ItemTypes.GOLDEN_BOOTS);
        conversions.put(ItemTypes.IRON_LEGGINGS, ItemTypes.GOLDEN_LEGGINGS);
        conversions.put(ItemTypes.IRON_CHESTPLATE, ItemTypes.GOLDEN_CHESTPLATE);
        conversions.put(ItemTypes.IRON_HELMET, ItemTypes.GOLDEN_HELMET);
    }

    private static List<ItemType> compatibleTypes = new ArrayList<>(conversions.keySet());

    public GoldUpgrade(ModManager modManager) {
        this.modManager = modManager;
    }

    @Override
    public String getName() {
        return getName("Gold Upgrade");
    }

    @Override
    public int getMaxLevel() {
        return getMaxLevel(-1);
    }

    @Override
    public int getLevelWeight() {
        return getLevelWeight(1);
    }

    @Override
    public ItemType getModifierItemType() {
        return getModifierItemType(ItemTypes.GOLD_INGOT);
    }

    @Override
    public List<ItemType> getCompatibleItems() {
        return compatibleTypes;
    }

    @Override
    public ItemStack onModifierApplication(ItemStack itemStack, int level) {
        modManager.incrementItemModifierSlots(itemStack);

        int desiredLevel = ItemTypeUtils.getMaterialCost(itemStack.getType());

        if (level >= desiredLevel && conversions.containsKey(itemStack.getType())) {
            return ItemStack.builder()
                    .from(itemStack)
                    .itemType(conversions.get(itemStack.getType()))
                    .build();
        }

        return itemStack;
    }

    @Override
    public String getDescription() {
        return getDescription("Upgrades the item type from iron to gold when max level is reached.");
    }

    @Override
    public Optional<CraftingRecipe> getRecipe() {
        ShapelessCraftingRecipe recipe = ShapelessCraftingRecipe.builder()
                .addIngredient(Ingredient.of(ItemTypes.GOLD_INGOT))
                .result(getModifierItem())
                .id(getKey())
                .build();

        return Optional.of(getCraftingRecipe(recipe));
    }

}
