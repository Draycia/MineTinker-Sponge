package net.draycia.minetinkersponge.utils;

import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.ItemTypes;

import java.util.ArrayList;

public class ItemTypeUtils {

    private static ArrayList<ItemType> axeTypes = new ArrayList<>();
    private static ArrayList<ItemType> hoeTypes = new ArrayList<>();
    private static ArrayList<ItemType> swordTypes = new ArrayList<>();
    private static ArrayList<ItemType> shovelTypes = new ArrayList<>();
    private static ArrayList<ItemType> pickaxeTypes = new ArrayList<>();

    static {
        axeTypes.add(ItemTypes.WOODEN_AXE);
        axeTypes.add(ItemTypes.IRON_AXE);
        axeTypes.add(ItemTypes.GOLDEN_AXE);
        axeTypes.add(ItemTypes.DIAMOND_AXE);

        hoeTypes.add(ItemTypes.WOODEN_HOE);
        hoeTypes.add(ItemTypes.IRON_HOE);
        hoeTypes.add(ItemTypes.GOLDEN_HOE);
        hoeTypes.add(ItemTypes.DIAMOND_HOE);

        swordTypes.add(ItemTypes.WOODEN_SWORD);
        swordTypes.add(ItemTypes.IRON_SWORD);
        swordTypes.add(ItemTypes.GOLDEN_SWORD);
        swordTypes.add(ItemTypes.DIAMOND_SWORD);

        shovelTypes.add(ItemTypes.WOODEN_SHOVEL);
        shovelTypes.add(ItemTypes.IRON_SHOVEL);
        shovelTypes.add(ItemTypes.GOLDEN_SHOVEL);
        shovelTypes.add(ItemTypes.DIAMOND_SHOVEL);

        pickaxeTypes.add(ItemTypes.WOODEN_PICKAXE);
        pickaxeTypes.add(ItemTypes.IRON_PICKAXE);
        pickaxeTypes.add(ItemTypes.GOLDEN_PICKAXE);
        pickaxeTypes.add(ItemTypes.DIAMOND_PICKAXE);
    }

    public static ArrayList<ItemType> getAxeTypes() {
        return axeTypes;
    }

    public static ArrayList<ItemType> getHoeTypes() {
        return hoeTypes;
    }

    public static ArrayList<ItemType> getSwordTypes() {
        return swordTypes;
    }

    public static ArrayList<ItemType> getShovelTypes() {
        return shovelTypes;
    }

    public static ArrayList<ItemType> getPickaxeTypes() {
        return pickaxeTypes;
    }

}
