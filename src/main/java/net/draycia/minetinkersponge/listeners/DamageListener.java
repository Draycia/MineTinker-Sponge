package net.draycia.minetinkersponge.listeners;

import net.draycia.minetinkersponge.data.MTKeys;
import net.draycia.minetinkersponge.modifiers.ModManager;
import net.draycia.minetinkersponge.utils.MTConfig;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.cause.entity.damage.DamageFunction;
import org.spongepowered.api.event.cause.entity.damage.DamageModifierTypes;
import org.spongepowered.api.event.cause.entity.damage.DamageType;
import org.spongepowered.api.event.cause.entity.damage.DamageTypes;
import org.spongepowered.api.event.cause.entity.damage.source.DamageSource;
import org.spongepowered.api.event.entity.DamageEntityEvent;
import org.spongepowered.api.event.filter.cause.Root;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;
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
        if (!(event.getTargetEntity() instanceof Player)) {
            return;
        }

        Player player = (Player)event.getTargetEntity();

        boolean giveShieldXP = false;
        int experienceToGive = MTConfig.SHIELD_XP_PER_BLOCK;

        for (DamageFunction damageFunction : event.getModifiers()) {
            if (damageFunction.getModifier().getType() == DamageModifierTypes.SHIELD) {
                giveShieldXP = true;

                if (MTConfig.SHIELD_XP_IS_DAMAGE_REDUCED) {
                    double newDamage = damageFunction.getFunction().applyAsDouble(event.getBaseDamage());

                    experienceToGive = Math.max(1, (int)Math.round(event.getBaseDamage() - newDamage));
                }
            }
        }

        if (giveShieldXP) {
            Optional<ItemStack> mainHand = player.getItemInHand(HandTypes.MAIN_HAND);
            Optional<ItemStack> offHand = player.getItemInHand(HandTypes.OFF_HAND);

            if (mainHand.isPresent()) {
                modManager.addExperience(mainHand.get(), experienceToGive);
            }

            if (offHand.isPresent()) {
                modManager.addExperience(offHand.get(), experienceToGive);
            }
        }

        DamageType type = source.getType();

        if (type == DamageTypes.ATTACK || type == DamageTypes.PROJECTILE || type == DamageTypes.GENERIC) {
            EquipmentInventory inventory = player.getInventory().query(QueryOperationTypes.INVENTORY_TYPE.of(EquipmentInventory.class));

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
