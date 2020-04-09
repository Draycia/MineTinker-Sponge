package net.draycia.minetinkersponge.modifiers.impls;

import com.flowpowered.math.vector.Vector3d;
import com.google.common.collect.ImmutableList;
import net.draycia.minetinkersponge.MineTinkerSponge;
import net.draycia.minetinkersponge.managers.ModManager;
import net.draycia.minetinkersponge.modifiers.Modifier;
import org.spongepowered.api.GameState;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.value.immutable.ImmutableValue;
import org.spongepowered.api.effect.particle.ParticleEffect;
import org.spongepowered.api.effect.particle.ParticleTypes;
import org.spongepowered.api.effect.potion.PotionEffect;
import org.spongepowered.api.effect.potion.PotionEffectTypes;
import org.spongepowered.api.entity.living.Living;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.event.Cancellable;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.ChangeBlockEvent;
import org.spongepowered.api.event.cause.EventContextKeys;
import org.spongepowered.api.event.data.ChangeDataHolderEvent;
import org.spongepowered.api.event.entity.DamageEntityEvent;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.filter.cause.Root;
import org.spongepowered.api.event.item.inventory.UseItemStackEvent;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.recipe.crafting.CraftingRecipe;
import org.spongepowered.api.item.recipe.crafting.Ingredient;
import org.spongepowered.api.item.recipe.crafting.ShapedCraftingRecipe;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.Text;

import java.util.*;
import java.util.function.Consumer;

public class Stasis extends Modifier {

    private static List<ItemType> compatibleTypes = ImmutableList.<ItemType>builder()
            .addAll(MineTinkerSponge.getItemTypeUtils().CHESTPLATES)
            .build();

    private HashMap<UUID, Task> tasks = new HashMap<>();

    @Override
    public List<ItemType> getCompatibleItems() {
        return compatibleTypes;
    }

    @Override
    public Text getCompatibilityString() {
        return Text.of("Chestplates.");
    }

    @Override
    public Text getDisplayName() {
        return getName(Text.of("Stasis"));
    }

    @Override
    public int getMaxLevel() {
        return getMaxLevel(2);
    }

    @Override
    public int getLevelWeight() {
        return getLevelWeight(1);
    }

    @Override
    public ItemType getModifierItemType() {
        return getModifierItemType(ItemTypes.BONE_BLOCK);
    }

    private Object plugin;

    @Override
    public void onModifierRegister(Object plugin) {
        this.plugin = plugin;
        Sponge.getEventManager().registerListeners(plugin, this);
    }

    @Override
    public Optional<CraftingRecipe> getRecipe() {

        ShapedCraftingRecipe recipe = ShapedCraftingRecipe.builder()
                .aisle("ISI", "SES", "ISI")
                .where('I', Ingredient.of(ItemTypes.DIAMOND))
                .where('S', Ingredient.of(ItemTypes.BONE_BLOCK))
                .where('E', Ingredient.of(ItemTypes.BONE))
                .result(getModifierItem())
                .id(getId())
                .build();

        return Optional.of(getCraftingRecipe(recipe));
    }

    @Override
    public String getDescription() {
        return getDescription("When sneaking, prevents taking damage, prevents you dealing damage, and knocks back entities that get near you.");
    }

    @Listener
    public void onSneakToggle(ChangeDataHolderEvent.ValueChange event, @Root Player player) {
        for (ImmutableValue<?> successfulData : event.getEndResult().getSuccessfulData()) {
            if (successfulData.exists() && successfulData.getKey().equals(Keys.IS_SNEAKING)) {
                if (((ImmutableValue<Boolean>)successfulData).get()) {
                    // Is toggling sneak on
                } else {
                    return;
                    // Is toggling sneak off
                }

                if (!player.getChestplate().isPresent()) {
                    return;
                }

                int level = ModManager.getModifierLevel(player.getChestplate().get(), this);

                if (level <= 0) {
                    return;
                }

                Task task = Sponge.getScheduler().createTaskBuilder().execute(
                        new Consumer<Task>() {
                            double phi = 0;

                            @Override
                            public void accept(Task task) {
                                List<PotionEffect> effects = player.get(Keys.POTION_EFFECTS).orElse(new ArrayList<>());
                                effects.add(PotionEffect.builder().potionType(PotionEffectTypes.SLOWNESS).amplifier(10).duration(2).particles(false).build());
                                player.offer(Keys.POTION_EFFECTS, effects);

                                phi += Math.PI / 10;

                                for (double theta = 0; theta <= 2 * Math.PI; theta += Math.PI / 10) {
                                    double r = 1.5 + (level - 1);
                                    double x = r * Math.cos(theta) * Math.sin(phi);
                                    double y = r * Math.cos(phi) + 1;
                                    double z = r * Math.sin(theta) * Math.sin(phi);

                                    Vector3d particle = player.getLocation().getPosition().add(x, y, z);

                                    if (level >= 2) {
                                        Vector3d playerPos = player.getLocation().getPosition();

                                        player.getLocation().getExtent().getNearbyEntities(playerPos, r).forEach(entity -> {
                                            if (entity instanceof Living && !entity.equals(player)) {
                                                MineTinkerSponge.knockbackLiving(playerPos, (Living)entity, 1);
                                            }
                                        });
                                    }

                                    player.getLocation().getExtent().spawnParticles(ParticleEffect.builder().type(ParticleTypes.FLAME).build(), particle);
                                }

                                if (!player.require(Keys.IS_SNEAKING) || !tasks.containsKey(player.getUniqueId()) || Sponge.getGame().getState() == GameState.GAME_STOPPING) {
                                    task.cancel();
                                    tasks.remove(player.getUniqueId());
                                }
                            }
                        }
                ).intervalTicks(2).submit(plugin);

                tasks.put(player.getUniqueId(), task);

                Sponge.getScheduler().createTaskBuilder().execute(() -> {
                    if (task.cancel()) {
                        player.offer(Keys.IS_SNEAKING, false);
                        tasks.remove(player.getUniqueId());
                    }
                }).delayTicks(level * 20).submit(plugin);
            }
        }
    }

    @Listener
    public void onPlayerDamage(DamageEntityEvent event) {
        event.getContext().get(EventContextKeys.OWNER)
                .flatMap(User::getPlayer)
                .filter(player -> player.require(Keys.IS_SNEAKING))
                .ifPresent(player -> playerHasStasis(player, event));
    }

    @Listener
    public void onPlayerTakeDamage(DamageEntityEvent event) {
        if (event.getTargetEntity() instanceof Player) {
            playerHasStasis((Player)event.getTargetEntity(), event);
        }
    }

    @Listener
    public void onBlockEdit(ChangeBlockEvent event, @First Player player) {
        playerHasStasis(player, event);
    }

    @Listener
    public void onItemConsume(UseItemStackEvent.Start event, @Root Player player) {
        playerHasStasis(player, event);
    }

    private void playerHasStasis(Player player, Cancellable event) {
        player.getChestplate()
                .filter(item -> ModManager.itemHasModifier(item, this))
                .filter(item -> player.require(Keys.IS_SNEAKING))
                .filter(item -> tasks.containsKey(player.getUniqueId()))
                .ifPresent(item -> event.setCancelled(true));
    }
}
