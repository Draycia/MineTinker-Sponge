package net.draycia.minetinkersponge.modifiers;

import org.spongepowered.api.item.inventory.ItemStack;

public class ModifierApplicationResult {

    private ItemStack itemStack;
    private boolean wasSuccess;

    ModifierApplicationResult(ItemStack itemStack) {
        this.itemStack = itemStack;
        this.wasSuccess = itemStack != null;
    }

    public boolean wasSuccess() {
        return wasSuccess;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }
}
