package net.draycia.minetinkersponge;

import com.google.inject.Inject;
import net.draycia.minetinkersponge.data.DataRegistrar;
import net.draycia.minetinkersponge.listeners.*;
import net.draycia.minetinkersponge.managers.*;
import net.draycia.minetinkersponge.managers.ModManager;
import net.draycia.minetinkersponge.modifiers.impls.*;
import net.draycia.minetinkersponge.modifiers.impls.enchantments.*;
import net.draycia.minetinkersponge.modifiers.impls.potioneffects.InstantDamage;
import net.draycia.minetinkersponge.modifiers.impls.potioneffects.Poisonous;
import net.draycia.minetinkersponge.modifiers.impls.upgrades.DiamondUpgrade;
import net.draycia.minetinkersponge.modifiers.impls.upgrades.GoldUpgrade;
import net.draycia.minetinkersponge.modifiers.impls.upgrades.IronUpgrade;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.event.game.GameReloadEvent;
import org.spongepowered.api.event.game.state.*;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.plugin.Dependency;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;

import java.nio.file.Path;

@Plugin(
        id = "minetinker-sponge",
        name = "MineTinker-Sponge",
        description = "Adds an alternate enchantment system and new enchantments",
        dependencies = @Dependency(id = "TeslaLibs", optional = true)
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

    private static ModManager modManager = null;

    private ItemLevelManager itemLevelManager;
    private PlayerNameManager playerNameManager;
    private InventoryGUIManager inventoryGUIManager;
    private CommandManager commandManager;
    private ConfigManager configManager;

    public static ModManager getModManager() {
        return modManager;
    }

    public ItemLevelManager getItemLevelManager() {
        return itemLevelManager;
    }

    public InventoryGUIManager getInventoryGUIManager() {
        return inventoryGUIManager;
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public PluginContainer getContainer() {
        return container;
    }

    @Listener
    public void onPreInit(GamePreInitializationEvent event) {
        DataRegistrar.registerDataManipulators();

        modManager = new ModManager();

        registerModifiers();

        configManager = new ConfigManager(configDir, defaultConfig, configLoader, logger, modManager);
        configManager.reloadConfig();

        itemLevelManager = new ItemLevelManager(modManager);
        playerNameManager = new PlayerNameManager(this);
        commandManager = new CommandManager(this);

        commandManager.registerCommands();

        registerListeners();

        if (Sponge.getPluginManager().isLoaded("TeslaLibs")) {
            inventoryGUIManager = new InventoryGUIManager(this);
        }
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
        configManager.reloadConfig();
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

    private void registerListeners() {
        Sponge.getEventManager().registerListeners(this, new BlockBreakListener(modManager));
        Sponge.getEventManager().registerListeners(this, new InventoryListener(modManager));
        Sponge.getEventManager().registerListeners(this, new InteractListener(modManager));
        Sponge.getEventManager().registerListeners(this, new ItemDropListener(modManager));
        Sponge.getEventManager().registerListeners(this, new FishingListener(modManager));
        Sponge.getEventManager().registerListeners(this, new DamageListener(modManager));
        Sponge.getEventManager().registerListeners(this, new AnvilListener(modManager));
        Sponge.getEventManager().registerListeners(this, modManager);
    }

}
