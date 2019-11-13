package net.draycia.minetinkersponge;

import com.google.inject.Inject;
import net.draycia.minetinkersponge.commands.*;
import net.draycia.minetinkersponge.data.DataRegistrar;
import net.draycia.minetinkersponge.listeners.*;
import net.draycia.minetinkersponge.modifiers.ModManager;
import net.draycia.minetinkersponge.modifiers.Modifier;
import net.draycia.minetinkersponge.modifiers.impls.*;
import net.draycia.minetinkersponge.modifiers.impls.enchantments.*;
import net.draycia.minetinkersponge.modifiers.impls.potioneffects.InstantDamage;
import net.draycia.minetinkersponge.modifiers.impls.potioneffects.Poisonous;
import net.draycia.minetinkersponge.modifiers.impls.upgrades.DiamondUpgrade;
import net.draycia.minetinkersponge.modifiers.impls.upgrades.GoldUpgrade;
import net.draycia.minetinkersponge.modifiers.impls.upgrades.IronUpgrade;
import net.draycia.minetinkersponge.utils.*;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.event.game.GameReloadEvent;
import org.spongepowered.api.event.game.state.*;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.recipe.crafting.CraftingRecipe;
import org.spongepowered.api.item.recipe.crafting.Ingredient;
import org.spongepowered.api.item.recipe.crafting.ShapedCraftingRecipe;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.util.TypeTokens;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Plugin(
        id = "minetinker-sponge",
        name = "MineTinker-Sponge",
        description = "Adds an alternate enchantment system and new enchantments"
)
public class MineTinkerSponge {

    @Inject
    private PluginContainer container;

    @Inject
    @ConfigDir(sharedRoot = false)
    private Path configDir;

    @Inject
    @DefaultConfig(sharedRoot = false)
    private Path defaultConfig;

    @Inject
    @DefaultConfig(sharedRoot = false)
    private ConfigurationLoader<CommentedConfigurationNode> configLoader;

    @Inject
    private Logger logger;

    private ConfigurationNode mainConfig;

    private ModManager modManager;
    private ItemLevelManager itemLevelManager;
    private PlayerNameManager playerNameManager;
    private InventoryGUIManager inventoryGUIManager;

    public ModManager getModManager() {
        return modManager;
    }

    public ItemLevelManager getItemLevelManager() {
        return itemLevelManager;
    }

    public PluginContainer getContainer() {
        return container;
    }

    public InventoryGUIManager getInventoryGUIManager() {
        return inventoryGUIManager;
    }

    @Listener
    public void onPreInit(GamePreInitializationEvent event) {
        DataRegistrar.registerDataManipulators();

        modManager = new ModManager();

        registerModifiers();

        reloadConfig();

        itemLevelManager = new ItemLevelManager(modManager);
        playerNameManager = new PlayerNameManager(this);

        registerCommands();
        registerListeners();

        inventoryGUIManager = new InventoryGUIManager(this);
    }

    @Listener
    public void onGameStarted(GameStartingServerEvent event) {
        playerNameManager.onGameStarted();
        playerNameManager.startScheduler();
    }

    @Listener
    public void onGameStopped(GameStoppingServerEvent event) {
        playerNameManager.onGameStopped();
    }

    @Listener
    public void reload(GameReloadEvent event) {
        reloadConfig();
    }

