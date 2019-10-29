package net.draycia.minetinkersponge.utils;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.cause.EventContext;
import org.spongepowered.api.event.cause.EventContextKeys;

import java.util.Optional;

public class ContextUtils {

    public static Optional<Player> getPlayerFromContext(EventContext eventContext) {
        if (eventContext.containsKey(EventContextKeys.PLAYER)) {
            return Optional.ofNullable(eventContext.get(EventContextKeys.PLAYER).orElse(null));
        } else {
            return Optional.empty();
        }
    }

}
