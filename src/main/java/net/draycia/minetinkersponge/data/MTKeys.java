package net.draycia.minetinkersponge.data;

import com.google.common.reflect.TypeToken;
import org.spongepowered.api.CatalogKey;
import org.spongepowered.api.data.Key;
import org.spongepowered.api.data.value.MapValue;
import org.spongepowered.api.data.value.Value;

public class MTKeys {

    public static final Key<Value<Boolean>> IS_MINETINKER;

    public static final Key<Value<Integer>> MINETINKER_LEVEL;
    public static final Key<Value<Integer>> MINETINKER_SLOTS;
    public static final Key<Value<Integer>> MINETINKER_XP;

    public static final Key<Value<String>> MODIFIER_ID;

    public static final Key<MapValue<String, Integer>> ITEM_MODIFIERS;

    static {
        IS_MINETINKER = Key.builder()
                .type(new TypeToken<Value<Boolean>>() {})
                .key(CatalogKey.builder().namespace("minetinker.is_compatible").build())
                .build();

        MINETINKER_LEVEL = Key.builder()
                .type(new TypeToken<Value<Integer>>() {})
                .key(CatalogKey.builder().namespace("minetinker.minetinker_level").build())
                .build();

        MINETINKER_XP = Key.builder()
                .type(new TypeToken<Value<Integer>>() {})
                .key(CatalogKey.builder().namespace("minetinker.minetinker_xp").build())
                .build();

        MINETINKER_SLOTS = Key.builder()
                .type(new TypeToken<Value<Integer>>() {})
                .key(CatalogKey.builder().namespace("minetinker.minetinker_slots").build())
                .build();

        MODIFIER_ID = Key.builder()
                .type(new TypeToken<Value<String>>() {})
                .key(CatalogKey.builder().namespace("minetinker.modifier_id").build())
                .build();

        ITEM_MODIFIERS = Key.builder()
                .type(new TypeToken<MapValue<String, Integer>>() {})
                .key(CatalogKey.builder().namespace("minetinker.item_modifiers").build())
                .build();
    }

    static void dummy() {} // invoke static constructor
}
