package net.draycia.minetinkersponge.modifiers;

import org.spongepowered.api.item.inventory.ItemStack;

public class ModifierApplicationResult {

    private ItemStack itemStack;
    private boolean wasSuccess;
    private String reason;

    /**
     *
     * @param itemStack The resulting item from the operation. If null, {@link #wasSuccess()} will return false.
     */
    ModifierApplicationResult(ItemStack itemStack, String reason) {
        this.itemStack = itemStack;
        this.wasSuccess = itemStack != null;
        this.reason = reason;
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

    /**
     *
     * @return If the result is a failure, the reason why. Otherwise, blank.
     */
    public String getReason() {
        return reason;
    }
}
