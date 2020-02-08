package net.draycia.minetinkersponge.utils;

import com.google.common.collect.ImmutableList;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.item.ItemType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

public class ItemTypeUtils {

    private List<ItemType> getItemsByPattern(String pattern) {
        List<ItemType> itemTypeList = new ArrayList<>();
        Pattern regex = Pattern.compile(pattern);

        for (ItemType itemType : Sponge.getGame().getRegistry().getAllOf(ItemType.class)) {
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

    private List<ItemType> getItemsByConfig(String pattern) {
        List<ItemType> itemTypeList = new ArrayList<>();

        List<String> customEntries = MTConfig.CUSTOM_ITEM_PATTERNS.get(pattern);

        if (customEntries != null) {
            for (String entry : customEntries) {
                Sponge.getGame().getRegistry().getType(ItemType.class, entry).ifPresent(itemTypeList::add);
            }
        }

        return itemTypeList;
    }

    private ImmutableList<ItemType> getItems(String pattern) {
        return ImmutableList.<ItemType>builder()
                .addAll(getItemsByPattern(pattern))
                .addAll(getItemsByConfig(pattern))
                .build();
    }

    // Tools
    public final ImmutableList<ItemType> PICKAXES = getItems("pickaxe");
    public final ImmutableList<ItemType> AXES = getItems("axe");
    public final ImmutableList<ItemType> SHOVELS = getItems("shovel");
    public final ImmutableList<ItemType> HOES = getItems("hoe");
    public final ImmutableList<ItemType> FISHING_RODS = getItems("fishing_rod");

    // Weapons
    public final ImmutableList<ItemType> SWORDS = getItems("sword");
    public final ImmutableList<ItemType> BOWS = getItems("bow");

    // Armor
    public final ImmutableList<ItemType> HELMETS = getItems("helmet");
    public final ImmutableList<ItemType> CHESTPLATES = getItems("chestplate");
    public final ImmutableList<ItemType> LEGGINGS = getItems("legging");
    public final ImmutableList<ItemType> BOOTS = getItems("boot");
    public final ImmutableList<ItemType> SHIELDS = getItems("shield");

    // All
    public final ImmutableList<ItemType> ALL_TYPES = ImmutableList.<ItemType>builder()
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
