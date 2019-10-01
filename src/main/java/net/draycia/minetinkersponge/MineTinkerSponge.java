package net.draycia.minetinkersponge;

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
import net.draycia.minetinkersponge.utils.ItemLevelManager;
import net.draycia.minetinkersponge.utils.MTConfig;
import net.draycia.minetinkersponge.utils.PlayerNameManager;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.manipulator.mutable.PotionEffectData;
import org.spongepowered.api.effect.potion.PotionEffect;
import org.spongepowered.api.effect.potion.PotionEffectTypes;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.living.ArmorStand;
import org.spongepowered.api.entity.living.monster.Slime;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.game.state.*;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.ArrayList;
import java.util.Collections;

@Plugin(
        id = "minetinker-sponge",
        name = "MineTinker-Sponge"
)
public class MineTinkerSponge {

    private ModManager modManager;
    private ItemLevelManager itemLevelManager;
    private PlayerNameManager playerNameManager;

    public ModManager getModManager() {
        return modManager;
    }

    public ItemLevelManager getItemLevelManager() {
        return itemLevelManager;
    }

    @Listener
    public void onPreInit(GamePreInitializationEvent event) {
        DataRegistrar.registerDataManipulators();

        modManager = new ModManager();
        itemLevelManager = new ItemLevelManager(modManager);
        playerNameManager = new PlayerNameManager(this);

        registerModifiers();
        registerCommands();
        registerListeners();
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
    public void onPlayerJoin(ClientConnectionEvent.Join event) {
        Player player = event.getTargetEntity();

        for (Entity entity : player.getPassengers()) {
            for (Entity entity1 : entity.getPassengers()) {
                for (Entity entity2 : entity1.getPassengers()) {
                    for (Entity entity3 : entity2.getPassengers()) {
                        if (!(entity3 instanceof Player)) {
                            entity3.remove();
                        }
                    }
                    if (!(entity2 instanceof Player)) {
                        entity2.remove();
                    }
                }
                if (!(entity1 instanceof Player)) {
                    entity1.remove();
                }
            }
            if (!(entity instanceof Player)) {
                entity.remove();
            }
        }

        player.clearPassengers();

        Entity silverfish = player.getWorld().createEntity(EntityTypes.SILVERFISH, player.getLocation().getPosition());
        PotionEffect potionEffect = PotionEffect.builder().potionType(PotionEffectTypes.INVISIBILITY).duration(99999).amplifier(9).ambience(false).build();
        PotionEffectData effectData = silverfish.getOrCreate(PotionEffectData.class).get();
        effectData.addElement(potionEffect);
        silverfish.offer(effectData);
        silverfish.offer(Keys.AI_ENABLED, false);

        player.getWorld().spawnEntity(silverfish);

        Entity armorStand = player.getWorld().createEntity(EntityTypes.ARMOR_STAND, player.getLocation().getPosition());
        armorStand.offer(Keys.INVISIBLE, true);
        armorStand.offer(Keys.ARMOR_STAND_MARKER, true);
        armorStand.offer(Keys.ARMOR_STAND_IS_SMALL, true);
        armorStand.offer(Keys.CUSTOM_NAME_VISIBLE, true);
        armorStand.offer(Keys.DISPLAY_NAME, Text.of(TextColors.RED, "Combat Level: ", TextColors.WHITE, getItemLevelManager().getPlayerCombatLevel(player, false)));

        player.getWorld().spawnEntity(armorStand);

        Entity silverfish2 = player.getWorld().createEntity(EntityTypes.SILVERFISH, player.getLocation().getPosition());
        PotionEffectData effectData2 = silverfish2.getOrCreate(PotionEffectData.class).get();
        effectData2.addElement(potionEffect);
        silverfish2.offer(effectData2);
        silverfish2.offer(Keys.AI_ENABLED, false);

        player.getWorld().spawnEntity(silverfish2);

        ArmorStand armorStand2 = (ArmorStand) player.getWorld().createEntity(EntityTypes.ARMOR_STAND, player.getLocation().getPosition());

        armorStand2.offer(Keys.INVISIBLE, true);
        armorStand2.offer(Keys.ARMOR_STAND_MARKER, true);
        armorStand2.offer(Keys.ARMOR_STAND_IS_SMALL, true);
        armorStand2.offer(Keys.CUSTOM_NAME_VISIBLE, true);
        armorStand2.offer(Keys.DISPLAY_NAME, Text.of(TextColors.GRAY, player.getName()));

        player.getWorld().spawnEntity(armorStand2);

        silverfish.addPassenger(armorStand);
        armorStand.addPassenger(silverfish2);
        silverfish2.addPassenger(armorStand2);
        player.addPassenger(silverfish);
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