    private void reloadConfig() {
        // TODO: Modifier method that's called when configs are messed with so extra options can be registered
        // Plugin will automatically handle config values that all modifiers share, but unique per-modifier ones
        // will be handled by the modifier itself. Maybe have a config serializer/deserializer instead?

        try {
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
        mainConfig.getNode("globalMaxLevel").setValue(MTConfig.GLOBAL_MAX_LEVEL);

        mainConfig.getNode("enchantmentConvertBlock").setValue(MTConfig.ENCHANTMENT_CONVERT_BLOCK.getId());

        mainConfig.getNode("convertTransfersEnchantments").setValue(MTConfig.CONVERT_TRANSFERS_ENCHANTMENTS);
        mainConfig.getNode("convertExceedsMaxLevel").setValue(MTConfig.CONVERT_EXCEEDS_MAX_LEVEL);
        mainConfig.getNode("hideEnchantments").setValue(MTConfig.HIDE_ENCHANTMENTS);
        mainConfig.getNode("makeItemsUnbreakable").setValue(MTConfig.MAKE_UNBREAKABLE);
        mainConfig.getNode("costsAreLinear").setValue(MTConfig.COSTS_ARE_LINEAR);
        mainConfig.getNode("convertMobDrops").setValue(MTConfig.CONVERT_MOB_DROPS);

        mainConfig.getNode("resultIncompatibleModifier").setValue(MTConfig.RESULT_INCOMPATIBLE_MODIFIER);
        mainConfig.getNode("resultIncompatibleTool").setValue(MTConfig.RESULT_INCOMPATIBLE_TOOL);
        mainConfig.getNode("resultNotEnoughSlots").setValue(MTConfig.RESULT_NOT_ENOUGH_SLOTS);
        mainConfig.getNode("resultRandomChance").setValue(MTConfig.RESULT_RANDOM_CHANCE);
        mainConfig.getNode("resultLevelCap").setValue(MTConfig.RESULT_LEVEL_CAP);

        configLoader.save(mainConfig);
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

                modifier.setRecipe(((ShapedCraftingRecipe.Builder.ResultStep) recipeBuilder).result(modifier.getModifierItem()).build());

            } catch (ObjectMappingException e) {
                e.printStackTrace();
            }
        } else {
            // Shapeless recipe
        }

