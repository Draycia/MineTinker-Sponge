package net.draycia.minetinkersponge.data.interfaces;

import org.spongepowered.api.data.manipulator.DataManipulator;
import org.spongepowered.api.data.manipulator.DataManipulatorBuilder;
import org.spongepowered.api.data.manipulator.ImmutableDataManipulator;

public interface IsMineTinkerData extends DataManipulator<IsMineTinkerData, IsMineTinkerData.Immutable> {

    interface Immutable extends ImmutableDataManipulator<Immutable, IsMineTinkerData> {
    }

    interface Builder extends DataManipulatorBuilder<IsMineTinkerData, Immutable> {
    }
}
