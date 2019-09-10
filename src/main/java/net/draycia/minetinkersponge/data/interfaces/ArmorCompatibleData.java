package net.draycia.minetinkersponge.data.interfaces;

import org.spongepowered.api.data.manipulator.DataManipulator;
import org.spongepowered.api.data.manipulator.DataManipulatorBuilder;
import org.spongepowered.api.data.manipulator.ImmutableDataManipulator;

public interface ArmorCompatibleData extends DataManipulator<ArmorCompatibleData, ArmorCompatibleData.Immutable> {

    interface Immutable extends ImmutableDataManipulator<Immutable, ArmorCompatibleData> {
    }

    interface Builder extends DataManipulatorBuilder<ArmorCompatibleData, Immutable> {
    }
}
