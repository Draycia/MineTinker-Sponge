package net.draycia.minetinkersponge.data.interfaces;

import org.spongepowered.api.data.manipulator.DataManipulator;
import org.spongepowered.api.data.manipulator.DataManipulatorBuilder;
import org.spongepowered.api.data.manipulator.ImmutableDataManipulator;

public interface ModifierIdentifierData extends DataManipulator<ModifierIdentifierData, ModifierIdentifierData.Immutable> {

    interface Immutable extends ImmutableDataManipulator<Immutable, ModifierIdentifierData> {
    }

    interface Builder extends DataManipulatorBuilder<ModifierIdentifierData, Immutable> {
    }
}
