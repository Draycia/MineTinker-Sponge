package net.draycia.minetinkersponge.data;

import com.google.common.reflect.TypeToken;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.value.mutable.Value;
import org.spongepowered.api.util.TypeTokens;

import java.util.Map;


public class MTKeys {

    public static final Key<Value<Boolean>> IS_MINETINKER;
    public static final Key<Value<Boolean>> IS_MT_TOOL;
    public static final Key<Value<Boolean>> IS_MT_ARMOR;

//    public static final Key<Value<List<String>>> MINETINKER_MODIFIERS;

    public static final Key<Value<Map<String, Integer>>> ITEM_MODIFIERS;

    static {
        IS_MINETINKER = Key.builder()
                .type(TypeTokens.BOOLEAN_VALUE_TOKEN)
                .id("minetinker-sponge")
                .name("Is MineTinker")
                .query(DataQuery.of("minetinker.is_compatible"))
                .build();

        IS_MT_TOOL = Key.builder()
                .type(TypeTokens.BOOLEAN_VALUE_TOKEN)
                .id("minetinker-sponge")
                .name("Is MineTinker Tool")
                .query(DataQuery.of("minetinker.is_tool"))
                .build();

        IS_MT_ARMOR = Key.builder()
                .type(TypeTokens.BOOLEAN_VALUE_TOKEN)
                .id("minetinker-sponge")
                .name("Is MineTinker Armor")
                .query(DataQuery.of("minetinker.is_armor"))
                .build();

        ITEM_MODIFIERS = Key.builder()
                .type(new TypeToken<Value<Map<String, Integer>>>() {})
                .id("minetinker-sponge")
                .name("Item Modifiers")
                .query(DataQuery.of("minetinker.item_modifiers"))
                .build();
    }

    static void dummy() {} // invoke static constructor
}
