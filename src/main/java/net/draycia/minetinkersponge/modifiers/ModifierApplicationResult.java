package net.draycia.minetinkersponge.modifiers;

import org.spongepowered.api.item.inventory.ItemStack;

public class ModifierApplicationResult {

    private ItemStack itemStack;
    private boolean wasSuccess;

    ModifierApplicationResult(ItemStack itemStack, boolean wasSuccess) {
        this.itemStack = itemStack;
        this.wasSuccess = wasSuccess;
    }

    public boolean wasSuccess() {
        return wasSuccess;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }
}
