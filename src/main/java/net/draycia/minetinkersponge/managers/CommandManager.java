package net.draycia.minetinkersponge.managers;

import com.google.inject.Inject;
import com.google.inject.Injector;
import net.draycia.minetinkersponge.commands.*;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.text.Text;

public class CommandManager {

    @Inject
    public CommandManager(PluginContainer container, Injector injector) {
        // Command Root
        CommandSpec.Builder mainCommand = CommandSpec.builder();

        CommandSpec addModifier = CommandSpec.builder()
                .description(Text.of("Applies the modifier to the held item (or increments its level)."))
                .permission("minetinker.commands.addmodifier")
                .arguments(GenericArguments.string(Text.of("modifier")),
                        GenericArguments.optional(GenericArguments.integer(Text.of("amount"))))
                .executor(injector.getInstance(AddModifierCommand.class))
                .build();

        mainCommand = mainCommand.child(addModifier, "addmod", "addmodifier");

        CommandSpec convertItem = CommandSpec.builder()
                .description(Text.of("Converts the held item."))
                .permission("minetinker.commands.convertitem")
                .executor(injector.getInstance(ConvertItemCommand.class))
                .build();

        mainCommand = mainCommand.child(convertItem, "convertitem");

        CommandSpec giveModifierItem = CommandSpec.builder()
                .description(Text.of("Gives a modifier item for the specified modifier."))
                .permission("minetinker.commands.givemodifieritem")
                .arguments(GenericArguments.string(Text.of("modifier")))
                .executor(injector.getInstance(GiveModifierItemCommand.class))
                .build();

        mainCommand = mainCommand.child(giveModifierItem, "givemod", "givemodifier");

        CommandSpec addLevel = CommandSpec.builder()
                .description(Text.of("Increases the level of the item."))
                .permission("minetinker.commands.addlevel")
                .executor(injector.getInstance(AddLevelCommand.class))
                .build();

        mainCommand = mainCommand.child(addLevel, "addlevel");

        CommandSpec addSlots = CommandSpec.builder()
                .description(Text.of("Increases the modifier slots of the item."))
                .permission("minetinker.commands.addslots")
                .arguments(GenericArguments.optional(GenericArguments.integer(Text.of("amount"))))
                .executor(injector.getInstance(AddSlotsCommand.class))
                .build();

        mainCommand = mainCommand.child(addSlots, "addslots");

        CommandSpec version = CommandSpec.builder()
                .description(Text.of("Gets the version of the plugin"))
                .permission("minetinker.commands.version")
                .executor(injector.getInstance(VersionCommand.class))
                .build();

        mainCommand = mainCommand.child(version, "version", "v");

        if (Sponge.getPluginManager().isLoaded("teslalibs")) {
            CommandSpec modifiers = CommandSpec.builder()
                    .description(Text.of("Shows the modifier GUI"))
                    .permission("minetinker.commands.modifiers")
                    .executor(injector.getInstance(ModifiersCommand.class))
                    .build();

            mainCommand = mainCommand.child(modifiers, "modifiers", "mods");
        }

        Sponge.getCommandManager().register(container, mainCommand.build(), "mt", "minetinker");
    }

}
