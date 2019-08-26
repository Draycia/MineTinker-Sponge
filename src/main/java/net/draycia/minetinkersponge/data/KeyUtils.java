package net.draycia.minetinkersponge.data;

import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.value.mutable.Value;
import org.spongepowered.api.item.inventory.ItemStack;

public class KeyUtils {

    public static boolean itemHasKeyTrue(ItemStack itemStack, Key<Value<Boolean>> key) {
        return itemStack.get(key).orElse(false);
    }
}
