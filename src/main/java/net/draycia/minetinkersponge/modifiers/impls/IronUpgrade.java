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

public class IronUpgrade extends Modifier {

    private static HashMap<ItemType, ItemType> conversions = new HashMap<>();

    static {
        // TODO: Mod support?
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

    @Override
    public Text getCompatibilityString() {
        return Text.of("All wooden tools and leather armors.");
    }

    @Override
    public Text getDisplayName() {
        return getName(Text.of("Iron Upgrade"));
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
        return getModifierItemType(ItemTypes.IRON_INGOT);
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
        return getDescription("Upgrades the item type from leather/wood to iron when max level is reached.");
    }

    @Override
    public Optional<CraftingRecipe> getRecipe() {
        ShapelessCraftingRecipe recipe = ShapelessCraftingRecipe.builder()
                .addIngredient(Ingredient.of(ItemTypes.IRON_INGOT))
                .result(getModifierItem())
                .id(getId())
                .build();

        return Optional.of(getCraftingRecipe(recipe));
    }

}
