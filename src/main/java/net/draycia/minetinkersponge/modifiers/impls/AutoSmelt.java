package net.draycia.minetinkersponge.modifiers.impls;

import com.google.common.collect.ImmutableList;
import net.draycia.minetinkersponge.managers.ModManager;
import net.draycia.minetinkersponge.modifiers.Modifier;
import net.draycia.minetinkersponge.utils.ItemTypeUtils;
import net.minecraft.item.crafting.FurnaceRecipes;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.Item;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.cause.EventContext;
import org.spongepowered.api.event.cause.EventContextKey;
import org.spongepowered.api.event.cause.EventContextKeys;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.item.inventory.DropItemEvent;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;
import org.spongepowered.api.item.recipe.crafting.CraftingRecipe;
import org.spongepowered.api.item.recipe.crafting.Ingredient;
import org.spongepowered.api.item.recipe.crafting.ShapedCraftingRecipe;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.*;

public class AutoSmelt extends Modifier {

    private ModManager modManager;
    private static List<ItemType> compatibleTypes;

    static {
        // TODO: Bow Support
        // TODO: Fishing Rod Support
        compatibleTypes = ImmutableList.<ItemType>builder()
                .addAll(ItemTypeUtils.PICKAXES)
                .addAll(ItemTypeUtils.AXES)
                .addAll(ItemTypeUtils.SHOVELS)
                .build();
    }

    @Override
    public List<ItemType> getCompatibleItems() {
        return compatibleTypes;
    }

    @Override
    public String getName() {
        return getName("Auto-Smelt");
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
        return getModifierItemType(ItemTypes.FURNACE);
    }

    @Override
    public void onModifierRegister(Object plugin) {
        Sponge.getEventManager().registerListeners(plugin, this);
    }

    @Override
    public Optional<CraftingRecipe> getRecipe() {

        ShapedCraftingRecipe recipe = ShapedCraftingRecipe.builder()
                .aisle("ECE", "CIC", "ECE")
                .where('E', Ingredient.of(ItemTypes.MAGMA_CREAM))
                .where('C', Ingredient.of(ItemTypes.COMPASS))
                .where('I', Ingredient.of(ItemTypes.FURNACE))
                .result(getModifierItem())
                .id(getKey())
                .build();

        return Optional.of(getCraftingRecipe(recipe));
    }

    @Override
    public String getDescription() {
        return getDescription("Drops from breaking blocks will instantly be smelted.");
    }

    public AutoSmelt(ModManager modManager) {
        this.modManager = modManager;
    }

    private ImmutableList<EventContextKey> whitelistedContexts = ImmutableList.<EventContextKey>builder()
            .add(EventContextKeys.BLOCK_HIT)
            .build();

    @Listener
    public void onItemDrop(DropItemEvent.Destruct event, @First Player player) {
        EventContext context = event.getContext();

        // Check if contexts contains blocks being broken or entities being killed
        if (!Collections.disjoint(context.keySet(), whitelistedContexts)) {
            // Get the item in the player's main hand
            Optional<ItemStackSnapshot> itemStack = context.get(EventContextKeys.USED_ITEM);

            if (itemStack.isPresent()) {
                // Check if the item used has this modifier (directing)
                if (modManager.itemHasModifier(itemStack.get(), this)) {
                    List<Item> itemsToRemove = new LinkedList<>();
                    List<Item> itemsToAdd = new LinkedList<>();

                    // Loop through all entities dropped
                    for (Entity entity : event.getEntities()) {
                        if (entity instanceof Item) {
                            Item item = (Item) entity;

                            // And put each in the player's inventory
                            if (item.item().exists()) {
                                ItemStack itemToGive = item.item().get().createStack();

                                net.minecraft.item.Item nmsItem = ((net.minecraft.item.ItemStack)(Object)itemToGive).getItem();
                                net.minecraft.item.ItemStack nmsStack = new net.minecraft.item.ItemStack(nmsItem, 1, 32767);
                                ItemStack newItemStack = (ItemStack)(Object)FurnaceRecipes.instance().getSmeltingResult(nmsStack);

                                if (!newItemStack.isEmpty()) {
                                    newItemStack.setQuantity(itemToGive.getQuantity());

                                    Location<World> location = item.getLocation();
                                    Entity itemEntity = location.createEntity(EntityTypes.ITEM);

                                    itemEntity.offer(Keys.REPRESENTED_ITEM, newItemStack.createSnapshot());

                                    itemsToAdd.add((Item)itemEntity);
                                    itemsToRemove.add(item);
                                }
                            }
                        }
                    }

                    event.getEntities().removeAll(itemsToRemove);
                    event.getEntities().addAll(itemsToAdd);
                }
            }
        }
    }
}
