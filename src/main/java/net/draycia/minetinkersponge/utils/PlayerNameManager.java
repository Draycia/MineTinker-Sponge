package net.draycia.minetinkersponge.utils;

import net.draycia.minetinkersponge.MineTinkerSponge;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.manipulator.mutable.PotionEffectData;
import org.spongepowered.api.effect.potion.PotionEffect;
import org.spongepowered.api.effect.potion.PotionEffectTypes;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.living.ArmorStand;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.scoreboard.Scoreboard;
import org.spongepowered.api.scoreboard.critieria.Criteria;
import org.spongepowered.api.scoreboard.displayslot.DisplaySlots;
import org.spongepowered.api.scoreboard.objective.Objective;
import org.spongepowered.api.scoreboard.objective.displaymode.ObjectiveDisplayModes;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.Optional;

public class PlayerNameManager {

    private MineTinkerSponge mineTinkerSponge;

    private final String OBJECTIVE_ID = "CombatLevel";
    private final String OBJECTIVE_NAME = "Combat Level";

    public PlayerNameManager(MineTinkerSponge mineTinkerSponge) {
        this.mineTinkerSponge = mineTinkerSponge;
    }

    public void onGameStarted() {
        Optional<Scoreboard> serverScoreboard = Sponge.getServer().getServerScoreboard();

        if (serverScoreboard.isPresent()) {
            Scoreboard globalScoreboard = serverScoreboard.get();

            globalScoreboard.getObjective(OBJECTIVE_ID).ifPresent(globalScoreboard::removeObjective);

            Objective objective = Objective.builder()
                    .name(OBJECTIVE_ID)
                    .displayName(Text.of(TextColors.DARK_RED, OBJECTIVE_NAME))
                    .criterion(Criteria.DUMMY)
                    .objectiveDisplayMode(ObjectiveDisplayModes.INTEGER)
                    .build();

            globalScoreboard.addObjective(objective);
            globalScoreboard.updateDisplaySlot(objective, DisplaySlots.BELOW_NAME);
        } else {
            System.out.println("Global scoreboard couldn't be loaded");
        }
    }

    public void onGameStopped() {
        Sponge.getServer().getServerScoreboard()
                .ifPresent(scoreboard -> scoreboard.getObjective(OBJECTIVE_ID)
                        .ifPresent(scoreboard::removeObjective));
    }

    public void startScheduler() {
        Task.builder().execute(() -> {
            for (Player player : Sponge.getServer().getOnlinePlayers()) {
                Entity entity = player.getPassengers().get(0).getPassengers().get(0);

                entity.offer(Keys.DISPLAY_NAME, Text.of(TextColors.RED, "Combat Level: ", TextColors.WHITE,
                        mineTinkerSponge.getItemLevelManager().getPlayerCombatLevel(player, false)));
            }
        }).delayTicks(20).intervalTicks(20).submit(mineTinkerSponge);
    }

}
