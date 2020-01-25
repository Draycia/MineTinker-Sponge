package net.draycia.minetinkersponge.modifiers.impls;

import com.google.common.collect.ImmutableList;
import net.draycia.minetinkersponge.managers.ModManager;
import net.draycia.minetinkersponge.modifiers.Modifier;
import net.draycia.minetinkersponge.utils.ItemTypeUtils;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.type.HandTypes;
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
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.entity.MainPlayerInventory;
import org.spongepowered.api.item.inventory.query.QueryOperationTypes;
import org.spongepowered.api.item.recipe.crafting.CraftingRecipe;
import org.spongepowered.api.item.recipe.crafting.Ingredient;
import org.spongepowered.api.item.recipe.crafting.ShapedCraftingRecipe;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class Directing extends Modifier {

    private static List<ItemType> compatibleTypes;

    static {
        // TODO: Fishing Rod Support
        // TODO: Investigate Bow Support
        compatibleTypes = ImmutableList.<ItemType>builder()
                .addAll(ItemTypeUtils.PICKAXES)
                .addAll(ItemTypeUtils.AXES)
                .addAll(ItemTypeUtils.SHOVELS)
                .addAll(ItemTypeUtils.HOES)
                .addAll(ItemTypeUtils.FISHING_RODS)
                .addAll(ItemTypeUtils.SWORDS)
                .addAll(ItemTypeUtils.BOWS)
                .build();
    }

    @Override
    public List<ItemType> getCompatibleItems() {
        return compatibleTypes;
    }

    @Override
    public String getName() {
        return getName("Directing");
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
        return getModifierItemType(ItemTypes.COMPASS);
    }

    @Override
    public void onModifierRegister(Object plugin) {
        Sponge.getEventManager().registerListeners(plugin, this);
    }

    @Override
    public Optional<CraftingRecipe> getRecipe() {

        ShapedCraftingRecipe recipe = ShapedCraftingRecipe.builder()
                .aisle("ECE", "CIC", "ECE")
                .where('E', Ingredient.of(ItemTypes.ENDER_PEARL))
                .where('C', Ingredient.of(ItemTypes.COMPASS))
                .where('I', Ingredient.of(ItemTypes.IRON_BLOCK))
                .result(getModifierItem())
                .id(getKey())
                .build();

        return Optional.of(getCraftingRecipe(recipe));
    }

    @Override
    public String getDescription() {
        return getDescription("Drops from breaking blocks and killing mobs will instantly be placed in your inventory.");
    }

    private ImmutableList<EventContextKey> whitelistedContexts = ImmutableList.<EventContextKey>builder()
            .add(EventContextKeys.BLOCK_HIT)
            .add(EventContextKeys.SPAWN_TYPE)
            .build();

    @Listener
    public void onItemDrop(DropItemEvent.Destruct event) {
        EventContext context = event.getContext();

        if (context.containsKey(EventContextKeys.USED_ITEM) || context.containsKey(EventContextKeys.USED_HAND)) {
            return;
        }

        context.get(EventContextKeys.OWNER).ifPresent(owner -> {
            Optional<ItemStack> mainHand = owner.getItemInHand(HandTypes.MAIN_HAND);
            Optional<ItemStack> offHand = owner.getItemInHand(HandTypes.OFF_HAND);

            ItemStack itemStack = null;

            if (mainHand.isPresent() && ModManager.itemHasModifier(mainHand.get(), this)) {
                itemStack = mainHand.get();
            } else if (offHand.isPresent() && ModManager.itemHasModifier(offHand.get(), this)){
                itemStack = offHand.get();
            }

            Optional<Player> optionalPlayer = owner.getPlayer();

            if (itemStack != null && optionalPlayer.isPresent()) {
                transferDrops(optionalPlayer.get(), event.getEntities());

                // Cancel the drops
                event.setCancelled(true);
            }
        });
    }

    @Listener
    public void onItemDrop(DropItemEvent.Destruct event, @First Player player) {
        EventContext context = event.getContext();

        // Check if contexts contains blocks being broken or entities being killed
        if (!Collections.disjoint(context.keySet(), whitelistedContexts)) {
            // Get the item in the player's main hand
            context.get(EventContextKeys.USED_ITEM)
                    .filter(itemStack -> ModManager.itemHasModifier(itemStack, this))
                    .ifPresent(itemStack -> {

                transferDrops(player, event.getEntities());

                // Cancel the drops
                event.setCancelled(true);
            });
        }
    }

    private void transferDrops(Player player, List<Entity> entities) {
        // Get the player's grid inventory
        Inventory inventory = player.getInventory()
                .query(QueryOperationTypes.INVENTORY_TYPE.of(MainPlayerInventory.class));

        // Loop through all entities dropped
        for (Entity entity : entities) {
            if (entity instanceof Item) {
                Item item = (Item) entity;

                // And put each in the player's inventory
                if (item.item().exists()) {
                    ItemStack itemToGive = item.item().get().createStack();

                    if (inventory.canFit(itemToGive)) {
                        inventory.offer(itemToGive);
                    } else {
                        // If the player can't fit the item in their inventory, drop it next to them
                        Location<World> location = player.getLocation();

                        Entity itemEntity = location.createEntity(EntityTypes.ITEM);
                        itemEntity.offer(Keys.ACTIVE_ITEM, item.item().get());

                        location.spawnEntity(itemEntity);
                    }
                }
            }
        }
    }
}
