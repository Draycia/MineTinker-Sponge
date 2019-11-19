package net.draycia.minetinkersponge.managers;

import net.draycia.minetinkersponge.MineTinkerSponge;
import net.draycia.minetinkersponge.commands.*;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.text.Text;

public class CommandManager {

    private MineTinkerSponge plugin;
    private ModManager modManager = MineTinkerSponge.getModManager();

    public CommandManager(MineTinkerSponge plugin) {
        this.plugin = plugin;
    }

    public void registerCommands() {
        // Command Root
        CommandSpec.Builder mainCommand = CommandSpec.builder();

        CommandSpec addModifier = CommandSpec.builder()
                .description(Text.of("Applies the modifier to the held item (or increments its level)."))
                .permission("minetinker.commands.addmodifier")
                .arguments(GenericArguments.string(Text.of("modifier")),
                        GenericArguments.optional(GenericArguments.integer(Text.of("amount"))))
                .executor(new AddModifierCommand(modManager))
                .build();

        mainCommand = mainCommand.child(addModifier, "addmod", "addmodifier");

        CommandSpec convertItem = CommandSpec.builder()
                .description(Text.of("Converts the held item."))
                .permission("minetinker.commands.convertitem")
                .executor(new ConvertItemCommand(modManager))
                .build();

        mainCommand = mainCommand.child(convertItem, "convertitem");

        CommandSpec giveModifierItem = CommandSpec.builder()
                .description(Text.of("Gives a modifier item for the specified modifier."))
                .permission("minetinker.commands.givemodifieritem")
                .arguments(GenericArguments.string(Text.of("modifier")))
                .executor(new GiveModifierItemCommand(modManager))
                .build();

        mainCommand = mainCommand.child(giveModifierItem, "givemod", "givemodifier");

        CommandSpec addLevel = CommandSpec.builder()
                .description(Text.of("Increases the level of the item."))
                .permission("minetinker.commands.addlevel")
                .executor(new AddLevelCommand(modManager))
                .build();

        mainCommand = mainCommand.child(addLevel, "addlevel");

        CommandSpec addSlots = CommandSpec.builder()
                .description(Text.of("Increases the modifier slots of the item.."))
                .permission("minetinker.commands.addslots")
                .executor(new AddSlotsCommand(modManager))
                .build();

        mainCommand = mainCommand.child(addSlots, "addslots");

        if (Sponge.getPluginManager().isLoaded("TeslaLibs")) {
            CommandSpec modifiers = CommandSpec.builder()
                    .description(Text.of("Shows the modifier GUI"))
                    .permission("minetinker.commands.modifiers")
                    .executor(new ModifiersCommand(plugin))
                    .build();

            mainCommand = mainCommand.child(modifiers, "modifiers", "mods");
        }

        Sponge.getCommandManager().register(plugin, mainCommand.build(), "mt", "minetinker");
    }

}
