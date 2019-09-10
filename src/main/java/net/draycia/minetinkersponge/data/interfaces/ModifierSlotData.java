package net.draycia.minetinkersponge.data.interfaces;

import org.spongepowered.api.data.manipulator.DataManipulator;
import org.spongepowered.api.data.manipulator.DataManipulatorBuilder;
import org.spongepowered.api.data.manipulator.ImmutableDataManipulator;

public interface ModifierSlotData extends DataManipulator<ModifierSlotData, ModifierSlotData.Immutable> {

    interface Immutable extends ImmutableDataManipulator<Immutable, ModifierSlotData> {
    }

    interface Builder extends DataManipulatorBuilder<ModifierSlotData, Immutable> {

    }
}
