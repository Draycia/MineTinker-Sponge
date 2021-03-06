package net.draycia.minetinkersponge.listeners;

import net.draycia.minetinkersponge.data.MTKeys;
import net.draycia.minetinkersponge.managers.ModManager;
import net.draycia.minetinkersponge.utils.MTConfig;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.cause.EventContextKeys;
import org.spongepowered.api.event.cause.entity.damage.DamageFunction;
import org.spongepowered.api.event.cause.entity.damage.DamageModifierTypes;
import org.spongepowered.api.event.cause.entity.damage.DamageType;
import org.spongepowered.api.event.cause.entity.damage.DamageTypes;
import org.spongepowered.api.event.cause.entity.damage.source.DamageSource;
import org.spongepowered.api.event.cause.entity.damage.source.DamageSources;
import org.spongepowered.api.event.entity.DamageEntityEvent;
import org.spongepowered.api.event.filter.cause.Root;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.equipment.EquipmentInventory;
import org.spongepowered.api.item.inventory.query.QueryOperationTypes;

import java.util.Optional;

public class DamageListener {

    @Listener
    public void onPlayerDamage(DamageEntityEvent event, @Root DamageSource source) {
        if (event.getFinalDamage() <= 0) {
            return;
        }

        if (source == DamageSources.FIRE_TICK) {
            return;
        }

        if (!(event.getTargetEntity() instanceof Player)) {
            event.getContext().get(EventContextKeys.OWNER)
                    .flatMap(User::getPlayer)
                    .flatMap(player -> player.getItemInHand(HandTypes.MAIN_HAND))
                    .ifPresent(mainHand -> {

                if (mainHand.get(MTKeys.IS_MINETINKER).orElse(false)) {
                    ModManager.addExperience(mainHand, 1);
                }
            });

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

            if (mainHand.isPresent()) {
                ModManager.addExperience(mainHand.get(), experienceToGive);
            } else {
                Optional<ItemStack> offHand = player.getItemInHand(HandTypes.OFF_HAND);

                if (offHand.isPresent()) {
                    ModManager.addExperience(offHand.get(), experienceToGive);
                }
            }

        }

        DamageType type = source.getType();

        if (type == DamageTypes.ATTACK || type == DamageTypes.PROJECTILE || type == DamageTypes.GENERIC) {
            EquipmentInventory inventory = player.getInventory().query(QueryOperationTypes.INVENTORY_TYPE.of(EquipmentInventory.class));

            for (Inventory slot : inventory.slots()) {
                slot.peek().ifPresent(itemStack -> {
                    Boolean isMineTinker = itemStack.get(MTKeys.IS_MINETINKER).orElse(false);

                    if (isMineTinker) {
                        ModManager.addExperience(itemStack, 1);
                        slot.set(itemStack); // This is necessary. I assume somewhere the item is copied.
                    }

                });

            }
        }
    }

}
