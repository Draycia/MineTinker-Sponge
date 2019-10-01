package net.draycia.minetinkersponge.modifiers;

import org.spongepowered.api.item.inventory.ItemStack;

public class ModifierApplicationResult {

    private ItemStack itemStack;
    private boolean wasSuccess;

    /**
     *
     * @param itemStack The resulting item from the operation. If null, {@link #wasSuccess()} will return false.
     */
    ModifierApplicationResult(ItemStack itemStack) {
        this.itemStack = itemStack;
        this.wasSuccess = itemStack != null;
    }

    /**
     *
     * @return If the operation was a success
     */
    public boolean wasSuccess() {
        return wasSuccess;
    }

    /**
     *
     * @return The resulting item from the operation. Null if the operation failed.
     */
    public ItemStack getItemStack() {
        return itemStack;
    }
}
