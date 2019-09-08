package net.draycia.minetinkersponge.modifiers.impls.potioneffects;

import net.draycia.minetinkersponge.modifiers.ModManager;
import net.draycia.minetinkersponge.modifiers.Modifier;
import net.draycia.minetinkersponge.utils.ItemTypeUtils;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.manipulator.mutable.PotionEffectData;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.effect.potion.PotionEffect;
import org.spongepowered.api.effect.potion.PotionEffectTypes;
import org.spongepowered.api.entity.living.Living;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.DamageEntityEvent;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.ItemTypes;

import java.util.ArrayList;
import java.util.List;

public class Poisonous extends Modifier {

    private ModManager modManager;

    @Override
    public String getName() {
        return "Poisonous";
    }

    @Override
    public int getMaxLevel() {
        return 5;
    }

    @Override
    public int getLevelWeight() {
        return 1;
    }

    @Override
    public ItemType getModifierItemType() {
        return ItemTypes.POISONOUS_POTATO;
    }

    @Override
    public List<ItemType> getCompatibleItems() {
        return ItemTypeUtils.getWeaponTypes();
    }

    @Override
    public void onModifierRegister(Object plugin) {
        Sponge.getEventManager().registerListeners(plugin, this);
    }

    public Poisonous(ModManager modManager) {
        this.modManager = modManager;
    }

    @Listener
    public void onItemDrop(DamageEntityEvent event) {
        if (!(event.getTargetEntity() instanceof Living)) {
            return;
        }

        event.getCause().first(Player.class).ifPresent(player -> {
            player.getItemInHand(HandTypes.MAIN_HAND).ifPresent(itemStack -> {
                if (modManager.itemHasModifier(itemStack, this)) {
                    int level = modManager.getModifierLevel(itemStack, this);

                    PotionEffectData potionEffects = event.getTargetEntity().getOrCreate(PotionEffectData.class).get();
                    potionEffects.addElement(PotionEffect.builder().potionType(PotionEffectTypes.POISON).duration(level * 20).amplifier(1).build());

                    System.out.println(event.getTargetEntity().offer(potionEffects).isSuccessful());
                }
            });
        });
    }
}
