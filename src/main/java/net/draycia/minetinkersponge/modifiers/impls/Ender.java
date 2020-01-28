package net.draycia.minetinkersponge.modifiers.impls;

import com.flowpowered.math.imaginary.Quaterniond;
import com.flowpowered.math.vector.Vector3d;
import com.google.common.collect.ImmutableList;
import net.draycia.minetinkersponge.managers.ModManager;
import net.draycia.minetinkersponge.modifiers.Modifier;
import net.draycia.minetinkersponge.utils.ItemTypeUtils;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.trait.BlockTrait;
import org.spongepowered.api.block.trait.BooleanTraits;
import org.spongepowered.api.block.trait.EnumTraits;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.property.block.PassableProperty;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.Living;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.cause.EventContextKeys;
import org.spongepowered.api.event.entity.DamageEntityEvent;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.recipe.crafting.CraftingRecipe;
import org.spongepowered.api.item.recipe.crafting.Ingredient;
import org.spongepowered.api.item.recipe.crafting.ShapedCraftingRecipe;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.TeleportHelper;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.extent.Extent;
import org.spongepowered.api.world.teleport.TeleportHelperFilter;
import org.spongepowered.api.world.teleport.TeleportHelperFilters;

import java.util.List;
import java.util.Optional;

public class Ender extends Modifier {

    private List<ItemType> compatibleTypes = ImmutableList.<ItemType>builder()
            .addAll(ItemTypeUtils.BOWS)
            .build();

    @Override
    public List<ItemType> getCompatibleItems() {
        return compatibleTypes;
    }

    @Override
    public String getName() {
        return getName("Ender");
    }

    @Override
    public int getMaxLevel() {
        return getMaxLevel(1);
    }

    @Override
    public int getLevelWeight() {
        return getLevelWeight(1);
    }

    @Override
    public ItemType getModifierItemType() {
        return getModifierItemType(ItemTypes.CHORUS_FRUIT_POPPED);
    }

    @Override
    public void onModifierRegister(Object plugin) {
        Sponge.getEventManager().registerListeners(plugin, this);
    }

    @Override
    public Optional<CraftingRecipe> getRecipe() {

        ShapedCraftingRecipe recipe = ShapedCraftingRecipe.builder()
                .aisle("IFI", "FCF", "IFI")
                .where('F', Ingredient.of(ItemTypes.CHORUS_FRUIT_POPPED))
                .where('C', Ingredient.of(ItemTypes.ENDER_EYE))
                .where('I', Ingredient.of(ItemTypes.ENDER_PEARL))
                .result(getModifierItem())
                .id(getKey())
                .build();

        return Optional.of(getCraftingRecipe(recipe));
    }

    @Override
    public String getDescription() {
        return getDescription("Teleports you behind entities when you shoot them with arrows.");
    }

    @Listener
    public void onEntityDamagedByEntity(DamageEntityEvent event) {
        if (!(event.getTargetEntity() instanceof Living)) {
            return;
        }

        event.getContext().get(EventContextKeys.OWNER).flatMap(User::getPlayer).ifPresent(player -> {
            if (!player.get(Keys.IS_SNEAKING).orElse(false)) {
                return;
            }

            Optional<ItemStack> itemStack = player.getItemInHand(HandTypes.MAIN_HAND);

            if (itemStack.isPresent() && ModManager.itemHasModifier(itemStack.get(), this)) {
                Entity entity = event.getTargetEntity();
                Location<World> location = entity.getLocation();
                Location<World> newLoc = location.getExtent().getLocation(behind(entity, 2));

                if (TeleportHelperFilters.DEFAULT.isSafeBodyMaterial(newLoc.getBlock())) {
                    player.setLocation(newLoc);
                    player.setRotation(event.getTargetEntity().getRotation());
                } else {
                    player.sendMessage(Text.of(TextColors.RED, "Unable to teleport you! Desired location is unsafe."));
                }

            }
        });
    }

    private Vector3d behind(Entity entity, double distance) {
        final Vector3d rotation = entity.getRotation();
        final Vector3d direction = Quaterniond.fromAxesAnglesDeg(0, -rotation.getY(), 0).getDirection();
        final Vector3d position = entity.getLocation().getPosition();
        return position.add(direction.negate().mul(distance)).add(0.0f, 0.1f, 0.0f);
    }

}
