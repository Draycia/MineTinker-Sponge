package net.draycia.minetinkersponge.modifiers.impls;

import net.draycia.minetinkersponge.modifiers.ModManager;
import net.draycia.minetinkersponge.modifiers.Modifier;
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

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class Kinetic extends Modifier {

    private ModManager modManager;

    @Override
    public String getName() {
        return getName("Kinetic");
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
    public List<ItemType> getCompatibleItems() {
        return Collections.singletonList(ItemTypes.ELYTRA);
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
        return getDescription("Drops from breaking blocks and killing mobs will instantly be placed in your inventory.");
    }

    public Kinetic(ModManager modManager) {
        this.modManager = modManager;
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
            if (modManager.itemHasModifier(player.getChestplate().get(), this)) {
                event.setCancelled(true);
            }
        }
    }
}
