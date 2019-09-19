package net.draycia.minetinkersponge.listeners;

import net.draycia.minetinkersponge.modifiers.ModManager;
import net.draycia.minetinkersponge.modifiers.Modifier;
import net.draycia.minetinkersponge.utils.MTConfig;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.Item;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.InteractBlockEvent;
import org.spongepowered.api.event.filter.cause.Root;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.enchantment.Enchantment;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InteractListener {

    private ModManager modManager;

    public InteractListener(ModManager modManager) {
        this.modManager = modManager;
    }

    @Listener
    public void onBlockInteract(InteractBlockEvent.Secondary event, @Root Player player) {
        // Ensure the player has permission to convert enchantments to modifiers
        if (!player.hasPermission("minetinker.convertenchants")) {
            return;
        }

        // Check if the interacted block is the one used for conversion
        if (event.getTargetBlock().getState().getType() != MTConfig.ENCHANTMENT_CONVERT_BLOCK) {
            return;
        }

        // Get the item used in the event
        Optional<ItemStack> item = player.getItemInHand(event.getHandType());

        if (item.isPresent()) {
            ItemStack itemStack = item.get();

            // Check if the item is an enchanted book
            if (itemStack.getType() != ItemTypes.ENCHANTED_BOOK) {
                return;
            }

            Optional<List<Enchantment>> enchantments = itemStack.get(Keys.STORED_ENCHANTMENTS);

            if (enchantments.isPresent()) {
                List<Enchantment> enchants = enchantments.get();
                List<Enchantment> enchantsToRemove = new ArrayList<>();

                // Loop through all enchantments the book has
                for (Enchantment enchantment : enchants) {
                    // Try to find any modifier that applies the enchantment
                    Optional<Modifier> modifier = modManager.getFirstModifierByEnchantment(enchantment.getType());

                    if (modifier.isPresent()) {
                        System.out.println("Modifier is present!");
                        // Attempt to spawn the item on the ground near the interacted block

                        // Give the player the modifier
                        ItemStack modifierItem = modifier.get().getModifierItem(enchantment.getLevel());

                        if (player.getInventory().canFit(modifierItem)) {
                            player.getInventory().offer(modifierItem);
                        } else {
                            Location<World> location = player.getLocation();

                            Entity itemEntity = location.createEntity(EntityTypes.ITEM);
                            itemEntity.offer(Keys.ACTIVE_ITEM, modifierItem.createSnapshot());

                            location.spawnEntity(itemEntity);
                        }

                        // Remove the enchantment from the item
                        enchantsToRemove.add(enchantment);
                    }
                }

                enchants.removeAll(enchantsToRemove);

                if (enchants.isEmpty()) {
                    player.setItemInHand(event.getHandType(), ItemStack.empty());
                } else {
                    itemStack.offer(Keys.STORED_ENCHANTMENTS, enchants);
                }

            }
        }
    }

}
