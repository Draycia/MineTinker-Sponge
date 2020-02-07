package net.draycia.minetinkersponge.modifiers.impls;

import com.google.common.collect.ImmutableList;
import net.draycia.minetinkersponge.managers.ModManager;
import net.draycia.minetinkersponge.modifiers.Modifier;
import net.draycia.minetinkersponge.utils.ItemTypeUtils;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.Item;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.cause.EventContext;
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
import org.spongepowered.api.item.recipe.smelting.SmeltingRecipeRegistry;
import org.spongepowered.api.item.recipe.smelting.SmeltingResult;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.*;

public class AutoSmelt extends Modifier {

    private SmeltingRecipeRegistry smeltingRegistry = Sponge.getGame().getRegistry().getSmeltingRecipeRegistry();

    private List<ItemType> compatibleTypes = ImmutableList.<ItemType>builder()
                .addAll(ItemTypeUtils.PICKAXES)
                .addAll(ItemTypeUtils.AXES)
                .addAll(ItemTypeUtils.SHOVELS)
                .build();

    @Override
    public List<ItemType> getCompatibleItems() {
        return compatibleTypes;
    }

    @Override
    public Text getCompatibilityString() {
        return Text.of("Pickaxes, Axes, and Shovels.");
    }

    @Override
    public Text getName() {
        return getName(Text.of("Auto-Smelt"));
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

    @Listener
    public void onItemDrop(DropItemEvent.Destruct event, @First Player player) {
        EventContext context = event.getContext();

        // Check if contexts contains blocks being broken or entities being killed
        if (context.containsKey(EventContextKeys.BLOCK_HIT)) {
            // Get the item in the player's main hand
            context.get(EventContextKeys.USED_ITEM)
                .filter(itemStack -> ModManager.itemHasModifier(itemStack, this))
                .ifPresent(itemStack -> {
                    List<Item> itemsToAdd = new LinkedList<>();

                    Entity entity;
                    Iterator<Entity> entityIterator = event.getEntities().iterator();

                    // Loop through all entities dropped
                    while (entityIterator.hasNext()) {
                        entity = entityIterator.next();

                        if (entity instanceof Item) {
                            Item item = (Item) entity;

                            // And put each in the player's inventory
                            if (item.item().exists()) {
                                ItemStack itemToGive = item.item().get().createStack();

                                Optional<ItemStackSnapshot> resultingItem = getSmeltingResult(itemToGive);

                                resultingItem.ifPresent(smeltingResult -> {
                                    ItemStack newItemStack = smeltingResult.createStack();

                                    newItemStack.setQuantity(itemToGive.getQuantity());

                                    Location<World> location = item.getLocation();
                                    Entity itemEntity = location.createEntity(EntityTypes.ITEM);

                                    itemEntity.offer(Keys.REPRESENTED_ITEM, newItemStack.createSnapshot());

                                    itemsToAdd.add((Item)itemEntity);

                                    entityIterator.remove();
                                });
                            }
                        }
                    }

                    event.getEntities().addAll(itemsToAdd);
                });
        }
    }

    private Optional<ItemStackSnapshot> getSmeltingResult(ItemStack itemStack) {
        return smeltingRegistry.getResult(itemStack.createSnapshot()).map(SmeltingResult::getResult);
    }
}
