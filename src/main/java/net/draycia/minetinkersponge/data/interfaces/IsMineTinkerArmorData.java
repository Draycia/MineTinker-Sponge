package net.draycia.minetinkersponge.data.interfaces;

import org.spongepowered.api.data.manipulator.DataManipulator;
import org.spongepowered.api.data.manipulator.DataManipulatorBuilder;
import org.spongepowered.api.data.manipulator.ImmutableDataManipulator;

public interface IsMineTinkerArmorData extends DataManipulator<IsMineTinkerArmorData, IsMineTinkerArmorData.Immutable> {

    interface Immutable extends ImmutableDataManipulator<Immutable, IsMineTinkerArmorData> {
    }

    interface Builder extends DataManipulatorBuilder<IsMineTinkerArmorData, Immutable> {
    }
}
