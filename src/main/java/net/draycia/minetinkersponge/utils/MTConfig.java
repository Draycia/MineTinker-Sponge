package net.draycia.minetinkersponge.utils;

import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.block.BlockTypes;

import java.util.HashMap;

@ConfigSerializable
public class MTConfig {

    @Setting(value = "globalMaxLevel", comment = "The highest level any MineTinker item can go, set to -1 to disable limit.")
    public static long GLOBAL_MAX_LEVEL = -1;

    // The type of block used to convert enchanted books into modifiers
    public static BlockType ENCHANTMENT_CONVERT_BLOCK = BlockTypes.BOOKSHELF;

    @Setting(value = "convertTransfersEnchantments", comment = "Determines if converting items converts enchantments into modifiers.")
    public static boolean CONVERT_TRANSFERS_ENCHANTMENTS = true;

    @Setting(value = "convertExceedsMaxLevel", comment = "Determines if converting items means modifiers can exceed their level caps.")
    public static boolean CONVERT_EXCEEDS_MAX_LEVEL = true;

    @Setting(value = "hideEnchantments", comment = "Determines if converting items adds the HIDE_ENCHANTMENTS tag.")
    public static boolean HIDE_ENCHANTMENTS = true;

    @Setting(value = "disableEnchantingTables", comment = "Determines if vanilla enchanting tables can be used by players or not.")
    public static boolean DISABLE_ENCHANTING_TABLES = true;

    @Setting(value = "disableAnvils", comment = "Determines if vanilla anvils can be used by players or not.")
    public static boolean DISABLE_ANVILS = true;

    @Setting(value = "preventEnchantedBookUse", comment = "Determines if vanilla enchanted books can be used on items.")
    public static boolean DISABLE_ENCHANTED_BOOKS = true;

    @Setting(value = "makeItemsUnbreakable", comment = "Determines if converting items adds the UNBREAKABLE tag.")
    public static boolean MAKE_UNBREAKABLE = true;

    // TODO: Implement scaling costs that mimic vanilla. Sharpness 3 would require 4 Sharpness mods, for example.
    //@Setting(value = "costsAreLinear", comment = "Currently unused.")
    //public static boolean COSTS_ARE_LINEAR = true;

    @Setting(value = "convertMobDrops", comment = "Determines if mob drops should be automatically converted.")
    public static boolean CONVERT_MOB_DROPS = true;

    @Setting(value = "convertFishingLoot", comment = "Determines if fish / other loot fished up should be automatically converted.")
    public static boolean CONVERT_FISHING_LOOT = true;

    @Setting(value = "convertLootTables", comment = "Determines if loot generated for dungeon chests should be automatically converted.")
    public static boolean CONVERT_LOOT_TABLES = true;

    @Setting(value = "shieldXpIsDamageReduced", comment = "Determines if shield XP gains should equal the amount of damage blocked.")
    public static boolean SHIELD_XP_IS_DAMAGE_REDUCED = false;

    @Setting(value = "shieldXpPerBlock", comment = "If shieldXpIsDamageReduced is set to false, determines the amount of XP shields gain for successful blocks.")
    public static int SHIELD_XP_PER_BLOCK = 1;

    @Setting(value = "startingSlotCount", comment = "The amount of modifier slots items start out with.")
    public static int STARTING_SLOT_COUNT = 1;

    @Setting(value = "startingItemLevel", comment = "The level items start out with.")
    public static int STARTING_LEVEL = 1;

    @Setting(value = "startingItemExperience", comment = "The amount of experience items start out with.")
    public static int STARTING_EXPERIENCE = 0;

    // TODO: Per-Item default modifiers
    @Setting(value = "startingModifiers", comment = "A list of modifiers that all items start out with.")
    public static HashMap<String, Integer> STARTING_MODIFIERS = new HashMap<>();
}
