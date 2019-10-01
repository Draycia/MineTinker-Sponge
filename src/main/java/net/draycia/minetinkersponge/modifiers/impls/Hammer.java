package net.draycia.minetinkersponge.modifiers.impls;

import com.google.common.collect.Sets;
import net.draycia.minetinkersponge.modifiers.ModManager;
import net.draycia.minetinkersponge.modifiers.Modifier;
import net.draycia.minetinkersponge.utils.CompositeUnmodifiableList;
import net.draycia.minetinkersponge.utils.ItemTypeUtils;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.block.trait.BlockTrait;
import org.spongepowered.api.data.Transaction;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.ChangeBlockEvent;
import org.spongepowered.api.event.cause.EventContextKey;
import org.spongepowered.api.event.cause.EventContextKeys;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.filter.cause.Root;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;
import org.spongepowered.api.item.recipe.crafting.CraftingRecipe;
import org.spongepowered.api.item.recipe.crafting.Ingredient;
import org.spongepowered.api.item.recipe.crafting.ShapedCraftingRecipe;
import org.spongepowered.api.util.Direction;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class Hammer extends Modifier {

    private ModManager modManager;

    @Override
    public String getName() {
        return "Hammer";
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    public int getLevelWeight() {
        return 1;
    }

    @Override
    public ItemType getModifierItemType() {
        return ItemTypes.EMERALD;
    }

    @Override
    public List<ItemType> getCompatibleItems() {
        return new CompositeUnmodifiableList<>(ItemTypeUtils.getToolTypes(), ItemTypeUtils.getHoeTypes());
    }

    @Override
    public void onModifierRegister(Object plugin) {
        Sponge.getEventManager().registerListeners(plugin, this);
    }

    @Override
    public Optional<CraftingRecipe> getRecipe() {

        ShapedCraftingRecipe recipe = ShapedCraftingRecipe.builder()
                .aisle("ECE", "CIC", "ECE")
                .where('E', Ingredient.of(ItemTypes.EMERALD))
                .where('C', Ingredient.of(ItemTypes.NETHER_STAR))
                .where('I', Ingredient.of(ItemTypes.DIAMOND))
                .result(getModifierItem())
                .id(getKey())
                .build();

        return Optional.of(recipe);
    }

    public Hammer(ModManager modManager) {
        this.modManager = modManager;
    }

    @Listener
    public void onBlockBreak(ChangeBlockEvent.Break event, @Root Player player) {
        if (!event.getContext().containsKey(EventContextKeys.USED_ITEM)) {
            return;
        }

        Optional<ItemStackSnapshot> itemStackSnapshot = event.getContext().get(EventContextKeys.USED_ITEM);

        if (!itemStackSnapshot.isPresent()) {
            return;
        }

        ItemStackSnapshot itemStack = itemStackSnapshot.get();

        if (!modManager.itemHasModifier(itemStack, this)) {
            return;
        }

        // TODO: Find a way to properly break the 3, 9, and 25 blocks
    }
}