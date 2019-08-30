package net.draycia.minetinkersponge.data.interfaces;

import org.spongepowered.api.data.manipulator.DataManipulator;
import org.spongepowered.api.data.manipulator.DataManipulatorBuilder;
import org.spongepowered.api.data.manipulator.ImmutableDataManipulator;

public interface MineTinkerModifierIDData extends DataManipulator<MineTinkerModifierIDData, MineTinkerModifierIDData.Immutable> {

    interface Immutable extends ImmutableDataManipulator<Immutable, MineTinkerModifierIDData> {
    }

    interface Builder extends DataManipulatorBuilder<MineTinkerModifierIDData, Immutable> {
    }
}
