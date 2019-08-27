package net.draycia.minetinkersponge.data.interfaces;

import org.spongepowered.api.data.manipulator.DataManipulator;
import org.spongepowered.api.data.manipulator.DataManipulatorBuilder;
import org.spongepowered.api.data.manipulator.ImmutableDataManipulator;

public interface MineTinkerItemSlotData extends DataManipulator<MineTinkerItemSlotData, MineTinkerItemSlotData.Immutable> {

    interface Immutable extends ImmutableDataManipulator<Immutable, MineTinkerItemSlotData> {
    }

    interface Builder extends DataManipulatorBuilder<MineTinkerItemSlotData, Immutable> {

    }
}
