package net.draycia.minetinkersponge.modifiers.impls;

import net.draycia.minetinkersponge.managers.ModManager;
import net.draycia.minetinkersponge.modifiers.Modifier;
import net.draycia.minetinkersponge.utils.CompositeUnmodifiableList;
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
import org.spongepowered.api.event.item.inventory.DropItemEvent;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;
import org.spongepowered.api.item.inventory.entity.MainPlayerInventory;
import org.spongepowered.api.item.inventory.query.QueryOperationTypes;
import org.spongepowered.api.item.recipe.crafting.CraftingRecipe;
import org.spongepowered.api.item.recipe.crafting.Ingredient;
import org.spongepowered.api.item.recipe.crafting.ShapedCraftingRecipe;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.List;
import java.util.Optional;

public class Directing extends Modifier {

    private ModManager modManager;

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
    public List<ItemType> getCompatibleItems() {
        return new CompositeUnmodifiableList<>(ItemTypeUtils.getToolTypes(), ItemTypeUtils.getWeaponTypes());
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

    @Override
    public String getDescription() {
        return "Drops from breaking blocks and killing mobs will instantly be placed in your inventory.";
    }

    public Directing(ModManager modManager) {
        this.modManager = modManager;
    }

    @Listener
    public void onItemDrop(DropItemEvent.Destruct event) {
        EventContext context = event.getContext();

        // Check if contexts contains blocks being broken or entities being killed
        if (context.containsKey(EventContextKeys.BLOCK_HIT) || context.containsKey(EventContextKeys.SPAWN_TYPE)) {
            Optional<Player> player = event.getCause().first(Player.class);

            // Check if the player is the cause
            if (player.isPresent()) {
                // Get the item in the player's main hand
                Optional<ItemStackSnapshot> itemStack = context.get(EventContextKeys.USED_ITEM);

                if (itemStack.isPresent()) {
                    // Check if the item used has this modifier (directing)
                    if (modManager.itemHasModifier(itemStack.get(), this)) {
                        // Get the player's grid inventory
                        Inventory inventory = player.get().getInventory()
                                .query(QueryOperationTypes.INVENTORY_TYPE.of(MainPlayerInventory.class));

                        // Loop through all entities dropped
                        for (Entity entity : event.getEntities()) {
                            if (entity instanceof Item) {
                                Item item = (Item) entity;

                                // And put each in the player's inventory
                                if (item.item().exists()) {
                                    ItemStack itemToGive = item.item().get().createStack();

                                    if (inventory.canFit(itemToGive)) {
                                        inventory.offer(itemToGive);
                                    } else {
                                        // If the player can't fit the item in their inventory, drop it next to them
                                        Location<World> location = player.get().getLocation();

                                        Entity itemEntity = location.createEntity(EntityTypes.ITEM);
                                        itemEntity.offer(Keys.ACTIVE_ITEM, item.item().get());

                                        location.spawnEntity(itemEntity);
                                    }
                                }
                            }
                        }

                        // Cancel the drops
                        event.setCancelled(true);
                    }
                }
            }
        }
    }
}
