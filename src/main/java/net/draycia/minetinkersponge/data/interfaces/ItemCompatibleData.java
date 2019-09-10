package net.draycia.minetinkersponge.data.interfaces;

import org.spongepowered.api.data.manipulator.DataManipulator;
import org.spongepowered.api.data.manipulator.DataManipulatorBuilder;
import org.spongepowered.api.data.manipulator.ImmutableDataManipulator;

public interface ItemCompatibleData extends DataManipulator<ItemCompatibleData, ItemCompatibleData.Immutable> {

    interface Immutable extends ImmutableDataManipulator<Immutable, ItemCompatibleData> {
    }

    interface Builder extends DataManipulatorBuilder<ItemCompatibleData, Immutable> {
    }
}
