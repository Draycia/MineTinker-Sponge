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

    // TODO: Implement scaling costs that mimic vanilla. Sharpness 3 would require 4 Sharpness mods, for example.
    public static boolean COSTS_ARE_LINEAR = true;

    // Converts mob drops into minetinker items
    public static boolean CONVERT_MOB_DROPS = true;

    // Should the amount of XP given to the shield = the amount of damage reduced?
    public static boolean SHIELD_XP_IS_DAMAGE_REDUCED = false;

    // If not, how much XP should be given per successful block?
    public static int SHIELD_XP_PER_BLOCK = 1;

    public static String RESULT_INCOMPATIBLE_MODIFIER = "That modifier is incompatible with %s";
    public static String RESULT_INCOMPATIBLE_TOOL = "That item is not MineTinker compatible!";
    public static String RESULT_NOT_ENOUGH_SLOTS = "That item does not have enough slots!";
    public static String RESULT_RANDOM_CHANCE = "Failed to apply enchantment due to RNG!";
    public static String RESULT_LEVEL_CAP = "Modifier is already max level!";

}
