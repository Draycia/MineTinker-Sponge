package net.draycia.minetinkersponge.managers;

import com.google.inject.Inject;
import com.google.inject.Injector;
import net.draycia.minetinkersponge.commands.*;
import org.spongepowered.api.CatalogTypes;
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

        mainCommand.child(addModifier, "addmod", "addmodifier");

        CommandSpec convertItem = CommandSpec.builder()
                .description(Text.of("Converts the held item."))
                .permission("minetinker.commands.convertitem")
                .executor(injector.getInstance(ConvertItemCommand.class))
                .build();

        mainCommand.child(convertItem, "convertitem", "convert");

        CommandSpec giveModifierItem = CommandSpec.builder()
                .description(Text.of("Gives a modifier item for the specified modifier."))
                .permission("minetinker.commands.givemodifieritem")
                .arguments(GenericArguments.string(Text.of("modifier")))
                .executor(injector.getInstance(GiveModifierItemCommand.class))
                .build();

        mainCommand.child(giveModifierItem, "givemod", "givemodifier");

        CommandSpec addLevel = CommandSpec.builder()
                .description(Text.of("Increases the level of the item."))
                .permission("minetinker.commands.addlevel")
                .arguments(GenericArguments.optional(GenericArguments.integer(Text.of("amount"))))
                .executor(injector.getInstance(AddLevelCommand.class))
                .build();

        mainCommand.child(addLevel, "addlevel");

        CommandSpec addSlots = CommandSpec.builder()
                .description(Text.of("Increases the modifier slots of the item."))
                .permission("minetinker.commands.addslots")
                .arguments(GenericArguments.optional(GenericArguments.integer(Text.of("amount"))))
                .executor(injector.getInstance(AddSlotsCommand.class))
                .build();

        mainCommand.child(addSlots, "addslots");

        CommandSpec version = CommandSpec.builder()
                .description(Text.of("Gets the version of the plugin"))
                .permission("minetinker.commands.version")
                .executor(injector.getInstance(VersionCommand.class))
                .build();

        mainCommand.child(version, "version", "v");

        if (Sponge.getPluginManager().isLoaded("teslalibs")) {
            CommandSpec modifiers = CommandSpec.builder()
                    .description(Text.of("Shows the modifier GUI."))
                    .permission("minetinker.commands.modifiers")
                    .executor(injector.getInstance(ModifiersCommand.class))
                    .build();

            mainCommand.child(modifiers, "modifiers", "mods");
        }

        CommandSpec giveItem = CommandSpec.builder()
                .description(Text.of("Creates and gives items with the arguments given."))
                .permission("minetinker.commands.giveitem")
                .arguments(
                        GenericArguments.player(Text.of("player")),
                        GenericArguments.catalogedElement(Text.of("item"), CatalogTypes.ITEM_TYPE),
                        GenericArguments.optional(GenericArguments.remainingJoinedStrings(Text.of("modifier")))
                )
                .executor(injector.getInstance(GiveItemCommand.class))
                .build();

        mainCommand.child(giveItem, "giveitem", "givei", "gi", "give");

        Sponge.getCommandManager().register(container, mainCommand.build(), "mt", "minetinker");
    }

}
