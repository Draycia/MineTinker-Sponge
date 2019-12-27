package net.draycia.minetinkersponge;

import com.google.inject.Inject;
import net.draycia.minetinkersponge.data.DataRegistrar;
import net.draycia.minetinkersponge.listeners.*;
import net.draycia.minetinkersponge.managers.*;
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
        dependencies = @Dependency(id = "teslalibs", optional = true)
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

    private ConfigManager configManager;

    private static PluginContainer pluginContainer = null;
    private static MineTinkerSponge mineTinkerSponge = null;

    public static PluginContainer getContainer() {
        return pluginContainer;
    }

    public static MineTinkerSponge getInstance() {
        return mineTinkerSponge;
    }

    @Listener
    public void onPreInit(GamePreInitializationEvent event) {
        DataRegistrar.registerDataManipulators();

        mineTinkerSponge = this;
        pluginContainer = container;

        registerModifiers();

        configManager = new ConfigManager(configDir, defaultConfig, configLoader, logger, ModManager.getInstance());
        configManager.reloadConfig();

        CommandManager.registerCommands();

        registerListeners();

        if (Sponge.getPluginManager().isLoaded("teslalibs")) {
            InventoryGUIManager.getInstance();
        }
    }

    @Listener
    public void reload(GameReloadEvent event) {
        configManager.reloadConfig();
    }

    private void registerModifiers() {
        ModManager modManager = ModManager.getInstance();

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
        modManager.registerModifier(this, new AutoSmelt(modManager));
        modManager.registerModifier(this, new Directing(modManager));
        modManager.registerModifier(this, new Grounding(modManager));
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
        Sponge.getEventManager().registerListeners(this, new EnchantingTableListener());
        Sponge.getEventManager().registerListeners(this, new BlockBreakListener());
        Sponge.getEventManager().registerListeners(this, new InventoryListener());
        Sponge.getEventManager().registerListeners(this, new InteractListener());
        Sponge.getEventManager().registerListeners(this, new ItemDropListener());
        Sponge.getEventManager().registerListeners(this, new FishingListener());
        Sponge.getEventManager().registerListeners(this, new DamageListener());
        Sponge.getEventManager().registerListeners(this, new AnvilListener());
        Sponge.getEventManager().registerListeners(this, ModManager.getInstance());
    }

}
