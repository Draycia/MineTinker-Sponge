package net.draycia.minetinkersponge.modifiers.impls;

import com.google.common.collect.ImmutableList;
import net.draycia.minetinkersponge.managers.ModManager;
import net.draycia.minetinkersponge.modifiers.Modifier;
import net.draycia.minetinkersponge.utils.ItemTypeUtils;
import ninja.leaping.configurate.ConfigurationNode;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.Living;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.cause.EventContextKey;
import org.spongepowered.api.event.cause.EventContextKeys;
import org.spongepowered.api.event.cause.entity.damage.source.DamageSource;
import org.spongepowered.api.event.entity.DamageEntityEvent;
import org.spongepowered.api.event.filter.cause.Root;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.recipe.crafting.CraftingRecipe;
import org.spongepowered.api.item.recipe.crafting.Ingredient;
import org.spongepowered.api.item.recipe.crafting.ShapedCraftingRecipe;

import java.util.List;
import java.util.Optional;

public class Lifesteal extends Modifier {

    private double percentPerLevel = 0.10f;

    private static List<ItemType> compatibleTypes = ImmutableList.<ItemType>builder()
            .addAll(ItemTypeUtils.SWORDS)
            .addAll(ItemTypeUtils.AXES)
            .build();

    @Override
    public List<ItemType> getCompatibleItems() {
        return compatibleTypes;
    }

    @Override
    public String getName() {
        return getName("Lifesteal");
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
        return getModifierItemType(ItemTypes.GOLDEN_APPLE);
    }

    @Override
    public void onModifierRegister(Object plugin) {
        Sponge.getEventManager().registerListeners(plugin, this);
    }

    @Override
    public Optional<CraftingRecipe> getRecipe() {

        ShapedCraftingRecipe recipe = ShapedCraftingRecipe.builder()
                .aisle("ISI", "SES", "ISI")
                .where('I', Ingredient.of(ItemTypes.GOLDEN_APPLE))
                .where('S', Ingredient.of(ItemTypes.STRING))
                .where('E', Ingredient.of(ItemTypes.SUGAR))
                .result(getModifierItem())
                .id(getKey())
                .build();

        return Optional.of(getCraftingRecipe(recipe));
    }

    @Override
    public String getDescription() {
        return getDescription("Heals you for %s% of the damage you deal to mobs, per level.")
                .replace("%s", Double.toString(getPercentPerLevel()));
    }

    @Override
    public void onConfigurationSave(ConfigurationNode modifierNode) {
        modifierNode.getNode("percentPerLevel").setValue(getPercentPerLevel());
    }

    @Override
    public void onConfigurationLoad(ConfigurationNode modifierNode) {
        this.percentPerLevel = modifierNode.getNode("percentPerLevel").getDouble();
    }

    @Listener
    public void onEntityDamage(DamageEntityEvent event, @Root DamageSource source) {
        if (!(event.getTargetEntity() instanceof Living)) {
            return;
        }

        event.getContext().get(EventContextKeys.USED_HAND).ifPresent(usedHand -> {
            event.getContext().get(EventContextKeys.OWNER).ifPresent(user -> {
                user.getItemInHand(usedHand).ifPresent(itemUsed -> {
                    int level = ModManager.getModifierLevel(itemUsed, this);

                    if (level <= 0) {
                        return;
                    }

                    double percent = getPercentPerLevel() * level;
                    double amount = percent * event.getFinalDamage();

                    double health = Math.min(user.get(Keys.HEALTH).orElse(20D) + amount, user.get(Keys.MAX_HEALTH).orElse(20D));

                    user.offer(Keys.HEALTH, health);
                });
            });
        });
    }

    public double getPercentPerLevel() {
        return percentPerLevel;
    }
}
