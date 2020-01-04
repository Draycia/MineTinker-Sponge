package net.draycia.minetinkersponge.managers;

import com.google.inject.Inject;
import net.draycia.minetinkersponge.MineTinkerSponge;
import net.draycia.minetinkersponge.commands.*;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.text.Text;

public class CommandManager {

    @Inject private MineTinkerSponge plugin;

    public CommandManager() {
        // Command Root
        CommandSpec.Builder mainCommand = CommandSpec.builder();

        CommandSpec addModifier = CommandSpec.builder()
                .description(Text.of("Applies the modifier to the held item (or increments its level)."))
                .permission("minetinker.commands.addmodifier")
                .arguments(GenericArguments.string(Text.of("modifier")),
                        GenericArguments.optional(GenericArguments.integer(Text.of("amount"))))
                .executor(new AddModifierCommand())
                .build();

        mainCommand = mainCommand.child(addModifier, "addmod", "addmodifier");

        CommandSpec convertItem = CommandSpec.builder()
                .description(Text.of("Converts the held item."))
                .permission("minetinker.commands.convertitem")
                .executor(new ConvertItemCommand())
                .build();

        mainCommand = mainCommand.child(convertItem, "convertitem");

        CommandSpec giveModifierItem = CommandSpec.builder()
                .description(Text.of("Gives a modifier item for the specified modifier."))
                .permission("minetinker.commands.givemodifieritem")
                .arguments(GenericArguments.string(Text.of("modifier")))
                .executor(new GiveModifierItemCommand())
                .build();

        mainCommand = mainCommand.child(giveModifierItem, "givemod", "givemodifier");

        CommandSpec addLevel = CommandSpec.builder()
                .description(Text.of("Increases the level of the item."))
                .permission("minetinker.commands.addlevel")
                .executor(new AddLevelCommand())
                .build();

        mainCommand = mainCommand.child(addLevel, "addlevel");

        CommandSpec addSlots = CommandSpec.builder()
                .description(Text.of("Increases the modifier slots of the item."))
                .permission("minetinker.commands.addslots")
                .arguments(GenericArguments.optional(GenericArguments.integer(Text.of("amount"))))
                .executor(new AddSlotsCommand())
                .build();

        mainCommand = mainCommand.child(addSlots, "addslots");

        CommandSpec version = CommandSpec.builder()
                .description(Text.of("Gets the version of the plugin"))
                .permission("minetinker.commands.version")
                .executor(new VersionCommand())
                .build();

        mainCommand = mainCommand.child(version, "version", "v");

        if (Sponge.getPluginManager().isLoaded("teslalibs")) {
            CommandSpec modifiers = CommandSpec.builder()
                    .description(Text.of("Shows the modifier GUI"))
                    .permission("minetinker.commands.modifiers")
                    .executor(new ModifiersCommand())
                    .build();

            mainCommand = mainCommand.child(modifiers, "modifiers", "mods");
        }

        Sponge.getCommandManager().register(plugin.getContainer(), mainCommand.build(), "mt", "minetinker");
    }

}
