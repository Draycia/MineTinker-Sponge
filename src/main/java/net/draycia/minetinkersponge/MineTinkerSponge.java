package net.draycia.minetinkersponge;

import com.google.inject.Inject;
import com.google.inject.Injector;
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
    private InventoryGUIManager guiManager = null;

    public PluginContainer getContainer() {
        return container;
    }

    @Listener
    public void onPreInit(GamePreInitializationEvent event) {
        SimpleBinderModule module = new SimpleBinderModule(this);
        Injector injector = module.createInjector();
        injector.injectMembers(this);

        DataRegistrar.registerDataManipulators();

        registerModifiers();

        configManager = new ConfigManager(configDir, defaultConfig, configLoader, logger);
        configManager.reloadConfig();

        new CommandManager();

        registerListeners();

        if (Sponge.getPluginManager().isLoaded("teslalibs")) {
            guiManager = new InventoryGUIManager();
        }
    }

    @Listener
    public void reload(GameReloadEvent event) {
        configManager.reloadConfig();
    }

    private void registerModifiers() {
        // Enchantment Modifiers
        ModManager.registerModifier(this, new AquaAffinity());
        ModManager.registerModifier(this, new BaneOfArthropods());
        ModManager.registerModifier(this, new BindingCurse());
        ModManager.registerModifier(this, new BlastProtection());
        ModManager.registerModifier(this, new DepthStrider());
        ModManager.registerModifier(this, new Efficiency());
        ModManager.registerModifier(this, new FeatherFalling());
        ModManager.registerModifier(this, new FireAspect());
        ModManager.registerModifier(this, new FireProtection());
        ModManager.registerModifier(this, new Flame());
        ModManager.registerModifier(this, new Fortune());
        ModManager.registerModifier(this, new FrostWalker());
        ModManager.registerModifier(this, new Infinity());
        ModManager.registerModifier(this, new Knockback());
        ModManager.registerModifier(this, new Looting());
        ModManager.registerModifier(this, new LuckOfTheSea());
        ModManager.registerModifier(this, new Lure());
        ModManager.registerModifier(this, new Mending());
        ModManager.registerModifier(this, new Power());
        ModManager.registerModifier(this, new ProjectileProtection());
        ModManager.registerModifier(this, new Protection());
        ModManager.registerModifier(this, new Punch());
        ModManager.registerModifier(this, new Respiration());
        ModManager.registerModifier(this, new Sharpness());
        ModManager.registerModifier(this, new SilkTouch());
        ModManager.registerModifier(this, new Smite());
        ModManager.registerModifier(this, new Sweeping());
        ModManager.registerModifier(this, new Thorns());
        ModManager.registerModifier(this, new Unbreaking());
        ModManager.registerModifier(this, new VanishingCurse());

        // Custom Modifiers
        ModManager.registerModifier(this, new AutoSmelt());
        ModManager.registerModifier(this, new Directing());
        ModManager.registerModifier(this, new Grounding());
        //ModManager.registerModifier(this, new Hammer(ModManager));
        ModManager.registerModifier(this, new Kinetic());

        // Potion Modifiers
        ModManager.registerModifier(this, new Poisonous());
        ModManager.registerModifier(this, new InstantDamage());

        // Upgrade Modifiers
        ModManager.registerModifier(this, new IronUpgrade());
        ModManager.registerModifier(this, new GoldUpgrade());
        ModManager.registerModifier(this, new DiamondUpgrade());
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
        Sponge.getEventManager().registerListeners(this, new ModManager());
    }

    public InventoryGUIManager getGuiManager() {
        return guiManager;
    }
}
