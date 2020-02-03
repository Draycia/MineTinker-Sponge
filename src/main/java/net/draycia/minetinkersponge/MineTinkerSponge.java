package net.draycia.minetinkersponge;

import com.google.inject.Inject;
import com.google.inject.Injector;
import net.draycia.minetinkersponge.data.DataRegistrar;
import net.draycia.minetinkersponge.listeners.*;
import net.draycia.minetinkersponge.managers.*;
import net.draycia.minetinkersponge.modifiers.impls.*;
import net.draycia.minetinkersponge.modifiers.impls.enchantments.*;
import net.draycia.minetinkersponge.modifiers.impls.upgrades.DiamondUpgrade;
import net.draycia.minetinkersponge.modifiers.impls.upgrades.GoldUpgrade;
import net.draycia.minetinkersponge.modifiers.impls.upgrades.IronUpgrade;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.game.GameReloadEvent;
import org.spongepowered.api.event.game.state.*;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.plugin.Dependency;
import org.spongepowered.api.plugin.Plugin;

@Plugin(
        id = "minetinker-sponge",
        name = "MineTinker-Sponge",
        description = "Adds an alternate enchantment system and new enchantments",
        dependencies = @Dependency(id = "teslalibs", optional = true)
)
public class MineTinkerSponge {

    @Inject
    private Injector pluginInjector;

    private ConfigManager configManager;
    private InventoryGUIManager guiManager = null;

    @Listener
    public void onPreInit(GamePreInitializationEvent event) {
        DataRegistrar.registerDataManipulators();

        registerModifiers();

        configManager = pluginInjector.getInstance(ConfigManager.class);
        configManager.reloadConfig();

        Sponge.getEventManager().registerListeners(this, pluginInjector.getInstance(ModManager.class));
    }

    @Listener
    public void onInit(GameInitializationEvent event) {
        pluginInjector.getInstance(CommandManager.class);
        //commandManager = new CommandManager();

        registerListeners();

        if (Sponge.getPluginManager().isLoaded("teslalibs")) {
            guiManager = pluginInjector.getInstance(InventoryGUIManager.class);
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
        ModManager.registerModifier(this, new Ender());
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
        ModManager.registerModifier(this, new DragonsBreath());
        ModManager.registerModifier(this, new Grounding());
        //ModManager.registerModifier(this, new Hammer(logger));
        ModManager.registerModifier(this, new Kinetic());
        ModManager.registerModifier(this, new Lifesteal());

        // Upgrade Modifiers
        ModManager.registerModifier(this, new IronUpgrade());
        ModManager.registerModifier(this, new GoldUpgrade());
        ModManager.registerModifier(this, new DiamondUpgrade());
    }

    private void registerListeners() {
        Sponge.getEventManager().registerListeners(this, pluginInjector.getInstance(EnchantingTableListener.class));
        Sponge.getEventManager().registerListeners(this, pluginInjector.getInstance(BlockBreakListener.class));
        Sponge.getEventManager().registerListeners(this, pluginInjector.getInstance(InventoryListener.class));
        Sponge.getEventManager().registerListeners(this, pluginInjector.getInstance(InteractListener.class));
        Sponge.getEventManager().registerListeners(this, pluginInjector.getInstance(ItemDropListener.class));
        Sponge.getEventManager().registerListeners(this, pluginInjector.getInstance(FishingListener.class));
        Sponge.getEventManager().registerListeners(this, pluginInjector.getInstance(DamageListener.class));
        Sponge.getEventManager().registerListeners(this, pluginInjector.getInstance(AnvilListener.class));
    }

    public InventoryGUIManager getGuiManager() {
        return guiManager;
    }
}
