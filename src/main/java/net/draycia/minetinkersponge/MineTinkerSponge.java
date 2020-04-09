package net.draycia.minetinkersponge;

import com.flowpowered.math.vector.Vector3d;
import com.google.inject.Inject;
import com.google.inject.Injector;
import net.draycia.minetinkersponge.data.DataRegistrar;
import net.draycia.minetinkersponge.data.MTRegistryEvent;
import net.draycia.minetinkersponge.listeners.*;
import net.draycia.minetinkersponge.managers.*;
import net.draycia.minetinkersponge.modifiers.Modifier;
import net.draycia.minetinkersponge.modifiers.impls.*;
import net.draycia.minetinkersponge.utils.ItemTypeUtils;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.value.mutable.Value;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.Item;
import org.spongepowered.api.entity.living.Living;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.game.GameRegistryEvent;
import org.spongepowered.api.event.game.GameReloadEvent;
import org.spongepowered.api.event.game.state.*;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.item.enchantment.EnchantmentType;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;
import org.spongepowered.api.item.inventory.entity.MainPlayerInventory;
import org.spongepowered.api.item.inventory.query.QueryOperationTypes;
import org.spongepowered.api.item.recipe.crafting.CraftingRecipe;
import org.spongepowered.api.plugin.Dependency;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.List;
import java.util.Optional;

@Plugin(
        id = "minetinker-sponge",
        name = "MineTinker-Sponge",
        description = "Adds an alternate enchantment system and new enchantments",
        dependencies = @Dependency(id = "teslalibs", optional = true)
)
public class MineTinkerSponge {

    @Inject
    private Injector pluginInjector;

    @Inject
    private Logger logger;

    private ConfigManager configManager;
    private InventoryGUIManager guiManager = null;

    private static ItemTypeUtils itemTypeUtils;

    public static ItemTypeUtils getItemTypeUtils() {
        return itemTypeUtils;
    }

    @Listener
    public void onConstruct(GameConstructionEvent event) {
        DataRegistrar.registerDataManipulators();

        configManager = pluginInjector.getInstance(ConfigManager.class);
        configManager.reloadConfig();

        itemTypeUtils = configManager.getItemTypeUtils();

        //Sponge.getRegistry().registerModule(Modifier.class, ModManager.INSTANCE);
    }

    @Listener
    public void onPreInit(GamePreInitializationEvent event) {
        MTRegistryEvent<Modifier> mtRegistryEvent = new MTRegistryEvent<>(Sponge.getCauseStackManager().getCurrentCause(),
                Modifier.class, ModManager.INSTANCE);

        Sponge.getEventManager().post(mtRegistryEvent);
    }

    @Listener
    public void onInit(GameInitializationEvent event) {
        configManager.setupModifierConfigs();

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

    @Listener
    public void registerModifiers(GameRegistryEvent.Register<Modifier> event) {
        logger.info("Registering default modifiers!");

        // Enchantment Modifiers
        Sponge.getRegistry().getAllOf(EnchantmentType.class).stream().map(EnchantmentModifier::new).forEach(event::register);

        // Custom Modifiers
        event.register(new AutoSmelt());
        event.register(new Directing());
        event.register(new DragonsBreath());
        event.register(new Grounding());
        event.register(new Kinetic());
        event.register(new Lifesteal());
        //event.register(new Magnetic());
        //event.register(new Stasis());

        // Upgrade Modifiers
        event.register(new IronUpgrade());
        event.register(new GoldUpgrade());
        event.register(new DiamondUpgrade());
    }

//    TODO: Fix this, Register<CraftingRecipe> is called before Register<Modifier>
    @Listener
    public void registerRecipes(GameRegistryEvent.Register<CraftingRecipe> event) {
        logger.info("Registering modifier recipes!");

        ModManager.INSTANCE.getAll().stream().map(Modifier::getRecipe).filter(Optional::isPresent).map(Optional::get).forEach(mod -> {
            event.register(mod);
            logger.info("Registering recipe for " + mod.getId());
        });
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

    public static void transferItemStacks(Player player, List<ItemStackSnapshot> items) {
        Inventory inventory = player.getInventory()
                .query(QueryOperationTypes.INVENTORY_TYPE.of(MainPlayerInventory.class));

        for (ItemStackSnapshot item : items) {
            ItemStack itemStack = item.createStack();

            if (inventory.canFit(itemStack)) {
                inventory.offer(itemStack);
            } else {
                Location<World> location = player.getLocation();

                Entity itemEntity = location.createEntity(EntityTypes.ITEM);
                itemEntity.offer(Keys.ACTIVE_ITEM, item);

                location.spawnEntity(itemEntity);
            }
        }
    }

    public static void transferItemEntities(Player player, List<Item> items) {
        Inventory inventory = player.getInventory()
                .query(QueryOperationTypes.INVENTORY_TYPE.of(MainPlayerInventory.class));

        for (Item item : items) {
            Value<ItemStackSnapshot> itemValue = item.item();

            if (itemValue.exists()) {
                ItemStack itemStack = itemValue.get().createStack();

                if (inventory.canFit(itemStack)) {
                    inventory.offer(itemStack);
                } else {
                    item.setLocation(player.getLocation());
                }
            }
        }
    }

    public static double knockbackLiving(Vector3d location, Living entity, double multiplier) {
        Vector3d targetPosition = entity.getLocation().getPosition();
        double distance = location.distance(targetPosition);

        Vector3d velocity = location.sub(targetPosition).mul(distance / 4).mul(multiplier).negate();
        velocity.add(0, 0.25, 0);
        entity.setVelocity(velocity);

        return distance;
    }

}
