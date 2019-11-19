package net.draycia.minetinkersponge.modifiers.impls;

import net.draycia.minetinkersponge.managers.ModManager;
import net.draycia.minetinkersponge.modifiers.Modifier;
import net.draycia.minetinkersponge.utils.CompositeUnmodifiableList;
import net.draycia.minetinkersponge.utils.ItemTypeUtils;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.ChangeBlockEvent;
import org.spongepowered.api.event.cause.EventContextKeys;
import org.spongepowered.api.event.filter.cause.Root;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;
import org.spongepowered.api.item.recipe.crafting.CraftingRecipe;
import org.spongepowered.api.item.recipe.crafting.Ingredient;
import org.spongepowered.api.item.recipe.crafting.ShapedCraftingRecipe;

import java.util.List;
import java.util.Optional;

public class Hammer extends Modifier {

    private ModManager modManager;

    @Override
    public String getName() {
        return getName("Hammer");
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
        return getModifierItemType(ItemTypes.EMERALD);
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

        return Optional.of(getCraftingRecipe(recipe));
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
        // TODO: User configurable BlockType blacklist
    }
}
