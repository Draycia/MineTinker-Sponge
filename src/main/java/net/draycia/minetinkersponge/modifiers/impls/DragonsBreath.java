package net.draycia.minetinkersponge.modifiers.impls;

import com.google.common.collect.ImmutableList;
import net.draycia.minetinkersponge.managers.ModManager;
import net.draycia.minetinkersponge.modifiers.Modifier;
import net.draycia.minetinkersponge.utils.ItemTypeUtils;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.effect.particle.ParticleEffect;
import org.spongepowered.api.effect.particle.ParticleOptions;
import org.spongepowered.api.effect.particle.ParticleTypes;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.Living;
import org.spongepowered.api.entity.living.monster.Monster;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.cause.EventContextKeys;
import org.spongepowered.api.event.cause.entity.damage.source.DamageSource;
import org.spongepowered.api.event.cause.entity.damage.source.DamageSources;
import org.spongepowered.api.event.entity.DamageEntityEvent;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.recipe.crafting.CraftingRecipe;
import org.spongepowered.api.item.recipe.crafting.Ingredient;
import org.spongepowered.api.item.recipe.crafting.ShapedCraftingRecipe;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.util.Direction;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class DragonsBreath extends Modifier {

    private List<ItemType> compatibleTypes = ImmutableList.<ItemType>builder()
            .addAll(ItemTypeUtils.BOWS)
            .addAll(ItemTypeUtils.SWORDS)
            .addAll(ItemTypeUtils.AXES)
            .build();

    @Override
    public List<ItemType> getCompatibleItems() {
        return compatibleTypes;
    }

    @Override
    public Text getCompatibilityString() {
        return Text.of("Bows, Swords, and Axes.");
    }

    @Override
    public Text getName() {
        return getName(Text.of("Dragon's Breath"));
    }

    @Override
    public int getMaxLevel() {
        return getMaxLevel(3);
    }

    @Override
    public int getLevelWeight() {
        return getLevelWeight(1);
    }

    @Override
    public ItemType getModifierItemType() {
        return getModifierItemType(ItemTypes.DRAGON_BREATH);
    }

    @Override
    public void onModifierRegister(Object plugin) {
        Sponge.getEventManager().registerListeners(plugin, this);
    }

    @Override
    public Optional<CraftingRecipe> getRecipe() {

        ShapedCraftingRecipe recipe = ShapedCraftingRecipe.builder()
                .aisle("IFI", "FCF", "IFI")
                .where('F', Ingredient.of(ItemTypes.BLAZE_POWDER))
                .where('C', Ingredient.of(ItemTypes.ENDER_EYE))
                .where('I', Ingredient.of(ItemTypes.DRAGON_BREATH))
                .result(getModifierItem())
                .id(getKey())
                .build();

        return Optional.of(getCraftingRecipe(recipe));
    }

    @Override
    public String getDescription() {
        return getDescription("If damaged mob is on fire, sets nearby hostile mobs on fire.");
    }

    private ParticleEffect dragonBreath = ParticleEffect.builder()
            .type(ParticleTypes.DRAGON_BREATH)
            .option(ParticleOptions.DIRECTION, Direction.NONE)
            .build();

    @Listener
    public void onEntityDamagedByEntity(DamageEntityEvent event) {
        if (!(event.getTargetEntity() instanceof Living)) {
            return;
        }

        Optional<DamageSource> source = event.getCause().first(DamageSource.class);

        if (source.isPresent() && source.get() == DamageSources.FIRE_TICK) {
            return;
        }

        Entity entity = event.getTargetEntity();

        if (entity.get(Keys.FIRE_TICKS).orElse(0) <= 0) {
            return;
        }

        event.getContext().get(EventContextKeys.OWNER).flatMap(User::getPlayer).ifPresent(player -> {
            if (!player.get(Keys.IS_SNEAKING).orElse(false)) {
                return;
            }

            player.getItemInHand(HandTypes.MAIN_HAND).ifPresent(itemStack -> {
                if (ModManager.itemHasModifier(itemStack, this)) {
                    int level = ModManager.getModifierLevel(itemStack, this);

                    Collection<Entity> entities = entity.getNearbyEntities(level + 1);

                    for (Entity target : entities) {
                        if (target instanceof Monster) {
                            target.offer(Keys.FIRE_TICKS, 40 * level);
                            target.getLocation().getExtent().spawnParticles(dragonBreath, target.getLocation().getPosition());
                        } else if (target instanceof Player) {
                            // TODO: check if party / PVP
                        }
                    }
                }
            });
        });
    }

}
