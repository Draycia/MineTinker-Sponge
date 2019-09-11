package net.draycia.minetinkersponge.data;

import net.draycia.minetinkersponge.data.impl.*;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.DataRegistration;
import org.spongepowered.api.data.manipulator.DataManipulator;
import org.spongepowered.api.data.manipulator.DataManipulatorBuilder;
import org.spongepowered.api.data.manipulator.ImmutableDataManipulator;
import org.spongepowered.api.data.manipulator.mutable.common.AbstractData;
import org.spongepowered.api.data.persistence.AbstractDataBuilder;
import org.spongepowered.api.data.persistence.DataContentUpdater;

public class DataRegistrar {

    public static void registerDataManipulators() {
        MTKeys.dummy();

        registerUpdater("mt_item_compat", ItemCompatibleData.class, ItemCompatibleData.Immutable.class,
                new ItemCompatibleData.Builder(), new ItemCompatibleData.VersionUpdater());

        registerUpdater("mt_item_level", ItemLevelData.class, ItemLevelData.Immutable.class,
                new ItemLevelData.Builder(), new ItemLevelData.VersionUpdater());

        registerUpdater("mt_item_experience", ItemExperienceData.class, ItemExperienceData.Immutable.class,
                new ItemExperienceData.Builder(), new ItemExperienceData.VersionUpdater());

        registerUpdater("mt_modifier_slots", ModifierSlotData.class, ModifierSlotData.Immutable.class,
                new ModifierSlotData.Builder(), new ModifierSlotData.VersionUpdater());

        registerUpdater("mt_modifier_id", ModifierIdentifierData.class, ModifierIdentifierData.Immutable.class,
                new ModifierIdentifierData.Builder(), new ModifierIdentifierData.VersionUpdater());

        registerUpdater("mt_modifier_list", ItemModifierListData.class, ItemModifierListData.Immutable.class,
                new ItemModifierListData.Builder(), new ItemModifierListData.Builder.VersionUpdater());
    }

    public static <D extends DataManipulator<D,M>, M extends ImmutableDataManipulator<M,D>, DMB extends DataManipulatorBuilder<D, M>>
                    void registerUpdater(String id, Class<D> dataClass, Class<M> dataImmutable, DMB dataBuilder, DataContentUpdater contentUpdater) {

        DataRegistration.builder().id(id).dataClass(dataClass).immutableClass(dataImmutable).builder(dataBuilder).build();
        Sponge.getDataManager().registerContentUpdater(dataClass, contentUpdater);
    }

}
