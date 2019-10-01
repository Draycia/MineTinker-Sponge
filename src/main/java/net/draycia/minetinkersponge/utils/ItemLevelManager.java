package net.draycia.minetinkersponge.utils;

import net.draycia.minetinkersponge.data.MTKeys;
import net.draycia.minetinkersponge.modifiers.ModManager;
import net.draycia.minetinkersponge.modifiers.Modifier;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.ItemStack;

import java.util.Map;
import java.util.Optional;

public class ItemLevelManager {

    private ModManager modManager;

    public ItemLevelManager(ModManager modManager) {
        this.modManager = modManager;
    }

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

        for (Inventory slot : player.getInventory().slots()) {
            Optional<ItemStack> itemStackOptional = slot.peek();

            if (!itemStackOptional.isPresent()) {
                continue;
            }

            ItemStack itemStack = itemStackOptional.get();

            if (!itemStack.get(MTKeys.IS_MINETINKER).orElse(false)) {
                continue;
            }

            Map<String, Integer> itemModifierLevels = modManager.getItemModifierLevels(itemStack);

            int itemLevel = 0;

            for (Map.Entry<String, Integer> entry : itemModifierLevels.entrySet()) {
                Optional<Modifier> mod = modManager.getModifier(entry.getKey());

                if (mod.isPresent()) {
                    itemLevel +=  mod.get().getLevelWeight() * entry.getValue();
                }
            }

            ItemType itemType = itemStack.getType();

            if (ItemTypeUtils.getHelmetTypes().contains(itemType)) {
                helmet = Math.max(helmet, itemLevel);
            } else if (ItemTypeUtils.getChestplateTypes().contains(itemType)) {
                chestplate = Math.max(chestplate, itemLevel);
            } else if (ItemTypeUtils.getLeggingTypes().contains(itemType)) {
                leggings = Math.max(leggings, itemLevel);
            } else if (ItemTypeUtils.getBootTypes().contains(itemType)) {
                boots = Math.max(boots, itemLevel);
            } else if (ItemTypeUtils.getWeaponTypes().contains(itemType)) {
                weapon = Math.max(weapon, itemLevel);
            }
        }

        if (average) {
            return (helmet + chestplate + leggings + boots + weapon) / 5;
        } else {
            return (helmet + chestplate + leggings + boots + weapon);
        }
    }

}
