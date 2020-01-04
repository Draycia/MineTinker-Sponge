package net.draycia.minetinkersponge.managers;

import net.draycia.minetinkersponge.data.MTKeys;
import net.draycia.minetinkersponge.modifiers.Modifier;
import net.draycia.minetinkersponge.utils.ItemTypeUtils;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.ItemStack;

import java.util.Map;
import java.util.Optional;

public class ItemLevelManager {

    /**
     *
     * @param player The player to get the combat level of
     * @param average If the returned number should be an average of all 5 slots, if false the returned number is a sum
     * @return The calculated combat level of the player
     */
    public int getPlayerCombatLevel(Player player, boolean average) {
        int helmet = 0;
        int chestplate = 0;
        int leggings = 0;
        int boots = 0;
        int weapon = 0;

        // Loop through all items in the player's inventory
        for (Inventory slot : player.getInventory().slots()) {
            // Get the item in the slot
            Optional<ItemStack> itemStackOptional = slot.peek();

            if (!itemStackOptional.isPresent()) {
                continue;
            }

            ItemStack itemStack = itemStackOptional.get();

            // Check if the item is MT compatible
            if (!itemStack.get(MTKeys.IS_MINETINKER).orElse(false)) {
                continue;
            }

            // Calculate the item's effective level
            Map<String, Integer> itemModifierLevels = ModManager.getItemModifierLevels(itemStack);

            int itemLevel = 0;

            for (Map.Entry<String, Integer> entry : itemModifierLevels.entrySet()) {
                Optional<Modifier> mod = ModManager.getModifier(entry.getKey());

                if (mod.isPresent()) {
                    itemLevel +=  mod.get().getLevelWeight() * entry.getValue();
                }
            }

            // Get the item's type
            ItemType itemType = itemStack.getType();

            // Set the slot's level to the higher of the two
            if (ItemTypeUtils.HELMETS.contains(itemType)) {
                helmet = Math.max(helmet, itemLevel);
            } else if (ItemTypeUtils.CHESTPLATES.contains(itemType)) {
                chestplate = Math.max(chestplate, itemLevel);
            } else if (ItemTypeUtils.LEGGINGS.contains(itemType)) {
                leggings = Math.max(leggings, itemLevel);
            } else if (ItemTypeUtils.BOOTS.contains(itemType)) {
                boots = Math.max(boots, itemLevel);
            }

            // TODO: Include weapons
//            else if (ItemTypeUtils.getWeaponTypes().contains(itemType)) {
//                weapon = Math.max(weapon, itemLevel);
//            }
        }

        // Return the result
        int sum = (helmet + chestplate + leggings + boots + weapon);

        return average ? sum / 5 : sum;
    }

}
