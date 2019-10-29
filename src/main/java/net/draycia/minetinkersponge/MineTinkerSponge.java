package net.draycia.minetinkersponge;

import com.google.inject.Inject;
import net.draycia.minetinkersponge.commands.*;
import net.draycia.minetinkersponge.listeners.*;
import net.draycia.minetinkersponge.modifiers.ModManager;
import net.draycia.minetinkersponge.modifiers.impls.Directing;
import net.draycia.minetinkersponge.modifiers.impls.Hammer;
import net.draycia.minetinkersponge.modifiers.impls.enchantments.*;
import net.draycia.minetinkersponge.modifiers.impls.potioneffects.InstantDamage;
import net.draycia.minetinkersponge.modifiers.impls.potioneffects.Poisonous;
import net.draycia.minetinkersponge.modifiers.impls.upgrades.DiamondUpgrade;
import net.draycia.minetinkersponge.modifiers.impls.upgrades.GoldUpgrade;
import net.draycia.minetinkersponge.modifiers.impls.upgrades.IronUpgrade;
import net.draycia.minetinkersponge.utils.InventoryGUIManager;
import net.draycia.minetinkersponge.utils.ItemLevelManager;
import net.draycia.minetinkersponge.utils.MTConfig;
import net.draycia.minetinkersponge.utils.PlayerNameManager;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.Command;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartingServerEvent;
import org.spongepowered.api.event.game.state.GameStoppingServerEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.text.Text;

@Plugin(
        id = "minetinker-sponge",
        name = "MineTinker-Sponge",
        description = "Adds an alternate enchantment system and new enchantments"
)
public class MineTinkerSponge {

    @Inject
    private PluginContainer container;

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

    private void registerModifiers() {
        // Enchantment Modifiers
        modManager.registerModifier(getContainer(), new AquaAffinity());
        modManager.registerModifier(getContainer(), new BaneOfArthropods());
        modManager.registerModifier(getContainer(), new BindingCurse());
        modManager.registerModifier(getContainer(), new BlastProtection());
        modManager.registerModifier(getContainer(), new DepthStrider());
        modManager.registerModifier(getContainer(), new Efficiency());
        modManager.registerModifier(getContainer(), new FeatherFalling());
        modManager.registerModifier(getContainer(), new FireAspect());
        modManager.registerModifier(getContainer(), new FireProtection());
        modManager.registerModifier(getContainer(), new Flame());
        modManager.registerModifier(getContainer(), new Fortune());
        modManager.registerModifier(getContainer(), new FrostWalker());
        modManager.registerModifier(getContainer(), new Infinity());
        modManager.registerModifier(getContainer(), new Knockback());
        modManager.registerModifier(getContainer(), new Looting());
        modManager.registerModifier(getContainer(), new LuckOfTheSea());
        modManager.registerModifier(getContainer(), new Lure());
        modManager.registerModifier(getContainer(), new Mending());
        modManager.registerModifier(getContainer(), new Power());
        modManager.registerModifier(getContainer(), new ProjectileProtection());
        modManager.registerModifier(getContainer(), new Protection());
        modManager.registerModifier(getContainer(), new Punch());
        modManager.registerModifier(getContainer(), new Respiration());
        modManager.registerModifier(getContainer(), new Sharpness());
        modManager.registerModifier(getContainer(), new SilkTouch());
        modManager.registerModifier(getContainer(), new Smite());
        modManager.registerModifier(getContainer(), new Sweeping());
        modManager.registerModifier(getContainer(), new Thorns());
        modManager.registerModifier(getContainer(), new Unbreaking());
        modManager.registerModifier(getContainer(), new VanishingCurse());

        // Custom Modifiers
        modManager.registerModifier(getContainer(), new Directing(modManager));
        modManager.registerModifier(getContainer(), new Hammer(modManager));

        // Potion Modifiers
        modManager.registerModifier(getContainer(), new Poisonous(modManager));
        modManager.registerModifier(getContainer(), new InstantDamage(modManager));

        // Upgrade Modifiers
        modManager.registerModifier(getContainer(), new IronUpgrade(modManager));
        modManager.registerModifier(getContainer(), new GoldUpgrade(modManager));
        modManager.registerModifier(getContainer(), new DiamondUpgrade(modManager));
    }

    private void registerCommands() {
        Command addModifier = Command.builder()
                .setShortDescription(Text.of("Applies the modifier to the held item (or increments its level)."))
                .setPermission("minetinker.commands.addmodifier")
                .parameters(
                        Parameter.string().setKey("modifier").build(),
                        Parameter.integerNumber().setKey("amount").optional().build()
                )
                .setExecutor(new AddModifierCommand(modManager))
                .build();

        Sponge.getCommandManager().register(getContainer(), addModifier, "addmod", "addmodifier");

        Command convertItem = Command.builder()
                .setShortDescription(Text.of("Converts the held item."))
                .setPermission("minetinker.commands.convertitem")
                .setExecutor(new ConvertItemCommand(modManager))
                .build();

        Sponge.getCommandManager().register(getContainer(), convertItem, "convertitem");

        Command giveModifierItem = Command.builder()
                .setShortDescription(Text.of("Gives a modifier item for the specified modifier."))
                .setPermission("minetinker.commands.givemodifieritem")
                .parameters(Parameter.string().setKey("modifier").build())
                .setExecutor(new GiveModifierItemCommand(modManager))
                .build();

        Sponge.getCommandManager().register(getContainer(), giveModifierItem, "givemod", "givemodifier");

        Command addLevel = Command.builder()
                .setShortDescription(Text.of("Increases the level of the item."))
                .setPermission("minetinker.commands.addlevel")
                .setExecutor(new AddLevelCommand(modManager))
                .build();

        Sponge.getCommandManager().register(getContainer(), addLevel, "addlevel");

        Command addSlots = Command.builder()
                .setShortDescription(Text.of("Increases the modifier slots of the item.."))
                .setPermission("minetinker.commands.givemodifieritem")
                .setExecutor(new AddSlotsCommand(modManager))
                .build();

        Sponge.getCommandManager().register(getContainer(), addSlots, "addslots");

        Command modifiers = Command.builder()
                .setShortDescription(Text.of("Shows the modifier GUI"))
                .setPermission("minetinker.commands.modifiers")
                .setExecutor(new ModifiersCommand(this))
                .build();

        Sponge.getCommandManager().register(getContainer(), modifiers, "modifiers", "mods");
    }

    private void registerListeners() {
        Sponge.getEventManager().registerListeners(getContainer(), new BlockBreakListener(modManager));
        Sponge.getEventManager().registerListeners(getContainer(), new InventoryListener(modManager));
        Sponge.getEventManager().registerListeners(getContainer(), new InteractListener(modManager));

        if (MTConfig.CONVERT_MOB_DROPS) {
            Sponge.getEventManager().registerListeners(getContainer(), new ItemDropListener(modManager));
        }

        Sponge.getEventManager().registerListeners(getContainer(), new FishingListener(modManager));
        Sponge.getEventManager().registerListeners(getContainer(), new DamageListener(modManager));
        Sponge.getEventManager().registerListeners(getContainer(), new AnvilListener(modManager));
        Sponge.getEventManager().registerListeners(getContainer(), modManager);
    }

}
