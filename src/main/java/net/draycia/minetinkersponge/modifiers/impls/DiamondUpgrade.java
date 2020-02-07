package net.draycia.minetinkersponge.modifiers.impls;

import net.draycia.minetinkersponge.managers.ModManager;
import net.draycia.minetinkersponge.modifiers.Modifier;
import net.draycia.minetinkersponge.utils.ItemTypeUtils;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.recipe.crafting.CraftingRecipe;
import org.spongepowered.api.item.recipe.crafting.Ingredient;
import org.spongepowered.api.item.recipe.crafting.ShapelessCraftingRecipe;
import org.spongepowered.api.text.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class DiamondUpgrade extends Modifier {

    private static HashMap<ItemType, ItemType> conversions = new HashMap<>();

    static {
        // TODO: Mod support?
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

    @Override
    public Text getCompatibilityString() {
        return Text.of("All golden tools and armors.");
    }

    @Override
    public Text getDisplayName() {
        return getName(Text.of("Diamond Upgrade"));
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
        return getModifierItemType(ItemTypes.DIAMOND);
    }

    @Override
    public List<ItemType> getCompatibleItems() {
        return compatibleTypes;
    }

    @Override
    public ItemStack onModifierApplication(ItemStack itemStack, int level) {
        ModManager.incrementItemModifierSlots(itemStack);

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
        return getDescription("Upgrades the item type from gold to diamond when max level is reached.");
    }

    @Override
    public Optional<CraftingRecipe> getRecipe() {
        ShapelessCraftingRecipe recipe = ShapelessCraftingRecipe.builder()
                .addIngredient(Ingredient.of(ItemTypes.DIAMOND))
                .result(getModifierItem())
                .id(getId())
                .build();

        return Optional.of(getCraftingRecipe(recipe));
    }

}
