package net.draycia.minetinkersponge.managers;

import com.google.common.reflect.TypeToken;
import com.google.inject.Inject;
import net.draycia.minetinkersponge.MineTinkerSponge;
import net.draycia.minetinkersponge.modifiers.Modifier;
import net.draycia.minetinkersponge.utils.MTConfig;
import net.draycia.minetinkersponge.utils.MTTranslations;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import ninja.leaping.configurate.objectmapping.ObjectMapper;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;
import org.spongepowered.api.item.recipe.crafting.Ingredient;
import org.spongepowered.api.item.recipe.crafting.ShapedCraftingRecipe;
import org.spongepowered.api.item.recipe.crafting.ShapelessCraftingRecipe;
import org.spongepowered.api.text.serializer.TextSerializers;
import org.spongepowered.api.util.TypeTokens;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class ConfigManager {

    private Path configDir;
    private Path defaultConfig;
    private ConfigurationLoader<CommentedConfigurationNode> configLoader;
    private ConfigurationNode mainConfig;
    private Logger logger;
    private MineTinkerSponge mineTinkerSponge;

    @Inject
    public ConfigManager(@ConfigDir(sharedRoot = false) Path configDir, @DefaultConfig(sharedRoot = false) Path defaultConfig,
                         @DefaultConfig(sharedRoot = false) ConfigurationLoader<CommentedConfigurationNode> configLoader,
                         Logger logger, MineTinkerSponge mineTinkerSponge) {

        this.configDir = configDir;
        this.defaultConfig = defaultConfig;
        this.configLoader = configLoader;
        this.logger = logger;
        this.mineTinkerSponge = mineTinkerSponge;
    }

    public void reloadConfig() {
        try {
            // Main configuration
            mainConfig = configLoader.load();

            if (!defaultConfig.toFile().exists()) {
                saveDefaultConfigValues();
            } else {
                loadConfigValues();
            }

            File modifierDirectory = configDir.resolve("modifiers").toFile();

            if (!modifierDirectory.exists()) {
                modifierDirectory.mkdirs();
            }

            // Language configuration
            File languageFile = new File(configDir.toFile(), "language.conf");
            ConfigurationLoader<CommentedConfigurationNode> languageLoader = HoconConfigurationLoader.builder().setFile(languageFile).build();
            ConfigurationNode languageNode = languageLoader.load();

            try {
                ObjectMapper<MTTranslations>.BoundInstance languageMapper = ObjectMapper.forClass(MTTranslations.class).bindToNew();

                if (!languageFile.exists()) {
                    languageMapper.serialize(languageNode);
                    languageLoader.save(languageNode);
                } else {
                    languageMapper.populate(languageNode);
                }
            } catch (ObjectMappingException e) {
                e.printStackTrace();
            }

            // Modifier configurations
            for (Modifier modifier : ModManager.getAllModifiers().values()) {
                File modifierFile = new File(modifierDirectory, modifier.getKey() + ".conf");
                ConfigurationLoader<CommentedConfigurationNode> modifierLoader = HoconConfigurationLoader.builder().setFile(modifierFile).build();
                ConfigurationNode modifierNode = modifierLoader.load();

                if (!modifierFile.exists()) {
                    saveDefaultModifierValues(modifier, modifierNode, modifierLoader);
                }

                loadModifierConfigValues(modifier, modifierNode);

                if (modifierNode.getNode("enabled").getBoolean()) {
                    modifier.onModifierRegister(mineTinkerSponge);
                }
            }
        } catch (IOException exception) {
            logger.warn("Failed to load main configuration!");
            exception.printStackTrace();
        }
    }

    private void saveDefaultConfigValues() throws IOException {
        mainConfig.getNode("enchantmentConvertBlock").setValue(MTConfig.ENCHANTMENT_CONVERT_BLOCK.getId());

        try {
            ObjectMapper.forClass(MTConfig.class).bindToNew().serialize(mainConfig);
        } catch (ObjectMappingException e) {
            e.printStackTrace();
        }

        configLoader.save(mainConfig);
    }

    private void loadConfigValues() {
        String blockName = mainConfig.getNode("enchantmentConvertBlock").getString();
        Optional<BlockType> convertBlock = Sponge.getGame().getRegistry().getType(BlockType.class, blockName);

        if (convertBlock.isPresent()) {
            MTConfig.ENCHANTMENT_CONVERT_BLOCK = convertBlock.get();
        } else {
            logger.warn("No BlockType found matching input \"" + blockName + "\".");
            MTConfig.ENCHANTMENT_CONVERT_BLOCK = BlockTypes.BOOKSHELF;
        }

        try {
            ObjectMapper.forClass(MTConfig.class).bindToNew().populate(mainConfig);
        } catch (ObjectMappingException e) {
            e.printStackTrace();
        }
    }

    private void saveDefaultModifierValues(Modifier modifier, ConfigurationNode modifierNode,
                                           ConfigurationLoader modifierLoader) throws IOException {

        modifierNode.getNode("name").setValue(TextSerializers.FORMATTING_CODE.serialize(modifier.getName()));
        modifierNode.getNode("enabled").setValue(true);
        modifierNode.getNode("maxLevel").setValue(modifier.getMaxLevel());
        modifierNode.getNode("levelWeight").setValue(modifier.getLevelWeight());
        modifierNode.getNode("applicationChance").setValue(modifier.getApplicationChance());
        modifierNode.getNode("description").setValue(modifier.getDescription());
        modifierNode.getNode("modifierItem").setValue(modifier.getModifierItemType().getId());

        modifier.getRecipe().ifPresent(recipe -> {
            if (recipe instanceof ShapedCraftingRecipe) {
                ShapedCraftingRecipe shapedRecipe = (ShapedCraftingRecipe) recipe;

                List<String> itemIds = new LinkedList<>();

                for (int y = 0; y < shapedRecipe.getHeight(); y++) {
                    for (int x = 0; x < shapedRecipe.getWidth(); x++) {
                        itemIds.add(shapedRecipe.getIngredient(x, y).displayedItems().get(0).getType().getId());
                    }
                }

                modifierNode.getNode("shapedRecipeIngredients").setValue(itemIds);
                modifierNode.getNode("recipeIsShaped").setValue(true);
            } else if (recipe instanceof ShapelessCraftingRecipe) {
                ShapelessCraftingRecipe shapelessRecipe = (ShapelessCraftingRecipe) recipe;

                List<List<String>> ingredients = new ArrayList<>();

                for (Ingredient ingredient : shapelessRecipe.getIngredientPredicates()) {
                    ingredients.add(ingredient.displayedItems().stream()
                            .map(ItemStackSnapshot::getType)
                            .map(ItemType::getName)
                            .collect(Collectors.toList()));
                }

                modifierNode.getNode("shapelessRecipeIngredients").setValue(ingredients);
                modifierNode.getNode("recipeIsShaped").setValue(false);
            }
        });

        modifier.onConfigurationSave(modifierNode);

        modifierLoader.save(modifierNode);
    }

    private void loadModifierConfigValues(Modifier modifier, ConfigurationNode modifierNode) {
        modifier.setEnabled(modifierNode.getNode("enabled").getBoolean());
        modifier.setName(TextSerializers.FORMATTING_CODE.deserialize(modifierNode.getNode("name").getString()));
        modifier.setMaxLevel(modifierNode.getNode("maxLevel").getInt());
        modifier.setLevelWeight(modifierNode.getNode("levelWeight").getInt());
        modifier.setApplicationChance(modifierNode.getNode("applicationChance").getInt());
        modifier.setDescription(modifierNode.getNode("description").getString());

        String modifierItemId = modifierNode.getNode("modifierItem").getString();
        Optional<ItemType> modifierItem = Sponge.getGame().getRegistry().getType(ItemType.class, modifierItemId);

        if (modifierItem.isPresent()) {
            modifier.setModifierItemType(modifierItem.get());
        } else {
            logger.warn("No ItemType found matching input \"" + modifierItemId + "\".");
        }

        if (modifierNode.getNode("recipeIsShaped").getBoolean()) {
            try {
                List<String> ingredientIds = modifierNode.getNode("shapedRecipeIngredients").getList(TypeTokens.STRING_TOKEN);

                ShapedCraftingRecipe.Builder recipeBuilder = ShapedCraftingRecipe.builder();

                recipeBuilder = recipeBuilder.aisle("ABC", "DEF", "GHI");

                for (int i = 0; i < ingredientIds.size(); i++) {
                    Optional<ItemType> itemType = Sponge.getGame().getRegistry().getType(ItemType.class, ingredientIds.get(i));

                    if (!itemType.isPresent()) {
                        logger.warn("Invalid item id [" + ingredientIds.get(i) + "], skipping recipe! Please ensure the ID is spelled correctly.");
                        break;
                    }

                    recipeBuilder = ((ShapedCraftingRecipe.Builder.AisleStep) recipeBuilder).where((char) (i + 65), Ingredient.of(itemType.get()));
                }

                modifier.setRecipe(((ShapedCraftingRecipe.Builder.ResultStep) recipeBuilder).result(modifier.getModifierItem()).id(modifier.getKey()).build());

            } catch (ObjectMappingException e) {
                e.printStackTrace();
            }
        } else {
            try {
                List<List<String>> ingredientIds = modifierNode.getNode("shapelessRecipeIngredients").getList(new TypeToken<List<String>>() {});

                ShapelessCraftingRecipe.Builder builder = ShapelessCraftingRecipe.builder();
                ShapelessCraftingRecipe.Builder.ResultStep resultStep = null;

                for (List<String> ingredients : ingredientIds) {
                    List<ItemType> itemTypes = new ArrayList<>();

                    for (String ingredient : ingredients) {
                        Sponge.getGame().getRegistry().getType(ItemType.class, ingredient).ifPresent(itemTypes::add);
                    }

                    resultStep = builder.addIngredient(Ingredient.of(itemTypes.toArray(new ItemType[0])));
                }

                if (resultStep == null) {
                    logger.warn("Invalid recipe for modifier " + modifier.getName().toPlain());
                    return;
                }

                modifier.setRecipe(resultStep.result(modifier.getModifierItem()).id(modifier.getKey()).build());
            } catch (ObjectMappingException e) {
                e.printStackTrace();
            }
        }

        if (modifier.isEnabled()) {
            modifier.onConfigurationLoad(modifierNode);
        }
    }

}