        modifier.onConfigurationLoad(modifierNode);
    }

    private void loadConfigValues() {
        MTConfig.GLOBAL_MAX_LEVEL = mainConfig.getNode("globalMaxLevel").getInt();

        String blockName = mainConfig.getNode("enchantmentConvertBlock").getString();
        Optional<BlockType> convertBlock = Sponge.getGame().getRegistry().getType(BlockType.class, blockName);

        if (convertBlock.isPresent()) {
            MTConfig.ENCHANTMENT_CONVERT_BLOCK = convertBlock.get();
        } else {
            logger.warn("No BlockType found matching input \"" + blockName + "\".");
            MTConfig.ENCHANTMENT_CONVERT_BLOCK = BlockTypes.BOOKSHELF;
        }

        MTConfig.CONVERT_TRANSFERS_ENCHANTMENTS = mainConfig.getNode("convertTransfersEnchantments").getBoolean();
        MTConfig.CONVERT_EXCEEDS_MAX_LEVEL = mainConfig.getNode("convertExceedsMaxLevel").getBoolean();
        MTConfig.HIDE_ENCHANTMENTS = mainConfig.getNode("hideEnchantments").getBoolean();
        MTConfig.MAKE_UNBREAKABLE = mainConfig.getNode("makeItemsUnbreakable").getBoolean();
        MTConfig.COSTS_ARE_LINEAR = mainConfig.getNode("costsAreLinear").getBoolean();
        MTConfig.CONVERT_MOB_DROPS = mainConfig.getNode("convertMobDrops").getBoolean();

        MTConfig.RESULT_INCOMPATIBLE_MODIFIER = mainConfig.getNode("resultIncompatibleModifier").getString();
        MTConfig.RESULT_INCOMPATIBLE_TOOL = mainConfig.getNode("resultIncompatibleTool").getString();
        MTConfig.RESULT_NOT_ENOUGH_SLOTS = mainConfig.getNode("resultNotEnoughSlots").getString();
        MTConfig.RESULT_RANDOM_CHANCE = mainConfig.getNode("resultRandomChance").getString();
        MTConfig.RESULT_LEVEL_CAP = mainConfig.getNode("resultLevelCap").getString();
    }

    private void registerModifiers() {
        // Enchantment Modifiers
        modManager.registerModifier(this, new AquaAffinity());
        modManager.registerModifier(this, new BaneOfArthropods());
        modManager.registerModifier(this, new BindingCurse());
        modManager.registerModifier(this, new BlastProtection());
        modManager.registerModifier(this, new DepthStrider());
        modManager.registerModifier(this, new Efficiency());
        modManager.registerModifier(this, new FeatherFalling());
        modManager.registerModifier(this, new FireAspect());
        modManager.registerModifier(this, new FireProtection());
        modManager.registerModifier(this, new Flame());
        modManager.registerModifier(this, new Fortune());
        modManager.registerModifier(this, new FrostWalker());
        modManager.registerModifier(this, new Infinity());
        modManager.registerModifier(this, new Knockback());
        modManager.registerModifier(this, new Looting());
        modManager.registerModifier(this, new LuckOfTheSea());
        modManager.registerModifier(this, new Lure());
        modManager.registerModifier(this, new Mending());
        modManager.registerModifier(this, new Power());
        modManager.registerModifier(this, new ProjectileProtection());
        modManager.registerModifier(this, new Protection());
        modManager.registerModifier(this, new Punch());
        modManager.registerModifier(this, new Respiration());
        modManager.registerModifier(this, new Sharpness());
        modManager.registerModifier(this, new SilkTouch());
        modManager.registerModifier(this, new Smite());
        modManager.registerModifier(this, new Sweeping());
        modManager.registerModifier(this, new Thorns());
        modManager.registerModifier(this, new Unbreaking());
        modManager.registerModifier(this, new VanishingCurse());

        // Custom Modifiers
        modManager.registerModifier(this, new Directing(modManager));
        modManager.registerModifier(this, new Hammer(modManager));
        modManager.registerModifier(this, new Kinetic(modManager));

        // Potion Modifiers
        modManager.registerModifier(this, new Poisonous(modManager));
        modManager.registerModifier(this, new InstantDamage(modManager));

        // Upgrade Modifiers
        modManager.registerModifier(this, new IronUpgrade(modManager));
        modManager.registerModifier(this, new GoldUpgrade(modManager));
        modManager.registerModifier(this, new DiamondUpgrade(modManager));
    }

    private void registerCommands() {
        CommandSpec addModifier = CommandSpec.builder()
                .description(Text.of("Applies the modifier to the held item (or increments its level)."))
                .permission("minetinker.commands.addmodifier")
                .arguments(GenericArguments.string(Text.of("modifier")),
                        GenericArguments.optional(GenericArguments.integer(Text.of("amount"))))
                .executor(new AddModifierCommand(modManager))
                .build();

        Sponge.getCommandManager().register(this, addModifier, "addmod", "addmodifier");

        CommandSpec convertItem = CommandSpec.builder()
                .description(Text.of("Converts the held item."))
                .permission("minetinker.commands.convertitem")
                .executor(new ConvertItemCommand(modManager))
                .build();

        Sponge.getCommandManager().register(this, convertItem, "convertitem");

        CommandSpec giveModifierItem = CommandSpec.builder()
                .description(Text.of("Gives a modifier item for the specified modifier."))
                .permission("minetinker.commands.givemodifieritem")
                .arguments(GenericArguments.string(Text.of("modifier")))
                .executor(new GiveModifierItemCommand(modManager))
                .build();

        Sponge.getCommandManager().register(this, giveModifierItem, "givemod", "givemodifier");

        CommandSpec addLevel = CommandSpec.builder()
                .description(Text.of("Increases the level of the item."))
                .permission("minetinker.commands.addlevel")
                .executor(new AddLevelCommand(modManager))
                .build();

        Sponge.getCommandManager().register(this, addLevel, "addlevel");

        CommandSpec addSlots = CommandSpec.builder()
                .description(Text.of("Increases the modifier slots of the item.."))
                .permission("minetinker.commands.givemodifieritem")
                .executor(new AddSlotsCommand(modManager))
                .build();

        Sponge.getCommandManager().register(this, addSlots, "addslots");

        CommandSpec modifiers = CommandSpec.builder()
                .description(Text.of("Shows the modifier GUI"))
                .permission("minetinker.commands.modifiers")
                .executor(new ModifiersCommand(this))
                .build();

        Sponge.getCommandManager().register(this, modifiers, "modifiers", "mods");
    }

    private void registerListeners() {
        Sponge.getEventManager().registerListeners(this, new BlockBreakListener(modManager));
        Sponge.getEventManager().registerListeners(this, new InventoryListener(modManager));
        Sponge.getEventManager().registerListeners(this, new InteractListener(modManager));

        if (MTConfig.CONVERT_MOB_DROPS) {
            Sponge.getEventManager().registerListeners(this, new ItemDropListener(modManager));
        }

        Sponge.getEventManager().registerListeners(this, new FishingListener(modManager));
        Sponge.getEventManager().registerListeners(this, new DamageListener(modManager));
        Sponge.getEventManager().registerListeners(this, new AnvilListener(modManager));
        Sponge.getEventManager().registerListeners(this, modManager);
    }

}
