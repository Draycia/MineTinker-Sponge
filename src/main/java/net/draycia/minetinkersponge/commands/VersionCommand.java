package net.draycia.minetinkersponge.commands;

import com.google.inject.Inject;
import net.draycia.minetinkersponge.MineTinkerSponge;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;

public class VersionCommand implements CommandExecutor {

    @Inject
    private MineTinkerSponge plugin;

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) {
        src.sendMessage(Text.of("MineTinker version " + plugin.getContainer().getVersion().orElse("(VERSION MISSING)")));

        return CommandResult.success();
    }

}
