package net.draycia.minetinkersponge.data.interfaces;

import org.spongepowered.api.data.manipulator.DataManipulator;
import org.spongepowered.api.data.manipulator.DataManipulatorBuilder;
import org.spongepowered.api.data.manipulator.ImmutableDataManipulator;
import org.spongepowered.api.data.manipulator.immutable.ImmutableMappedData;
import org.spongepowered.api.data.manipulator.mutable.MappedData;

public interface MineTinkerItemModsData extends DataManipulator<MineTinkerItemModsData, MineTinkerItemModsData.Immutable>, MappedData<String, Integer, MineTinkerItemModsData, MineTinkerItemModsData.Immutable> {

    interface Immutable extends ImmutableDataManipulator<Immutable, MineTinkerItemModsData>, ImmutableMappedData<String, Integer, Immutable, MineTinkerItemModsData> {
    }

    interface Builder extends DataManipulatorBuilder<MineTinkerItemModsData, Immutable> {
    }
}
