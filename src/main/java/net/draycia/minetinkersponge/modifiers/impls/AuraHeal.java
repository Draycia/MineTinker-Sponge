package net.draycia.minetinkersponge.modifiers.impls;

import com.google.common.collect.ImmutableList;
import net.draycia.minetinkersponge.MineTinkerSponge;
import net.draycia.minetinkersponge.modifiers.Modifier;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.recipe.crafting.CraftingRecipe;
import org.spongepowered.api.item.recipe.crafting.Ingredient;
import org.spongepowered.api.item.recipe.crafting.ShapedCraftingRecipe;
import org.spongepowered.api.text.Text;

import java.util.List;
import java.util.Optional;

public class AuraHeal extends Modifier {

    private static List<ItemType> compatibleTypes = ImmutableList.<ItemType>builder()
            .addAll(MineTinkerSponge.getItemTypeUtils().LEGGINGS)
            .build();

    @Override
    public List<ItemType> getCompatibleItems() {
        return compatibleTypes;
    }

    @Override
    public Text getCompatibilityString() {
        return Text.of("Leggings.");
    }

    @Override
    public Text getDisplayName() {
        return getName(Text.of("Heal Aura"));
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
        return getModifierItemType(ItemTypes.PRISMARINE_CRYSTALS);
    }

    @Override
    public void onModifierRegister(Object plugin) {
        Sponge.getEventManager().registerListeners(plugin, this);
    }

    @Override
    public Optional<CraftingRecipe> getRecipe() {

        ShapedCraftingRecipe recipe = ShapedCraftingRecipe.builder()
                .aisle("ISI", "SES", "ISI")
                .where('I', Ingredient.of(ItemTypes.GOLD_BLOCK))
                .where('S', Ingredient.of(ItemTypes.LAPIS_BLOCK))
                .where('E', Ingredient.of(ItemTypes.APPLE))
                .result(getModifierItem())
                .id(getId())
                .build();

        return Optional.of(getCraftingRecipe(recipe));
    }

    @Override
    public String getDescription() {
        return getDescription("Heals nearby allies by 1 HP (half a heart) every 5 seconds per ally below 50% HP, up to 3 HP per 5 seconds.");
    }


}
