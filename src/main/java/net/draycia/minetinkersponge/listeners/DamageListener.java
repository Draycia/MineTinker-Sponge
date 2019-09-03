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

import java.util.Optional;

public class DamageListener {

    private ModManager modManager;

    public DamageListener(ModManager modManager) {
        this.modManager = modManager;
    }

    @Listener
    public void onPlayerDamage(DamageEntityEvent event, @Root DamageSource source) {
        if (!(event.getTargetEntity() instanceof Player)) {
            return;
        }

        Player player = (Player)event.getTargetEntity();
        DamageType type = source.getType();

        if (type == DamageTypes.ATTACK || type == DamageTypes.PROJECTILE || type == DamageTypes.GENERIC) {
            EquipmentInventory inventory = player.getInventory().query(EquipmentInventory.class);

            for (Inventory slot : inventory.slots()) {
                Optional<ItemStack> item = slot.peek();

                if (item.isPresent()) {
                    ItemStack itemStack = item.get();

                    Optional<Boolean> isMineTinker = itemStack.get(MTKeys.IS_MINETINKER);

                    if (isMineTinker.isPresent() && isMineTinker.get()) {
                        modManager.addExperience(itemStack, 1);
                        slot.set(itemStack); // This is necessary. I assume somewhere the item is copied.
                    }
                }
            }
        }
    }

}
