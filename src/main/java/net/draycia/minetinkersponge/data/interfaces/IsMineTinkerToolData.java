package net.draycia.minetinkersponge.data.interfaces;

import org.spongepowered.api.data.manipulator.DataManipulator;
import org.spongepowered.api.data.manipulator.DataManipulatorBuilder;
import org.spongepowered.api.data.manipulator.ImmutableDataManipulator;

public interface IsMineTinkerToolData extends DataManipulator<IsMineTinkerToolData, IsMineTinkerToolData.Immutable> {

    interface Immutable extends ImmutableDataManipulator<Immutable, IsMineTinkerToolData> {
    }

    interface Builder extends DataManipulatorBuilder<IsMineTinkerToolData, Immutable> {
    }
}
