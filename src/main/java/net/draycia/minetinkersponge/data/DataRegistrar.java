package net.draycia.minetinkersponge.data;

import net.draycia.minetinkersponge.data.impl.IsMineTinkerArmorDataImpl;
import net.draycia.minetinkersponge.data.impl.IsMineTinkerDataImpl;
import net.draycia.minetinkersponge.data.impl.IsMineTinkerToolDataImpl;
import net.draycia.minetinkersponge.data.impl.MinetinkerItemModsDataImpl;
import net.draycia.minetinkersponge.data.interfaces.IsMineTinkerArmorData;
import net.draycia.minetinkersponge.data.interfaces.IsMineTinkerData;
import net.draycia.minetinkersponge.data.interfaces.IsMineTinkerToolData;
import net.draycia.minetinkersponge.data.interfaces.MineTinkerItemModsData;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.DataRegistration;
import org.spongepowered.api.plugin.PluginContainer;

public class DataRegistrar {

    public DataRegistrar(PluginContainer container) {
        MTKeys.dummy();

        // MineTinker Compatible Items
        DataRegistration.builder()
                .dataName("Is MineTinker")
                .manipulatorId("is_minetinker")
                .dataClass(IsMineTinkerData.class)
                .dataImplementation(IsMineTinkerDataImpl.class)
                .immutableClass(IsMineTinkerData.Immutable.class)
                .immutableImplementation(IsMineTinkerDataImpl.Immutable.class)
                .builder(new IsMineTinkerDataImpl.Builder())
                .buildAndRegister(container);

        Sponge.getDataManager().registerContentUpdater(IsMineTinkerDataImpl.class, new IsMineTinkerDataImpl.BoolEnabled1To2Updater());

        // MineTinker Compatible Tools
        DataRegistration.builder()
                .dataName("Is MineTinker Tool")
                .manipulatorId("is_tool")
                .dataClass(IsMineTinkerToolData.class)
                .dataImplementation(IsMineTinkerToolDataImpl.class)
                .immutableClass(IsMineTinkerToolData.Immutable.class)
                .immutableImplementation(IsMineTinkerToolDataImpl.Immutable.class)
                .builder(new IsMineTinkerToolDataImpl.Builder())
                .buildAndRegister(container);

        Sponge.getDataManager().registerContentUpdater(IsMineTinkerToolDataImpl.class, new IsMineTinkerToolDataImpl.BoolEnabled1To2Updater());

        // MineTinker Compatible Armor
        DataRegistration.builder()
                .dataName("Is MineTinker Armor")
                .manipulatorId("is_armor")
                .dataClass(IsMineTinkerArmorData.class)
                .dataImplementation(IsMineTinkerArmorDataImpl.class)
                .immutableClass(IsMineTinkerArmorData.Immutable.class)
                .immutableImplementation(IsMineTinkerArmorDataImpl.Immutable.class)
                .builder(new IsMineTinkerArmorDataImpl.Builder())
                .buildAndRegister(container);

        Sponge.getDataManager().registerContentUpdater(IsMineTinkerArmorDataImpl.class, new IsMineTinkerArmorDataImpl.BoolEnabled1To2Updater());

        // List of Modifiers The Item Has
        DataRegistration.builder()
                .dataName("MineTinker Modifiers")
                .manipulatorId("mt_modifiers")
                .dataClass(MineTinkerItemModsData.class)
                .dataImplementation(MinetinkerItemModsDataImpl.class)
                .immutableClass(MineTinkerItemModsData.Immutable.class)
                .immutableImplementation(MinetinkerItemModsDataImpl.Immutable.class)
                .builder(new MinetinkerItemModsDataImpl.Builder())
                .buildAndRegister(container);

        Sponge.getDataManager().registerContentUpdater(MinetinkerItemModsDataImpl.class, new MinetinkerItemModsDataImpl.Builder.List1To2Updater());
    }

}
