package net.draycia.minetinkersponge.modifiers.impls.potioneffects;

import com.google.common.collect.ImmutableList;
import net.draycia.minetinkersponge.managers.ModManager;
import net.draycia.minetinkersponge.modifiers.Modifier;
import net.draycia.minetinkersponge.utils.ItemTypeUtils;
import net.minecraft.entity.projectile.EntityTippedArrow;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.manipulator.mutable.PotionEffectData;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.effect.potion.PotionEffect;
import org.spongepowered.api.effect.potion.PotionEffectTypes;
import org.spongepowered.api.entity.living.Living;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.entity.projectile.arrow.Arrow;
import org.spongepowered.api.entity.projectile.arrow.TippedArrow;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.cause.EventContextKeys;
import org.spongepowered.api.event.entity.DamageEntityEvent;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.recipe.crafting.CraftingRecipe;
import org.spongepowered.api.item.recipe.crafting.Ingredient;
import org.spongepowered.api.item.recipe.crafting.ShapedCraftingRecipe;

import java.util.List;
import java.util.Optional;

public class Poisonous extends Modifier {

    private static List<ItemType> compatibleTypes;

    static {
        compatibleTypes = ImmutableList.<ItemType>builder()
                .addAll(ItemTypeUtils.SWORDS)
                .addAll(ItemTypeUtils.AXES)
                .addAll(ItemTypeUtils.BOWS)
                .build();
    }

    @Override
    public List<ItemType> getCompatibleItems() {
        return compatibleTypes;
    }

    @Override
    public String getName() {
        return getName("Poisonous");
    }

    @Override
    public int getMaxLevel() {
        return getMaxLevel(5);
    }

    @Override
    public int getLevelWeight() {
        return getLevelWeight(1);
    }

    @Override
    public ItemType getModifierItemType() {
        return getModifierItemType(ItemTypes.POISONOUS_POTATO);
    }

    @Override
    public Optional<CraftingRecipe> getRecipe() {
        ShapedCraftingRecipe recipe = ShapedCraftingRecipe.builder()
                .aisle("SGS", "GDG", "SGS")
                .where('S', Ingredient.of(ItemTypes.POISONOUS_POTATO))
                .where('G', Ingredient.of(ItemTypes.ROTTEN_FLESH))
                .where('D', Ingredient.of(ItemTypes.DIAMOND))
                .result(getModifierItem())
                .id(getKey())
                .build();

        return Optional.of(getCraftingRecipe(recipe));
    }

    @Override
    public String getDescription() {
        return getDescription("Applies the Poisonous potion effect to mobs that are hit. Duration = level, amplifier = 1.");
    }

    @Override
    public void onModifierRegister(Object plugin) {
        Sponge.getEventManager().registerListeners(plugin, this);
    }

    @Listener
    public void onEntityDamagedByEntity(DamageEntityEvent event) {
        if (!(event.getTargetEntity() instanceof Living)) {
            return;
        }

        // I fear no man... But that THING... It scares me.
        Player player = event.getCause().first(Player.class).orElseGet(() -> event.getContext().get(EventContextKeys.OWNER)
                .flatMap(User::getPlayer).orElse(null));

        if (player != null) {
            event.getCause().first(TippedArrow.class).ifPresent(arrow -> {
                player.getItemInHand(HandTypes.MAIN_HAND).ifPresent(itemStack -> {
                    if (ModManager.itemHasModifier(itemStack, this)) {
                        int level = ModManager.getModifierLevel(itemStack, this);

                        // TODO: check if arrow is a custom "poison enchanted arrow" and then apply scaling effect

                        //PotionEffectData potionEffects = event.getTargetEntity().getOrCreate(PotionEffectData.class).get();
                        //potionEffects.addElement(PotionEffect.builder().potionType(PotionEffectTypes.POISON).duration(level * 20).amplifier(1).build());

                        //event.getTargetEntity().offer(potionEffects);
                    }
                });
            });
        }
    }

}
