package net.draycia.minetinkersponge.data.interfaces;

import org.spongepowered.api.data.manipulator.DataManipulator;
import org.spongepowered.api.data.manipulator.DataManipulatorBuilder;
import org.spongepowered.api.data.manipulator.ImmutableDataManipulator;

public interface MineTinkerItemLevelData extends DataManipulator<MineTinkerItemLevelData, MineTinkerItemLevelData.Immutable> {

    interface Immutable extends ImmutableDataManipulator<Immutable, MineTinkerItemLevelData> {
    }

    interface Builder extends DataManipulatorBuilder<MineTinkerItemLevelData, Immutable> {

    }
}
