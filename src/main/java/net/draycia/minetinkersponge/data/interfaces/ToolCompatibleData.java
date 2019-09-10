package net.draycia.minetinkersponge.data.interfaces;

import org.spongepowered.api.data.manipulator.DataManipulator;
import org.spongepowered.api.data.manipulator.DataManipulatorBuilder;
import org.spongepowered.api.data.manipulator.ImmutableDataManipulator;

public interface ToolCompatibleData extends DataManipulator<ToolCompatibleData, ToolCompatibleData.Immutable> {

    interface Immutable extends ImmutableDataManipulator<Immutable, ToolCompatibleData> {
    }

    interface Builder extends DataManipulatorBuilder<ToolCompatibleData, Immutable> {
    }
}
