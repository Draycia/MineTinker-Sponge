package net.draycia.minetinkersponge.modifiers.impls;

import com.google.common.collect.ImmutableList;
import net.draycia.minetinkersponge.MineTinkerSponge;
import net.draycia.minetinkersponge.managers.ModManager;
import net.draycia.minetinkersponge.modifiers.Modifier;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.Item;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.recipe.crafting.CraftingRecipe;
import org.spongepowered.api.item.recipe.crafting.Ingredient;
import org.spongepowered.api.item.recipe.crafting.ShapedCraftingRecipe;
import org.spongepowered.api.text.Text;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Magnetic extends Modifier {

    private static List<ItemType> compatibleTypes = ImmutableList.<ItemType>builder()
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
        return getName(Text.of("Magnetic"));
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
        return getModifierItemType(ItemTypes.IRON_BARS);
    }

    @Override
    public void onModifierRegister(Object plugin) {
        // TODO: Make intervalTicks configurable
        Sponge.getScheduler().createTaskBuilder().execute(this::magneticEffect).intervalTicks(5).submit(plugin);
    }

    private void magneticEffect() {
        for (Player player : Sponge.getServer().getOnlinePlayers()) {
            player.getBoots().ifPresent(boots -> {
                int level = ModManager.getModifierLevel(boots, this);

                if (level == 0) {
                    return;
                }

                // TODO: Make multiplier (3) configurable
                List<Item> items = player.getNearbyEntities(level * 3).stream()
                        .filter(entity -> entity instanceof Item)
                        .map(entity -> (Item)entity)
                        .collect(Collectors.toList());

                MineTinkerSponge.transferItemEntities(player, items);
            });
        }
    }

    @Override
    public Optional<CraftingRecipe> getRecipe() {

        ShapedCraftingRecipe recipe = ShapedCraftingRecipe.builder()
                .aisle("ISI", "SES", "ISI")
                .where('I', Ingredient.of(ItemTypes.GOLDEN_APPLE))
                .where('S', Ingredient.of(ItemTypes.STRING))
                .where('E', Ingredient.of(ItemTypes.SUGAR))
                .result(getModifierItem())
                .id(getId())
                .build();

        return Optional.of(getCraftingRecipe(recipe));
    }

    @Override
    public String getDescription() {
        return getDescription("Attracts items towards you.");
    }
}
