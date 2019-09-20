package net.draycia.minetinkersponge.utils;

import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.block.BlockTypes;

public class MTConfig {

    public static long GLOBAL_MAX_LEVEL = -1;

    public static BlockType ENCHANTMENT_CONVERT_BLOCK = BlockTypes.BOOKSHELF;

    public static boolean CONVERT_TRANSFERS_ENCHANTMENTS = true;
    public static boolean CONVERT_EXCEEDS_MAX_LEVEL = true;
    public static boolean HIDE_ENCHANTMENTS = true;
    public static boolean MAKE_UNBREAKABLE = true;

    // TODO: Implement scaling costs that mimic vanilla. Sharpness 3 would require 6 Sharpness mods, for example.
    // TODO: Method to calculate the modifier cost and remaining modifiers and resulting level.
    public static boolean COSTS_ARE_LINEAR = true;

    public static boolean CONVERT_MOB_DROPS = true;

}
