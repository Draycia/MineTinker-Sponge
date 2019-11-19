package net.draycia.minetinkersponge.listeners;

import net.draycia.minetinkersponge.data.MTKeys;
import net.draycia.minetinkersponge.managers.ModManager;
import net.draycia.minetinkersponge.modifiers.Modifier;
import net.draycia.minetinkersponge.modifiers.ModifierApplicationResult;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.item.inventory.ClickInventoryEvent;
import org.spongepowered.api.event.item.inventory.UpdateAnvilEvent;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.InventoryArchetypes;
import org.spongepowered.api.item.inventory.ItemStack;
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

        ItemStack input = event.getLeft().createStack();

        Optional<Boolean> isMineTinker = input.get(MTKeys.IS_MINETINKER);

        if (!isMineTinker.isPresent() || !isMineTinker.get()) {
            return;
        }

        Optional<String> modifierId = event.getRight().createStack().get(MTKeys.MODIFIER_ID);

        if (!modifierId.isPresent()) {
            return;
        }

        Optional<Modifier> optionalModifier = modManager.getModifier(modifierId.get());

        if (!optionalModifier.isPresent()) {
            return;
        }

        Modifier modifier = optionalModifier.get();

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
    }

    @Listener
    public void onAnvilClick(ClickInventoryEvent event, @First Player player) {
        if (event.getTargetInventory().getArchetype() != InventoryArchetypes.ANVIL) {
            return;
        }

        Inventory input = event.getTargetInventory().query(QueryOperationTypes.INVENTORY_TYPE.of(InputSlot.class));

        ItemStack left = null;
        ItemStack right = null;
        ItemStack result = null;

        for (Inventory slot : input.slots()) {
            Optional<ItemStack> itemStack = slot.peek();

            if (itemStack.isPresent()) {
                if (left == null) {
                    left = itemStack.get();
                } else if (right == null) {
                    right = itemStack.get();
                }
            }
        }

        Inventory output = event.getTargetInventory().query(QueryOperationTypes.INVENTORY_TYPE.of(OutputSlot.class));

        Optional<ItemStack> outputItem = output.peek();

        if (outputItem.isPresent()) {
            result = outputItem.get();
        }

        if (left == null || right == null || result == null) {
            return;
        }

        if (!event.getSlot().isPresent() || !event.getSlot().get().peek().isPresent()) {
            return;
        }

        if (event.getSlot().get().peek().get().equalTo(result)) {
            Optional<String> modifierId = right.get(MTKeys.MODIFIER_ID);

            if (!modifierId.isPresent()) {
                return;
            }

            Optional<Modifier> modifier = modManager.getModifier(modifierId.get());

            int slots = modManager.getItemModifierSlots(left);
            int quantity = right.getQuantity();

            int amount = Math.min(slots, quantity);

            if (!modifier.isPresent()) {
                return;
            }

            ModifierApplicationResult applicationResult = modManager.applyModifier(left, modifier.get(), false, false, amount);

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
        }
    }
}
