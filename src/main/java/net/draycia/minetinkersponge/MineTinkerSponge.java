package net.draycia.minetinkersponge;

import com.google.inject.Inject;
import net.draycia.minetinkersponge.commands.*;
import net.draycia.minetinkersponge.data.DataRegistrar;
import net.draycia.minetinkersponge.listeners.*;
import net.draycia.minetinkersponge.modifiers.ModManager;
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
import ninja.leaping.configurate.loader.ConfigurationLoader;
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
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.text.Text;

import java.io.IOException;
import java.nio.file.Path;
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
    @DefaultConfig(sharedRoot = false)
    private Path defaultConfig;

    @Inject
    @DefaultConfig(sharedRoot = false)
    private ConfigurationLoader<CommentedConfigurationNode> configLoader;

    @Inject
    @ConfigDir(sharedRoot = false)
    private Path configDir;

    @Inject
    private Logger logger;

    private ConfigurationNode config;

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
        reloadConfig();

        DataRegistrar.registerDataManipulators();

        modManager = new ModManager();
        itemLevelManager = new ItemLevelManager(modManager);
        playerNameManager = new PlayerNameManager(this);

        registerModifiers();
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
        // TODO: Per modifier configurations

        // TODO: Modifier method that's called when configs are messed with so extra options can be registered
        // Plugin will automatically handle config values that all modifiers share, but unique per-modifier ones
        // will be handled by the modifier itself. Maybe have a config serializer/deserializer instead?

        try {
            config = configLoader.load();

            if (!defaultConfig.toFile().exists()) {
                saveDefaultConfigValues();
            } else {
                loadConfigValues();
            }
        } catch (IOException exception) {
            logger.warn("Failed to load main configuration!");
            exception.printStackTrace();
        }
    }

    private void saveDefaultConfigValues() throws IOException {
        config.getNode("globalMaxLevel").setValue(MTConfig.GLOBAL_MAX_LEVEL);

        config.getNode("enchantmentConvertBlock").setValue(MTConfig.ENCHANTMENT_CONVERT_BLOCK.getId());

        config.getNode("convertTransfersEnchantments").setValue(MTConfig.CONVERT_TRANSFERS_ENCHANTMENTS);
        config.getNode("convertExceedsMaxLevel").setValue(MTConfig.CONVERT_EXCEEDS_MAX_LEVEL);
        config.getNode("hideEnchantments").setValue(MTConfig.HIDE_ENCHANTMENTS);
        config.getNode("makeItemsUnbreakable").setValue(MTConfig.MAKE_UNBREAKABLE);
        config.getNode("costsAreLinear").setValue(MTConfig.COSTS_ARE_LINEAR);
        config.getNode("convertMobDrops").setValue(MTConfig.CONVERT_MOB_DROPS);

        config.getNode("resultIncompatibleModifier").setValue(MTConfig.RESULT_INCOMPATIBLE_MODIFIER);
        config.getNode("resultIncompatibleTool").setValue(MTConfig.RESULT_INCOMPATIBLE_TOOL);
        config.getNode("resultNotEnoughSlots").setValue(MTConfig.RESULT_NOT_ENOUGH_SLOTS);
        config.getNode("resultRandomChance").setValue(MTConfig.RESULT_RANDOM_CHANCE);
        config.getNode("resultLevelCap").setValue(MTConfig.RESULT_LEVEL_CAP);

        configLoader.save(config);
    }

    private void loadConfigValues() {
        MTConfig.GLOBAL_MAX_LEVEL = config.getNode("globalMaxLevel").getInt();

        String blockName = config.getNode("enchantmentConvertBlock").getString();
        Optional<BlockType> convertBlock = Sponge.getGame().getRegistry().getType(BlockType.class, blockName);

        if (convertBlock.isPresent()) {
            MTConfig.ENCHANTMENT_CONVERT_BLOCK = convertBlock.get();
        } else {
            logger.warn("No BlockType found matching input \"" + blockName + "\".");
            MTConfig.ENCHANTMENT_CONVERT_BLOCK = BlockTypes.BOOKSHELF;
        }

        MTConfig.CONVERT_TRANSFERS_ENCHANTMENTS = config.getNode("convertTransfersEnchantments").getBoolean();
        MTConfig.CONVERT_EXCEEDS_MAX_LEVEL = config.getNode("convertExceedsMaxLevel").getBoolean();
        MTConfig.HIDE_ENCHANTMENTS = config.getNode("hideEnchantments").getBoolean();
        MTConfig.MAKE_UNBREAKABLE = config.getNode("makeItemsUnbreakable").getBoolean();
        MTConfig.COSTS_ARE_LINEAR = config.getNode("costsAreLinear").getBoolean();
        MTConfig.CONVERT_MOB_DROPS = config.getNode("convertMobDrops").getBoolean();

        MTConfig.RESULT_INCOMPATIBLE_MODIFIER = config.getNode("resultIncompatibleModifier").getString();
        MTConfig.RESULT_INCOMPATIBLE_TOOL = config.getNode("resultIncompatibleTool").getString();
        MTConfig.RESULT_NOT_ENOUGH_SLOTS = config.getNode("resultNotEnoughSlots").getString();
        MTConfig.RESULT_RANDOM_CHANCE = config.getNode("resultRandomChance").getString();
        MTConfig.RESULT_LEVEL_CAP = config.getNode("resultLevelCap").getString();
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
