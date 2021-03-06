package net.draycia.minetinkersponge.modifiers.impls;

import com.flowpowered.math.vector.Vector3d;
import com.google.common.collect.ImmutableList;
import net.draycia.minetinkersponge.MineTinkerSponge;
import net.draycia.minetinkersponge.managers.ModManager;
import net.draycia.minetinkersponge.modifiers.Modifier;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.Living;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
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

public class Grounding extends Modifier {

    private List<ItemType> compatibleTypes = ImmutableList.<ItemType>builder()
            .addAll(MineTinkerSponge.getItemTypeUtils().BOOTS)
            .build();

    @Override
    public List<ItemType> getCompatibleItems() {
        return compatibleTypes;
    }

    @Override
    public Text getCompatibilityString() {
        return Text.of("Boots.");
    }

    @Override
    public Text getDisplayName() {
        return getName(Text.of("Grounding"));
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
        return getModifierItemType(ItemTypes.NETHER_BRICK);
    }

    @Override
    public void onModifierRegister(Object plugin) {
        Sponge.getEventManager().registerListeners(plugin, this);
    }

    @Override
    public Optional<CraftingRecipe> getRecipe() {

        ShapedCraftingRecipe recipe = ShapedCraftingRecipe.builder()
                .aisle("IFI", "FCF", "IFI")
                .where('F', Ingredient.of(ItemTypes.FEATHER))
                .where('C', Ingredient.of(ItemTypes.REDSTONE))
                .where('I', Ingredient.of(ItemTypes.IRON_BLOCK))
                .result(getModifierItem())
                .id(getId())
                .build();

        return Optional.of(getCraftingRecipe(recipe));
    }

    @Override
    public String getDescription() {
        return getDescription("Damages nearby entities and knocks them back when you take fall damage.");
    }

    @Listener
    public void onPlayerDamage(DamageEntityEvent event, @Root DamageSource source) {
        // TODO: Scale damage off of level
        // TODO: Option to change damage modifier
        // TODO: Option to transfer damage taken instead, config % per level

        if (!(event.getTargetEntity() instanceof Player) || source.getType() != DamageTypes.FALL) {
            return;
        }

        Player player = (Player)event.getTargetEntity();
        Vector3d playerPosition = player.getPosition();

        // Ensure the player has boots with the modifier
        if (!player.getBoots().isPresent() || !ModManager.itemHasModifier(player.getBoots().get(), this)) {
            return;
        }

        for (Entity entity : player.getNearbyEntities(10)) {
            if (entity == player || !(entity instanceof Living)) {
                continue;
            }

            // Calculate the position and distance between the player and the entity
            double distance = MineTinkerSponge.knockbackLiving(playerPosition, (Living)entity, 1);

            // Damage the entity
            double damage = ((10 - distance) / 4);
            entity.damage(damage, DamageSource.builder().type(DamageTypes.ATTACK).bypassesArmor().build());
        }
    }

}
