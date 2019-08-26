package net.draycia.minetinkersponge.data.interfaces;

import org.spongepowered.api.data.manipulator.DataManipulator;
import org.spongepowered.api.data.manipulator.DataManipulatorBuilder;
import org.spongepowered.api.data.manipulator.ImmutableDataManipulator;

public interface MineTinkerItemXPData extends DataManipulator<MineTinkerItemXPData, MineTinkerItemXPData.Immutable> {

    interface Immutable extends ImmutableDataManipulator<Immutable, MineTinkerItemXPData> {
    }

    interface Builder extends DataManipulatorBuilder<MineTinkerItemXPData, Immutable> {

    }
}
