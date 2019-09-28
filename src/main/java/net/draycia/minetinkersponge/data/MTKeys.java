package net.draycia.minetinkersponge.data;

import com.google.common.reflect.TypeToken;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.value.mutable.MapValue;
import org.spongepowered.api.data.value.mutable.Value;
import org.spongepowered.api.util.TypeTokens;

public class MTKeys {

    public static final Key<Value<Boolean>> IS_MINETINKER;

    public static final Key<Value<Integer>> MINETINKER_LEVEL;
    public static final Key<Value<Integer>> MINETINKER_SLOTS;
    public static final Key<Value<Integer>> MINETINKER_XP;

    public static final Key<Value<String>> MODIFIER_ID;

    public static final Key<MapValue<String, Integer>> ITEM_MODIFIERS;

    static {
        IS_MINETINKER = Key.builder()
                .type(TypeTokens.BOOLEAN_VALUE_TOKEN)
                .id("minetinker-sponge")
                .name("Is MineTinker")
                .query(DataQuery.of("minetinker.is_compatible"))
                .build();

        MINETINKER_LEVEL = Key.builder()
                .type(TypeTokens.INTEGER_VALUE_TOKEN)
                .id("minetinker-sponge")
                .name("Item Level")
                .query(DataQuery.of("minetinker.minetinker_level"))
                .build();

        MINETINKER_XP = Key.builder()
                .type(TypeTokens.INTEGER_VALUE_TOKEN)
                .id("minetinker-sponge")
                .name("Item XP")
                .query(DataQuery.of("minetinker.minetinker_xp"))
                .build();

        MINETINKER_SLOTS = Key.builder()
                .type(TypeTokens.INTEGER_VALUE_TOKEN)
                .id("minetinker-sponge")
                .name("Item Modifier Slots")
                .query(DataQuery.of("minetinker.minetinker_slots"))
                .build();

        MODIFIER_ID = Key.builder()
                .type(TypeTokens.STRING_VALUE_TOKEN)
                .id("minetinker-sponge")
                .name("Modifier Item ID")
                .query(DataQuery.of("minetinker.modifier_id"))
                .build();

        ITEM_MODIFIERS = Key.builder()
                .type(new TypeToken<MapValue<String, Integer>>() {})
                .id("minetinker-sponge")
                .name("Item Modifiers")
                .query(DataQuery.of("minetinker.item_modifiers"))
                .build();
    }

    static void dummy() {} // invoke static constructor
}
