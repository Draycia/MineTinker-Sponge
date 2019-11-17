package net.draycia.minetinkersponge.managers;

import net.draycia.minetinkersponge.modifiers.ModManager;
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
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.recipe.crafting.CraftingRecipe;
import org.spongepowered.api.item.recipe.crafting.Ingredient;
import org.spongepowered.api.item.recipe.crafting.ShapedCraftingRecipe;
import org.spongepowered.api.util.TypeTokens;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class ConfigManager {

    private Path configDir;
    private Path defaultConfig;
    private ConfigurationLoader<CommentedConfigurationNode> configLoader;
    private ConfigurationNode mainConfig;
    private Logger logger;
    private ModManager modManager;

    public ConfigManager(Path configDir, Path defaultConfig, ConfigurationLoader<CommentedConfigurationNode> configLoader, Logger logger, ModManager modManager) {
        this.configDir = configDir;
        this.defaultConfig = defaultConfig;
        this.configLoader = configLoader;
        this.logger = logger;
        this.modManager = modManager;
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
            for (Modifier modifier : modManager.getAllModifiers().values()) {
                File modifierFile = new File(modifierDirectory, modifier.getKey() + ".conf");
                ConfigurationLoader<CommentedConfigurationNode> modifierLoader = HoconConfigurationLoader.builder().setFile(modifierFile).build();
                ConfigurationNode modifierNode = modifierLoader.load();

                if (!modifierFile.exists()) {
                    saveDefaultModifierValues(modifier, modifierNode, modifierLoader);
                } else {
                    loadModifierConfigValues(modifier, modifierNode);
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

        modifierNode.getNode("name").setValue(modifier.getName());
        modifierNode.getNode("maxLevel").setValue(modifier.getMaxLevel());
        modifierNode.getNode("levelWeight").setValue(modifier.getLevelWeight());
        modifierNode.getNode("applicationChance").setValue(modifier.getApplicationChance());
        modifierNode.getNode("description").setValue(modifier.getDescription());
        modifierNode.getNode("modifierItem").setValue(modifier.getModifierItemType().getId());
        // TODO: Recipes

        Optional<CraftingRecipe> optionalRecipe = modifier.getRecipe();

        if (optionalRecipe.isPresent()) {
            CraftingRecipe recipe = optionalRecipe.get();

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
            } else {
                // TODO: Implement shapeless recipe storage

                modifierNode.getNode("recipeIsShaped").setValue(false);
            }
        }

        modifier.onConfigurationSave(modifierNode);

        modifierLoader.save(modifierNode);
    }

    private void loadModifierConfigValues(Modifier modifier, ConfigurationNode modifierNode) {
        modifier.setName(modifierNode.getNode("name").getString());
        modifier.setMaxLevel(modifierNode.getNode("maxLevel").getInt());
        modifier.setLevelWeight(modifierNode.getNode("levelWeight").getInt());
        modifier.setApplicationChance(modifierNode.getNode("applicationChance").getInt());
        modifier.setDescription(modifierNode.getNode("description").getString());

        String modifierItemId = modifierNode.getNode("modifierItem").getString();
        Optional<ItemType> modifierItem = Sponge.getGame().getRegistry().getType(ItemType.class, modifierItemId);

        if (modifierItem.isPresent()) {
            modifier.setModifierItemType(modifierItem.get());
        } else {
            logger.warn("No BlockType found matching input \"" + modifierItemId + "\".");
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
            // Shapeless recipe
        }

        modifier.onConfigurationLoad(modifierNode);
    }

}
