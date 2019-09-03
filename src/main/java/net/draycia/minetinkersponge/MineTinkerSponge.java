package net.draycia.minetinkersponge;

import net.draycia.minetinkersponge.commands.AddModifierCommand;
import net.draycia.minetinkersponge.commands.ConvertItemCommand;
import net.draycia.minetinkersponge.commands.GiveModifierItemCommand;
import net.draycia.minetinkersponge.data.DataRegistrar;
import net.draycia.minetinkersponge.listeners.AnvilListener;
import net.draycia.minetinkersponge.listeners.BlockBreakListener;
import net.draycia.minetinkersponge.listeners.DamageListener;
import net.draycia.minetinkersponge.modifiers.ModManager;
import net.draycia.minetinkersponge.modifiers.impls.AquaAffinity;
import net.draycia.minetinkersponge.modifiers.impls.BaneOfArthropods;
import net.draycia.minetinkersponge.modifiers.impls.BindingCurse;
import net.draycia.minetinkersponge.modifiers.impls.BlastProtection;
import net.draycia.minetinkersponge.modifiers.impls.DepthStrider;
import net.draycia.minetinkersponge.modifiers.impls.Directing;
import net.draycia.minetinkersponge.modifiers.impls.Efficiency;
import net.draycia.minetinkersponge.modifiers.impls.FeatherFalling;
import net.draycia.minetinkersponge.modifiers.impls.Sharpness;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.text.Text;

@Plugin(
        id = "minetinker-sponge",
        name = "MineTinker-Sponge"
)
public class MineTinkerSponge {

    private ModManager modManager;

    @Listener
    public void onPreInit(GamePreInitializationEvent event) {
        DataRegistrar.registerDataManipulators();

        modManager = new ModManager();

        // Register modifiers
        modManager.registerModifier(this, new AquaAffinity());
        modManager.registerModifier(this, new BaneOfArthropods());
        modManager.registerModifier(this, new BindingCurse());
        modManager.registerModifier(this, new BlastProtection());
        modManager.registerModifier(this, new DepthStrider());
        modManager.registerModifier(this, new Directing(modManager));
        modManager.registerModifier(this, new Efficiency());
        modManager.registerModifier(this, new FeatherFalling());
        modManager.registerModifier(this, new Sharpness());

        // Register commands
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

        // Register listeners
        Sponge.getEventManager().registerListeners(this, new BlockBreakListener(modManager));
        Sponge.getEventManager().registerListeners(this, new DamageListener(modManager));
        Sponge.getEventManager().registerListeners(this, new AnvilListener(modManager));
    }

}
