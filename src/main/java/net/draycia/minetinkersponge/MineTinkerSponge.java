package net.draycia.minetinkersponge;

import com.google.inject.Inject;
import net.draycia.minetinkersponge.data.DataRegistrar;
import net.draycia.minetinkersponge.data.MTKeys;
import net.draycia.minetinkersponge.data.interfaces.*;
import net.draycia.minetinkersponge.modifiers.ModManager;
import net.draycia.minetinkersponge.modifiers.impls.Directing;
import net.draycia.minetinkersponge.utils.ItemTypeUtils;
import org.slf4j.Logger;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.Item;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.cause.EventContext;
import org.spongepowered.api.event.cause.EventContextKey;
import org.spongepowered.api.event.cause.EventContextKeys;
import org.spongepowered.api.event.cause.entity.damage.source.EntityDamageSource;
import org.spongepowered.api.event.entity.DestructEntityEvent;
import org.spongepowered.api.event.filter.cause.Root;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.item.inventory.DropItemEvent;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.text.Text;

import java.util.*;

@Plugin(
        id = "minetinker-sponge",
        name = "MineTinker-Sponge"
)
public class MineTinkerSponge {

    @Inject
    PluginContainer container;

    @Inject
    private Logger logger;

    private DataRegistrar dataRegistrar;
    private ModManager modManager;

    @Listener
    public void onPreInit(GamePreInitializationEvent event) {
        dataRegistrar = new DataRegistrar(container);
        modManager = new ModManager();

        modManager.registerModifier(this, new Directing());
    }

    @Listener
    public void onPlayerJoin(ClientConnectionEvent.Join event, @Root Player player) {
        ItemStack itemStack = ItemStack.of(ItemTypes.DIAMOND_PICKAXE);

        itemStack.offer(itemStack.getOrCreate(IsMineTinkerData.class).get());
        itemStack.offer(itemStack.getOrCreate(IsMineTinkerToolData.class).get());
        itemStack.offer(itemStack.getOrCreate(IsMineTinkerArmorData.class).get());
        itemStack.offer(itemStack.getOrCreate(MineTinkerItemModsData.class).get());

        itemStack.offer(MTKeys.IS_MINETINKER, true);
        itemStack.offer(MTKeys.IS_MT_TOOL, true);

        HashMap<String, Integer> modifiers = new HashMap<>();
        modifiers.put("Directing", 1);

        itemStack.offer(MTKeys.ITEM_MODIFIERS, modifiers);

        itemStack.offer(Keys.DISPLAY_NAME, Text.of("Map Test"));

        player.getInventory().offer(itemStack);
    }
}
