package net.draycia.minetinkersponge.listeners;

import net.draycia.minetinkersponge.data.MTKeys;
import net.draycia.minetinkersponge.managers.ModManager;
import net.draycia.minetinkersponge.modifiers.Modifier;
import net.draycia.minetinkersponge.modifiers.ModifierApplicationResult;
import net.draycia.minetinkersponge.utils.MTConfig;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.item.inventory.ClickInventoryEvent;
import org.spongepowered.api.event.item.inventory.UpdateAnvilEvent;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.InventoryArchetypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.property.SlotPos;
import org.spongepowered.api.item.inventory.query.QueryOperationTypes;
import org.spongepowered.api.item.inventory.slot.InputSlot;
import org.spongepowered.api.item.inventory.slot.OutputSlot;

import java.util.Optional;

public class AnvilListener {

    private ModManager modManager;

    public AnvilListener(ModManager modManager) {
        this.modManager = modManager;
    }

    @Listener
    public void onAnvilUpdate(UpdateAnvilEvent event) {
        if (event.getLeft().isEmpty() || event.getRight().isEmpty()) {
            return;
        }

        event.getLeft().createStack().get(MTKeys.IS_MINETINKER)
                .flatMap(state -> event.getRight().createStack().get(MTKeys.MODIFIER_ID))
                .flatMap(modifierId -> modManager.getModifier(modifierId))
                .ifPresent(modifier -> {

            ItemStack left = event.getLeft().createStack();
            ItemStack right = event.getRight().createStack();

            int slots = modManager.getItemModifierSlots(left);
            int quantity = right.getQuantity();

            int amount = Math.min(slots, quantity);

            ModifierApplicationResult result = modManager.applyModifier(left, modifier, false, true, amount);

            if (result.wasSuccess()) {
                event.getResult().setCustom(result.getItemStack().createSnapshot());
                event.getResult().setValid(true);
            }
        });
    }

    @Listener
    public void onAnvilClick(ClickInventoryEvent event, @First Player player) {
        if (event.getTargetInventory().getArchetype() != InventoryArchetypes.ANVIL) {
            return;
        }

        Inventory inventory = event.getTargetInventory();

        Inventory input = event.getTargetInventory().query(QueryOperationTypes.INVENTORY_TYPE.of(InputSlot.class));
        Inventory output = event.getTargetInventory().query(QueryOperationTypes.INVENTORY_TYPE.of(OutputSlot.class));

        ItemStack left = inventory.query(SlotPos.of(0)).peek().orElse(null);
        ItemStack right = inventory.query(SlotPos.of(1)).peek().orElse(null);
        ItemStack result = output.peek().orElse(null);

        if (left == null || right == null || result == null) {
            return;
        }

        event.getSlot().flatMap(Inventory::peek).ifPresent(slotPeek -> {
            if (MTConfig.DISABLE_ENCHANTED_BOOKS && right.getType() == ItemTypes.ENCHANTED_BOOK) {
                output.set(ItemStack.empty());

                return;
            }

            if (slotPeek.equalTo(result)) {
                right.get(MTKeys.MODIFIER_ID).flatMap(modifierId -> modManager.getModifier(modifierId)).ifPresent(modifier -> {
                    int slots = modManager.getItemModifierSlots(left);
                    int quantity = right.getQuantity();

                    int amount = Math.min(slots, quantity);

                    ModifierApplicationResult applicationResult = modManager.applyModifier(left, modifier, false, false, amount);

                    if (applicationResult.wasSuccess()) {
                        event.getCursorTransaction().setValid(true);
                        event.getCursorTransaction().setCustom(applicationResult.getItemStack().createSnapshot());

                        boolean isSecond = false;

                        for (Inventory slot : input.slots()) {
                            if (isSecond) {
                                right.setQuantity(right.getQuantity() - amount);
                                slot.set(right);
                            } else {
                                slot.set(ItemStack.builder().itemType(ItemTypes.AIR).quantity(1).build());
                                isSecond = true;
                            }
                        }

                        output.set(ItemStack.builder().itemType(ItemTypes.AIR).quantity(1).build());
                    }
                });
            }
        });

        // TODO: config option

    }
}
