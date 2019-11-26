package net.draycia.minetinkersponge.modifiers.impls;

import com.flowpowered.math.vector.Vector3i;
import net.draycia.minetinkersponge.managers.ModManager;
import net.draycia.minetinkersponge.modifiers.Modifier;
import net.draycia.minetinkersponge.utils.CompositeUnmodifiableList;
import net.draycia.minetinkersponge.utils.ItemTypeUtils;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.data.Transaction;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.ChangeBlockEvent;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.EventContextKeys;
import org.spongepowered.api.event.filter.cause.Root;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;
import org.spongepowered.api.item.recipe.crafting.CraftingRecipe;
import org.spongepowered.api.item.recipe.crafting.Ingredient;
import org.spongepowered.api.item.recipe.crafting.ShapedCraftingRecipe;
import org.spongepowered.api.util.TypeTokens;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Hammer extends Modifier {

    private ModManager modManager;
    private ArrayList<BlockType> blacklistedBlocks = new ArrayList<>();

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

    @Override
    public void onConfigurationSave(ConfigurationNode modifierNode) {
        List<String> entries = new ArrayList<>();

        for (BlockType block : blacklistedBlocks) {
            entries.add(block.getId());
        }

        modifierNode.getNode("blacklisted_blocks").setValue(entries);
    }

    @Override
    public void onConfigurationLoad(ConfigurationNode modifierNode) {
        // TODO: Check if Sponge already knows how to save/load lists of BlockTypes from config
        blacklistedBlocks.clear();

        try {
            List<String> entries = modifierNode.getNode("blacklisted_blocks").getList(TypeTokens.STRING_TOKEN);

            for (String entry : entries) {
                Optional<BlockType> block = Sponge.getGame().getRegistry().getType(BlockType.class, entry);

                if (block.isPresent()) {
                    blacklistedBlocks.add(block.get());
                } else {
                    // TODO: use logger
                    System.out.println("Invalid block detected in Hammer config! [" + entry + "]");
                }
            }
        } catch (ObjectMappingException e) {
            e.printStackTrace();
        }
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

        // TODO: Unsafe
        Location<World> location = event.getTransactions().get(0).getOriginal().getLocation().get();

        if (!modManager.itemHasModifier(itemStack, this)) {
            return;
        }

        int level = modManager.getModifierLevel(itemStack, this);

        // TODO: Variable width and height
        // TODO: Account for which block face the player broke

        for (int x = -level - 1; x < level; x++) {
            for (int y = -level - 1; y < level; y++) {
                BlockPos blockPos = new BlockPos(location.getBlockX() + x + 1, location.getBlockY() + y + 1, location.getBlockZ());

                // TODO: Call block break events and filter them out in this modifier

                EntityPlayerMP playerMP = (EntityPlayerMP)player;
                playerMP.interactionManager.tryHarvestBlock(blockPos);
            }
        }
    }
}
