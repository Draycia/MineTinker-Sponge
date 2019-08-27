package net.draycia.minetinkersponge;

import com.google.inject.Inject;
import net.draycia.minetinkersponge.commands.AddModifierCommand;
import net.draycia.minetinkersponge.data.DataRegistrar;
import net.draycia.minetinkersponge.listeners.BlockBreakListener;
import net.draycia.minetinkersponge.modifiers.ModManager;
import net.draycia.minetinkersponge.modifiers.impls.Directing;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.filter.cause.Root;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.text.Text;

@Plugin(
        id = "minetinker-sponge",
        name = "MineTinker-Sponge"
)
public class MineTinkerSponge {

    @Inject
    PluginContainer container;

    @Inject
    private Logger logger;

    private DataRegistrar dataRegistrar;
    private ModManager modManager;

    @Listener
    public void onPreInit(GamePreInitializationEvent event) {
        DataRegistrar.registerDataManipulators();

        modManager = new ModManager();

        // Register modifiers
        modManager.registerModifier(this, new Directing(modManager));

        // Register commands
        CommandSpec addModifier = CommandSpec.builder()
                .description(Text.of("Applies the modifier to the held item (or increments its level)."))
                .permission("minetinker.commands.addmodifier")
                .arguments(GenericArguments.string(Text.of("modifier")))
                .executor(new AddModifierCommand(modManager))
                .build();

        Sponge.getCommandManager().register(this, addModifier, "addmod", "addmodifier");

        // Register listeners
        Sponge.getEventManager().registerListeners(this, new BlockBreakListener(modManager));
    }

    // Remove in production, merely for testing purposes
    @Listener
    public void onPlayerJoin(ClientConnectionEvent.Join event, @Root Player player) {
        ItemStack itemStack = ItemStack.of(ItemTypes.DIAMOND_PICKAXE);

        if (!modManager.convertItemStack(itemStack)) {
            logger.warn("Could not convert item!");
        }

        modManager.getModifier("directing").ifPresent(modifier -> {
            modManager.applyModifier(itemStack, modifier);
        });

        player.getInventory().offer(itemStack);

    }
}
