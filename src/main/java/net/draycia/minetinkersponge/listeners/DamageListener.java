package net.draycia.minetinkersponge.listeners;

import net.draycia.minetinkersponge.data.MTKeys;
import net.draycia.minetinkersponge.modifiers.ModManager;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.cause.entity.damage.DamageType;
import org.spongepowered.api.event.cause.entity.damage.DamageTypes;
import org.spongepowered.api.event.cause.entity.damage.source.DamageSource;
import org.spongepowered.api.event.entity.DamageEntityEvent;
import org.spongepowered.api.event.filter.cause.Root;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.equipment.EquipmentInventory;
import org.spongepowered.api.item.inventory.query.QueryOperationTypes;

import java.util.Optional;

public class DamageListener {

    private ModManager modManager;

    public DamageListener(ModManager modManager) {
        this.modManager = modManager;
    }

    @Listener
    public void onPlayerDamage(DamageEntityEvent event, @Root DamageSource source) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        Player player = (Player)event.getEntity();
        DamageType type = source.getType();

        if (type == DamageTypes.ATTACK || type == DamageTypes.PROJECTILE || type == DamageTypes.GENERIC) {
            Inventory inventory = player.getInventory().query(QueryOperationTypes.INVENTORY_TYPE.of(EquipmentInventory.class));

            for (Inventory slot : inventory.slots()) {
                ItemStack itemStack = slot.peek();

                Optional<Boolean> isMineTinker = itemStack.get(MTKeys.IS_MINETINKER);

                if (isMineTinker.isPresent() && isMineTinker.get()) {
                    modManager.addExperience(itemStack, 1);
                    slot.set(0, itemStack); // This is necessary. I assume somewhere the item is copied.
                }
            }
        }
    }

}
