package net.draycia.minetinkersponge.modifiers.impls;

import com.google.common.collect.ImmutableList;
import net.draycia.minetinkersponge.MineTinkerSponge;
import net.draycia.minetinkersponge.managers.ModManager;
import net.draycia.minetinkersponge.modifiers.Modifier;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.Transaction;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.data.value.BaseValue;
import org.spongepowered.api.data.value.mutable.Value;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.Item;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.projectile.source.ProjectileSource;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.action.FishingEvent;
import org.spongepowered.api.event.cause.EventContext;
import org.spongepowered.api.event.cause.EventContextKey;
import org.spongepowered.api.event.cause.EventContextKeys;
import org.spongepowered.api.event.filter.cause.First;
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
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Directing extends Modifier {

    private static List<ItemType> compatibleTypes;

    static {
        compatibleTypes = ImmutableList.<ItemType>builder()
                .addAll(MineTinkerSponge.getItemTypeUtils().PICKAXES)
                .addAll(MineTinkerSponge.getItemTypeUtils().AXES)
                .addAll(MineTinkerSponge.getItemTypeUtils().SHOVELS)
                .addAll(MineTinkerSponge.getItemTypeUtils().HOES)
                .addAll(MineTinkerSponge.getItemTypeUtils().FISHING_RODS)
                .addAll(MineTinkerSponge.getItemTypeUtils().SWORDS)
                .addAll(MineTinkerSponge.getItemTypeUtils().BOWS)
                .build();
    }

    @Override
    public List<ItemType> getCompatibleItems() {
        return compatibleTypes;
    }

    @Override
    public Text getCompatibilityString() {
        return Text.of("Pickaxes, Axes, Shovels, Hoes, Fishing Rods, Swords, and Bows.");
    }

    @Override
    public Text getDisplayName() {
        return getName(Text.of("Directing"));
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
                .id(getId())
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

    // Bows
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
                transferEntityDrops(optionalPlayer.get(), event.getEntities());

                // Cancel the drops
                event.setCancelled(true);
            }
        });
    }

    // Melee weapons
    @Listener
    public void onItemDrop(DropItemEvent.Destruct event, @First Player player) {
        EventContext context = event.getContext();

        // Check if contexts contains blocks being broken or entities being killed
        if (!Collections.disjoint(context.keySet(), whitelistedContexts)) {
            // Get the item in the player's main hand
            context.get(EventContextKeys.USED_ITEM)
                    .filter(itemStack -> ModManager.itemHasModifier(itemStack, this))
                    .ifPresent(itemStack -> {

                transferEntityDrops(player, event.getEntities());

                // Cancel the drops
                event.setCancelled(true);
            });
        }
    }

    // Fishing poles
    @Listener
    public void onFishingStop(FishingEvent.Stop event) {
        ProjectileSource source = event.getFishHook().getShooter();

        if (!(source instanceof Player)) {
            return;
        }

        Player player = (Player) source;

        List<ItemStackSnapshot> items = event.getTransactions().stream()
                .map(Transaction::getFinal)
                .collect(Collectors.toList());

        if (items.isEmpty()) {
            return;
        }

        event.getTransactions().forEach(transaction -> transaction.setValid(false));

        MineTinkerSponge.transferItemStacks(player, items);
    }

    private void transferEntityDrops(Player player, List<Entity> entities) {
        List<ItemStackSnapshot> items = entities.stream()
                .filter(entity -> entity instanceof Item)
                .map(entity -> ((Item)entity).item())
                .filter(Value::exists)
                .map(BaseValue::get)
                .collect(Collectors.toList());

        MineTinkerSponge.transferItemStacks(player, items);
    }
}
