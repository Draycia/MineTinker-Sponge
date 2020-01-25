package net.draycia.minetinkersponge.managers;

import com.google.inject.Inject;
import com.mcsimonflash.sponge.teslalibs.inventory.Action;
import com.mcsimonflash.sponge.teslalibs.inventory.Element;
import com.mcsimonflash.sponge.teslalibs.inventory.Layout;
import com.mcsimonflash.sponge.teslalibs.inventory.View;
import net.draycia.minetinkersponge.data.MTKeys;
import net.draycia.minetinkersponge.modifiers.Modifier;
import net.draycia.minetinkersponge.utils.MTTranslations;
import net.draycia.minetinkersponge.utils.StringUtils;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.type.DyeColors;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;
import org.spongepowered.api.item.inventory.property.InventoryDimension;
import org.spongepowered.api.item.inventory.property.InventoryTitle;
import org.spongepowered.api.item.recipe.crafting.CraftingRecipe;
import org.spongepowered.api.item.recipe.crafting.Ingredient;
import org.spongepowered.api.item.recipe.crafting.ShapedCraftingRecipe;
import org.spongepowered.api.item.recipe.crafting.ShapelessCraftingRecipe;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.TranslatableText;
import org.spongepowered.api.text.format.TextColors;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class InventoryGUIManager {

    private View view;

    @Inject
    public InventoryGUIManager(PluginContainer container) {
        Layout.Builder layout = Layout.builder();

        int index = 0;

        // Loop through modifiers
        for (Map.Entry<String, Modifier> entry : ModManager.getAllModifiers().entrySet()) {
            Modifier modifier = entry.getValue();

            // Create the item to display in the GUI
            ItemStack itemStack = ItemStack.of(modifier.getModifierItemType());

            // Prepare the lore to give to the item
            List<Text> lore = new ArrayList<>();

            // Set the description
            if (!modifier.getDescription().isEmpty()) {
                lore.add(Text.of(MTTranslations.BLANK_LINE));

                lore.addAll(StringUtils.splitString(modifier.getDescription(), 30));
            }

            // Show the modifier's max level
            lore.add(Text.of(MTTranslations.BLANK_LINE));
            lore.add(Text.of(TextColors.GOLD, MTTranslations.MAX_LEVEL, TextColors.WHITE, modifier.getMaxLevel()));

            // Show the modifier slot costs
            StringBuilder slotCosts = new StringBuilder(Integer.toString(modifier.getModifierSlotCost(1)));

            if (modifier.getMaxLevel() > 1) {
                ArrayList<Integer> slots = new ArrayList<>();

                for (int i = 2; i <= modifier.getMaxLevel(); i++) {
                    slots.add(modifier.getModifierSlotCost(i));
                }

                if (slots.stream().distinct().count() == 1) {
                    slotCosts.append(MTTranslations.AT_ALL_LEVELS);
                } else {
                    for (Integer slotCost : slots) {
                        slotCosts.append(MTTranslations.SLOT_COST_SEPARATOR).append(slotCost);
                    }
                }
            }

            lore.add(Text.of(MTTranslations.BLANK_LINE));
            lore.add(Text.of(TextColors.GRAY, MTTranslations.REQUIRED_SLOTS, TextColors.WHITE, slotCosts.toString()));

            // Show the modifier's compatible items
            lore.add(Text.of(MTTranslations.BLANK_LINE));
            // TODO: Override compatibility strings in each modifier
            lore.add(Text.of(TextColors.BLUE, MTTranslations.APPLICABLE_ON, TextColors.WHITE, modifier.getCompatibilityString()));

            // If the modifier applies an enchantment, say so
            if (modifier.getAppliedEnchantment() != null) {
                lore.add(Text.of(MTTranslations.BLANK_LINE));
                lore.add(Text.of(TextColors.YELLOW, MTTranslations.APPLIED_ENCHANTMENTS, TextColors.WHITE,
                        TranslatableText.of(modifier.getAppliedEnchantment().getTranslation())));
            }

            // Set the lore and display name of the item
            itemStack.offer(Keys.ITEM_LORE, lore);
            itemStack.offer(Keys.DISPLAY_NAME, Text.of(TextColors.GREEN, modifier.getName()));
            itemStack.offer(MTKeys.MODIFIER_ID, modifier.getKey());

            // If the modifier has a recipe, create a sub-GUI for it
            if (modifier.getRecipe().isPresent()) {
                Layout.Builder recipeLayout = Layout.builder()
                        .dimension(InventoryDimension.of(9, 5));

                CraftingRecipe recipe = modifier.getRecipe().get();

                if (recipe instanceof ShapedCraftingRecipe) {
                    ShapedCraftingRecipe shapedRecipe = (ShapedCraftingRecipe)recipe;

                    for (int x = 0; x < 3; x++) {
                        for (int y = 0; y < 3; y++) {
                            int ingredientIndex = ((9 * (y + 1)) + 1) + x;

                            ItemStackSnapshot ingredient = shapedRecipe.getIngredient(x, y).displayedItems().get(0);
                            recipeLayout.set(Element.of(ingredient), ingredientIndex);
                        }
                    }
                } else if (recipe instanceof ShapelessCraftingRecipe) {
                    ShapelessCraftingRecipe shapelessRecipe = (ShapelessCraftingRecipe)recipe;

                    ArrayList<ItemStackSnapshot> items = new ArrayList<>();

                    for (Ingredient ingredient : shapelessRecipe.getIngredientPredicates()) {
                        items.addAll(ingredient.displayedItems());
                    }

                    int shapelessIndex = 0;

                    iterations: {
                        for (int x = 0; x < 3; x++) {
                            for (int y = 0; y < 3; y++) {
                                int ingredientIndex = ((9 * (y + 1)) + 1) + x;

                                if (items.size() < shapelessIndex + 1) {
                                    break iterations;
                                }

                                ItemStackSnapshot ingredient = items.get(shapelessIndex++);
                                recipeLayout.set(Element.of(ingredient), ingredientIndex);
                            }
                        }
                    }
                }

                recipeLayout.set(Element.of(recipe.getExemplaryResult()), 24);

                ItemStack greenPane = ItemStack.builder().itemType(ItemTypes.STAINED_GLASS_PANE).build();
                greenPane.offer(Keys.DYE_COLOR , DyeColors.LIME);
                greenPane.offer(Keys.DISPLAY_NAME, Text.of(MTTranslations.RETURN_TO_MODIFIERS));

                Consumer<Action.Click> goToMain = click -> showViewToPlayer(click.getPlayer());

                recipeLayout.border(Element.of(greenPane, goToMain));

                View recipeView = View.builder()
                        .property(InventoryTitle.of(Text.of(MTTranslations.MODIFIER, modifier.getName())))
                        .build(container);

                recipeView.define(recipeLayout.build());

                Consumer<Action.Click> goToRecipe = click -> recipeView.open(click.getPlayer());

                // Set the layout for later
                layout = layout.set(Element.of(itemStack, goToRecipe), index);
            } else {
                layout = layout.set(Element.of(itemStack), index);
            }

            index += 1;
        }

        view = View.builder()
                .property(InventoryTitle.of(Text.of(MTTranslations.MINETINKER_MODIFIERS)))
                .build(container);

        view.define(layout.build());
    }

    public void showViewToPlayer(Player player) {
        view.open(player);
    }

}