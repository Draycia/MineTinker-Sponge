package net.draycia.minetinkersponge.modifiers.impls.potioneffects;

import net.draycia.minetinkersponge.modifiers.ModManager;
import net.draycia.minetinkersponge.modifiers.Modifier;
import net.draycia.minetinkersponge.utils.CompositeUnmodifiableList;
import net.draycia.minetinkersponge.utils.ItemTypeUtils;
import org.spongepowered.api.Sponge;
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
import org.spongepowered.api.item.inventory.ItemStack;

import java.util.List;
import java.util.Optional;

public class InstantDamage extends Modifier {

    private ModManager modManager;

    @Override
    public String getName() {
        return "Instant Damage";
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public int getLevelWeight() {
        return 1;
    }

    @Override
    public ItemType getModifierItemType() {
        return ItemTypes.COAL;
    }

    @Override
    public List<ItemType> getCompatibleItems() {
        return new CompositeUnmodifiableList<>(ItemTypeUtils.getSwordTypes(), ItemTypeUtils.getAxeTypes());
    }

    @Override
    public void onModifierRegister(Object plugin) {
        Sponge.getEventManager().registerListeners(plugin, this);
    }

    public InstantDamage(ModManager modManager) {
        this.modManager = modManager;
    }

    @Listener
    public void onItemDrop(DamageEntityEvent event) {
        if (!(event.getTargetEntity() instanceof Living)) {
            return;
        }

        Optional<Player> player = event.getCause().first(Player.class);

        if (player.isPresent()) {
            Optional<ItemStack> itemStack = player.get().getItemInHand(HandTypes.MAIN_HAND);

            if (itemStack.isPresent()) {
                if (modManager.itemHasModifier(itemStack.get(), this)) {
                    PotionEffectData potionEffects = event.getTargetEntity().getOrCreate(PotionEffectData.class).get();
                    potionEffects.addElement(PotionEffect.builder().potionType(PotionEffectTypes.INSTANT_DAMAGE).duration(1).amplifier(1).build());
                }
            }
        }
    }
}
