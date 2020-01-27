package net.draycia.minetinkersponge.modifiers.impls.potioneffects;

import com.google.common.collect.ImmutableList;
import net.draycia.minetinkersponge.managers.ModManager;
import net.draycia.minetinkersponge.modifiers.Modifier;
import net.draycia.minetinkersponge.utils.ItemTypeUtils;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.manipulator.mutable.PotionEffectData;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.effect.potion.PotionEffect;
import org.spongepowered.api.effect.potion.PotionEffectTypes;
import org.spongepowered.api.entity.living.Living;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.cause.EventContextKey;
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

public class InstantDamage extends Modifier {

    private static List<ItemType> compatibleTypes;

    static {
        // TODO: Bow Support
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
        return getName("Instant Damage");
    }

    @Override
    public int getMaxLevel() {
        return getMaxLevel(1);
    }

    @Override
    public int getLevelWeight() {
        return getLevelWeight(1);
    }

    @Override
    public ItemType getModifierItemType() {
        return getModifierItemType(ItemTypes.COAL);
    }

    @Override
    public Optional<CraftingRecipe> getRecipe() {
        ShapedCraftingRecipe recipe = ShapedCraftingRecipe.builder()
                .aisle("SGS", "GDG", "SGS")
                .where('S', Ingredient.of(ItemTypes.POISONOUS_POTATO))
                .where('G', Ingredient.of(ItemTypes.DEADBUSH))
                .where('D', Ingredient.of(ItemTypes.DIAMOND))
                .result(getModifierItem())
                .id(getKey())
                .build();

        return Optional.of(getCraftingRecipe(recipe));
    }

    @Override
    public String getDescription() {
        return getDescription("Applies the Instant Damage potion effect to mobs that are hit. Duration = 1, amplifier = 1.");
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
            Optional<ItemStack> itemStack = player.getItemInHand(HandTypes.MAIN_HAND);

            if (itemStack.isPresent() && ModManager.itemHasModifier(itemStack.get(), this)) {
                PotionEffectData potionEffects = event.getTargetEntity().getOrCreate(PotionEffectData.class).get();
                potionEffects.addElement(PotionEffect.builder().potionType(PotionEffectTypes.INSTANT_DAMAGE).duration(1).amplifier(1).build());

                event.getTargetEntity().offer(potionEffects);
            }
        }
    }
}
