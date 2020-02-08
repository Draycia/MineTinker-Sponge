package net.draycia.minetinkersponge.utils;

import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

@ConfigSerializable
public class MTTranslations {

    @Setting("maxLevel")
    public static String MAX_LEVEL = "Max Level: ";

    @Setting("atAllLevels")
    public static String AT_ALL_LEVELS = " at all levels.";

    @Setting("blankLine")
    public static String BLANK_LINE = "";

    @Setting("slotCostSeparator")
    public static String SLOT_COST_SEPARATOR = ", ";

    @Setting("requiredSlots")
    public static String REQUIRED_SLOTS = "Required Slots: ";

    @Setting("applicableOn")
    public static String APPLICABLE_ON = "Applicable On: ";

    @Setting("appliedEnchantments")
    public static String APPLIED_ENCHANTMENTS = "Applied Enchantment: ";

    @Setting("returnToModifiers")
    public static String RETURN_TO_MODIFIERS = "Return To Modifiers";

    @Setting("modifier")
    public static String MODIFIER = "Modifier: ";

    @Setting("minetinkerModifiers")
    public static String MINETINKER_MODIFIERS = "MineTinker Modifiers";

    @Setting(value = "resultIncompatibleModifier")
    public static String RESULT_INCOMPATIBLE_MODIFIER = "That modifier is incompatible with %s";

    @Setting(value = "resultIncompatibleTool")
    public static String RESULT_INCOMPATIBLE_TOOL = "That item is not MineTinker compatible!";

    @Setting(value = "resultNotEnoughSlots")
    public static String RESULT_NOT_ENOUGH_SLOTS = "That item does not have enough slots!";

    @Setting(value = "resultRandomChance")
    public static String RESULT_RANDOM_CHANCE = "Failed to apply enchantment due to RNG!";

    @Setting(value = "resultLevelCap")
    public static String RESULT_LEVEL_CAP = "Modifier is already max level!";

    @Setting(value = "successIncrement")
    public static String SUCCESS_INCREMENT = "Incremented item level!";

    @Setting(value = "failedAddModifier")
    public static String FAILED_ADD_MODIFIER = "Failed to add modifier to item!";

    @Setting(value = "successAddModifier")
    public static String SUCCESS_ADD_MODIFIER = "Added modifier %modifier%!";

    @Setting(value = "addedSlots")
    public static String ADDED_SLOTS = "Added slots to item!";

    @Setting(value = "convertedItem")
    public static String CONVERTED_ITEM = "Converted item!";

    @Setting(value = "createdItem")
    public static String CREATED_ITEM = "Item created!";

    @Setting(value = "giveModifierItem")
    public static String GIVE_MODIFIER_ITEM = "Modifier item given!";
}
