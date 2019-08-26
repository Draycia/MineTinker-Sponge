package net.draycia.minetinkersponge.data.interfaces;

import org.spongepowered.api.data.manipulator.DataManipulator;
import org.spongepowered.api.data.manipulator.DataManipulatorBuilder;
import org.spongepowered.api.data.manipulator.ImmutableDataManipulator;
import org.spongepowered.api.data.value.immutable.ImmutableValue;
import org.spongepowered.api.data.value.mutable.Value;

public interface IsMineTinkerArmorData extends DataManipulator<IsMineTinkerArmorData, IsMineTinkerArmorData.Immutable> {

    Value<Boolean> enabled();

    interface Immutable extends ImmutableDataManipulator<Immutable, IsMineTinkerArmorData> {
        ImmutableValue<Boolean> enabled();
    }

    interface Builder extends DataManipulatorBuilder<IsMineTinkerArmorData, Immutable> {

    }
}
