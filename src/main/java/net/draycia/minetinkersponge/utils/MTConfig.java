package net.draycia.minetinkersponge.utils;

import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.block.BlockTypes;

public class MTConfig {

    // The highest level any minetinker item can go
    // Set to -1 to disable the global limit
    public static long GLOBAL_MAX_LEVEL = -1;

    // The type of block used to convert enchanted books into modifiers
    public static BlockType ENCHANTMENT_CONVERT_BLOCK = BlockTypes.BOOKSHELF;

    // Determines if converting items converts enchantments into modifiers
    public static boolean CONVERT_TRANSFERS_ENCHANTMENTS = true;

    // Determines if converting items means modifiers can exceed their level caps
    public static boolean CONVERT_EXCEEDS_MAX_LEVEL = true;

    // Gives converted items the HIDE_ENCHANTMENTS tag
    public static boolean HIDE_ENCHANTMENTS = true;

    // Gives converted items the UNBREAKABLE tag
    public static boolean MAKE_UNBREAKABLE = true;

    // TODO: Implement scaling costs that mimic vanilla. Sharpness 3 would require 6 Sharpness mods, for example.
    // TODO: Method to calculate the modifier cost and remaining modifiers and resulting level.
    public static boolean COSTS_ARE_LINEAR = true;

    // Converts mob drops into minetinker items
    public static boolean CONVERT_MOB_DROPS = true;

}
