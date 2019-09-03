package net.draycia.minetinkersponge.modifiers.impls;

import net.draycia.minetinkersponge.modifiers.ModManager;
import net.draycia.minetinkersponge.modifiers.Modifier;
import net.draycia.minetinkersponge.utils.ItemTypeUtils;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GoldUpgrade extends Modifier {

    private ModManager modManager;

    private static HashMap<ItemType, ItemType> conversions = new HashMap<>();

    static {
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
        return "Gold Upgrade";
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
        return ItemTypes.GOLD_INGOT;
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

}