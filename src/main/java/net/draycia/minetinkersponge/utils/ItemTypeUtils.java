package net.draycia.minetinkersponge.utils;

import com.google.common.collect.ImmutableList;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.item.ItemType;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class ItemTypeUtils {

    public static List<ItemType> getItemsByPattern(String pattern) {
        List<ItemType> itemTypeList = new ArrayList<>();
        Pattern regex = Pattern.compile(pattern);

        for (ItemType itemType : Sponge.getGame().getRegistry().getAllOf(ItemType.class)) {
            // TODO: Blacklist in config
            if (MTConfig.BLACKLISTED_ITEM_TYPES.contains(itemType.getId())) {
                continue;
            }

            // TODO: Allow additional entries to be made in each modifier's config
            if (regex.matcher(itemType.getId().split(":")[1]).find()) {
                itemTypeList.add(itemType);
            }
        }

        return itemTypeList;
    }

    public static List<ItemType> getItemsByConfig(String pattern) {
        List<ItemType> itemTypeList = new ArrayList<>();
        // TODO: Stub, implement
        return itemTypeList;
    }

    private static ImmutableList<ItemType> getItems(String pattern) {
        return ImmutableList.<ItemType>builder()
                .addAll(getItemsByPattern(pattern))
                .addAll(getItemsByConfig(pattern))
                .build();
    }

    // Tools
    public static final ImmutableList<ItemType> PICKAXES = getItems("pickaxe");
    public static final ImmutableList<ItemType> AXES = getItems("axe");
    public static final ImmutableList<ItemType> SHOVELS = getItems("shovel");
    public static final ImmutableList<ItemType> HOES = getItems("hoe");
    public static final ImmutableList<ItemType> FISHING_RODS = getItems("fishing_rod");

    // Weapons
    public static final ImmutableList<ItemType> SWORDS = getItems("sword");
    public static final ImmutableList<ItemType> BOWS = getItems("bow");

    // Armor
    public static final ImmutableList<ItemType> HELMETS = getItems("helmet");
    public static final ImmutableList<ItemType> CHESTPLATES = getItems("chestplate");
    public static final ImmutableList<ItemType> LEGGINGS = getItems("legging");
    public static final ImmutableList<ItemType> BOOTS = getItems("boot");
    public static final ImmutableList<ItemType> SHIELDS = getItems("shield");

    // All
    public static final ImmutableList<ItemType> ALL_TYPES = ImmutableList.<ItemType>builder()
            .addAll(PICKAXES).addAll(AXES).addAll(SHOVELS).addAll(HOES).addAll(FISHING_RODS)
            .addAll(SWORDS).addAll(BOWS).addAll(HELMETS).addAll(CHESTPLATES).addAll(LEGGINGS)
            .addAll(BOOTS).addAll(SHIELDS)
            .build();

    public static int getMaterialCost(ItemType itemType) {
        String id = itemType.getId();

        if (id.contains("chestplate")) {
            return 8; // Chestplate
        } else if (id.contains("leggings")) {
            return 7; // Leggings
        } else if (id.contains("helmet")) {
            return 5; // Helmet
        } else if (id.contains("boots")) {
            return 4; // Boots
        } else if (id.contains("axe")) {
            return 3; // Pickaxe and Axe
        } else if (id.contains("sword") || id.contains("hoe")) {
            return 2; // Sword and Hoe
        } else {
            return 1; // Shovel
        }
    }
}
