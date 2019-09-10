package net.draycia.minetinkersponge.data;

import net.draycia.minetinkersponge.data.impl.*;
import net.draycia.minetinkersponge.data.interfaces.*;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.DataRegistration;

public class DataRegistrar {

    public static void registerDataManipulators() {
        MTKeys.dummy();

        // MineTinker Compatible Items
        DataRegistration.builder()
                .name("Is MineTinker")
                .id("is_minetinker")
                .dataClass(ItemCompatibleData.class)
                .dataImplementation(ItemCompatibleDataImpl.class)
                .immutableClass(ItemCompatibleData.Immutable.class)
                .immutableImplementation(ItemCompatibleDataImpl.Immutable.class)
                .builder(new ItemCompatibleDataImpl.Builder())
                .build();

        Sponge.getDataManager().registerContentUpdater(ItemCompatibleDataImpl.class, new ItemCompatibleDataImpl.BoolEnabled1To2Updater());

        // MineTinker Compatible Tools
        DataRegistration.builder()
                .name("Is MineTinker Tool")
                .id("is_tool")
                .dataClass(ToolCompatibleData.class)
                .dataImplementation(ToolCompatibleDataImpl.class)
                .immutableClass(ToolCompatibleData.Immutable.class)
                .immutableImplementation(ToolCompatibleDataImpl.Immutable.class)
                .builder(new ToolCompatibleDataImpl.Builder())
                .build();

        Sponge.getDataManager().registerContentUpdater(ToolCompatibleDataImpl.class, new ToolCompatibleDataImpl.BoolEnabled1To2Updater());

        // MineTinker Compatible Armor
        DataRegistration.builder()
                .name("Is MineTinker Armor")
                .id("is_armor")
                .dataClass(ArmorCompatibleData.class)
                .dataImplementation(ArmorCompatibleDataImpl.class)
                .immutableClass(ArmorCompatibleData.Immutable.class)
                .immutableImplementation(ArmorCompatibleDataImpl.Immutable.class)
                .builder(new ArmorCompatibleDataImpl.Builder())
                .build();

        Sponge.getDataManager().registerContentUpdater(ArmorCompatibleDataImpl.class, new ArmorCompatibleDataImpl.BoolEnabled1To2Updater());

        // MineTinker Item Level
        DataRegistration.builder()
                .name("Item Level")
                .id("minetinker_level")
                .dataClass(ItemLevelData.class)
                .dataImplementation(ItemLevelDataImpl.class)
                .immutableClass(ItemLevelData.Immutable.class)
                .immutableImplementation(ItemLevelDataImpl.Immutable.class)
                .builder(new ItemLevelDataImpl.Builder())
                .build();

        Sponge.getDataManager().registerContentUpdater(ItemLevelDataImpl.class, new ItemLevelDataImpl.Int1To2Updater());

        // MineTinker Item XP
        DataRegistration.builder()
                .name("Item XP")
                .id("minetinker_xp")
                .dataClass(ItemExperienceData.class)
                .dataImplementation(ItemExperienceDataImpl.class)
                .immutableClass(ItemExperienceData.Immutable.class)
                .immutableImplementation(ItemExperienceDataImpl.Immutable.class)
                .builder(new ItemExperienceDataImpl.Builder())
                .build();

        Sponge.getDataManager().registerContentUpdater(ItemExperienceDataImpl.class, new ItemExperienceDataImpl.Int1To2Updater());

        // MineTinker Item XP
        DataRegistration.builder()
                .name("Item Modifier Slots")
                .id("minetinker_slots")
                .dataClass(ModifierSlotData.class)
                .dataImplementation(ModifierSlotDataImpl.class)
                .immutableClass(ModifierSlotData.Immutable.class)
                .immutableImplementation(ModifierSlotDataImpl.Immutable.class)
                .builder(new ModifierSlotDataImpl.Builder())
                .build();

        Sponge.getDataManager().registerContentUpdater(ModifierSlotDataImpl.class, new ModifierSlotDataImpl.Int1To2Updater());

        // MineTinker Modifier ID
        DataRegistration.builder()
                .name("Modifier ID")
                .id("modifier_id")
                .dataClass(ModifierIdentifierData.class)
                .dataImplementation(ModifierIdentifierDataImpl.class)
                .immutableClass(ModifierIdentifierData.Immutable.class)
                .immutableImplementation(ModifierIdentifierDataImpl.Immutable.class)
                .builder(new ModifierIdentifierDataImpl.Builder())
                .build();

        Sponge.getDataManager().registerContentUpdater(ModifierIdentifierDataImpl.class, new ModifierIdentifierDataImpl.String1To2Updater());

        // List of Modifiers The Item Has
        DataRegistration.builder()
                .name("MineTinker Modifiers")
                .id("mt_modifiers")
                .dataClass(ItemModifierListData.class)
                .dataImplementation(ModifierListDataImpl.class)
                .immutableClass(ItemModifierListData.Immutable.class)
                .immutableImplementation(ModifierListDataImpl.Immutable.class)
                .builder(new ModifierListDataImpl.Builder())
                .build();

        Sponge.getDataManager().registerContentUpdater(ModifierListDataImpl.class, new ModifierListDataImpl.Builder.List1To2Updater());
    }
}
