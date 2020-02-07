package net.draycia.minetinkersponge.modifiers.impls;

import com.google.common.collect.ImmutableList;
import net.draycia.minetinkersponge.managers.ModManager;
import net.draycia.minetinkersponge.modifiers.Modifier;
import ninja.leaping.configurate.ConfigurationNode;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.cause.entity.damage.DamageType;
import org.spongepowered.api.event.cause.entity.damage.DamageTypes;
import org.spongepowered.api.event.cause.entity.damage.source.DamageSource;
import org.spongepowered.api.event.entity.DamageEntityEvent;
import org.spongepowered.api.event.filter.cause.Root;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.recipe.crafting.CraftingRecipe;
import org.spongepowered.api.item.recipe.crafting.Ingredient;
import org.spongepowered.api.item.recipe.crafting.ShapedCraftingRecipe;
import org.spongepowered.api.text.Text;

import java.util.List;
import java.util.Optional;

public class Kinetic extends Modifier {

    private float reductionPercentPerLevel = 100.0f;
    private static List<ItemType> compatibleTypes = ImmutableList.<ItemType>builder()
            .add(ItemTypes.ELYTRA)
            .build();

    @Override
    public List<ItemType> getCompatibleItems() {
        return compatibleTypes;
    }

    @Override
    public Text getCompatibilityString() {
        return Text.of("Elytras.");
    }

    @Override
    public Text getName() {
        return getName(Text.of("Kinetic"));
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
        return getModifierItemType(ItemTypes.ANVIL);
    }

    @Override
    public void onModifierRegister(Object plugin) {
        Sponge.getEventManager().registerListeners(plugin, this);
    }

    @Override
    public Optional<CraftingRecipe> getRecipe() {

        ShapedCraftingRecipe recipe = ShapedCraftingRecipe.builder()
                .aisle("ISI", "SES", "ISI")
                .where('I', Ingredient.of(ItemTypes.IRON_INGOT))
                .where('S', Ingredient.of(ItemTypes.STRING))
                .where('E', Ingredient.of(ItemTypes.ELYTRA))
                .result(getModifierItem())
                .id(getKey())
                .build();

        return Optional.of(getCraftingRecipe(recipe));
    }

    @Override
    public String getDescription() {
        return getDescription("Reduces damage taken from elytra collisions by %s each level.")
                .replace("%s", "" + getReductionPercentPerLevel());
    }

    @Override
    public void onConfigurationSave(ConfigurationNode modifierNode) {
        modifierNode.getNode("reductionPercentPerLevel").setValue(getReductionPercentPerLevel());
    }

    @Override
    public void onConfigurationLoad(ConfigurationNode modifierNode) {
        this.reductionPercentPerLevel = modifierNode.getNode("reductionPercentPerLevel").getInt();
    }

    @Listener
    public void onPlayerDamage(DamageEntityEvent event, @Root DamageSource source) {
        if (!(event.getTargetEntity() instanceof Player)) {
            return;
        }

        Player player = (Player)event.getTargetEntity();
        DamageType type = source.getType();

        if (type != DamageTypes.CONTACT) {
            return;
        }

        if (player.getChestplate().isPresent()) {
            int modifierLevel = ModManager.getModifierLevel(player.getChestplate().get(), this);

            if (modifierLevel > 0) {
                float reductionPercent = modifierLevel * getReductionPercentPerLevel();

                if (reductionPercent >= 100) {
                    event.setCancelled(true);
                } else if (!(reductionPercent <= 0)) {
                    // Don't do anything if the percentage is 0 or less
                    event.setBaseDamage((event.getBaseDamage() / 100) * reductionPercent);
                }
            }
        }
    }

    public float getReductionPercentPerLevel() {
        return reductionPercentPerLevel;
    }
}
