package xyz.brassgoggledcoders.skyships.navigation;

import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.skyships.content.SkyShipsPOITypes;
import xyz.brassgoggledcoders.skyships.entity.SkyShip;
import xyz.brassgoggledcoders.skyships.util.functional.Result;

import java.util.Optional;

public class Navigator {
    private final SkyShip skyShip;
    private final IItemHandler navigationInventory;
    private int currentSlot;
    @NotNull
    private Result<GlobalPos> navigationItemResult;
    private Result<BlockPos> aeroporteResult;

    public Navigator(SkyShip skyShip, IItemHandler navigationInventory) {
        this.skyShip = skyShip;
        this.navigationInventory = navigationInventory;
        this.navigationItemResult = Result.waiting();
        this.aeroporteResult = Result.waiting();
    }

    public void navigate() {
        this.navigationItemResult = navigationItemResult.run(this::findNavigationItem);

        if (this.navigationItemResult.isSuccess() && !this.skyShip.getLevel().isClientSide()) {
            GlobalPos navigationItemPos = this.navigationItemResult.value();

            if (navigationItemPos.dimension() == this.skyShip.getLevel().dimension()) {
                this.aeroporteResult = this.aeroporteResult.run(this::findAeroporte);

                BlockPos destination = this.aeroporteResult.fold(
                        aeroportePos -> aeroportePos,
                        navigationItemPos::pos
                );

                int vertical = 0;
                BlockPos entityPos = this.skyShip.getOnPos();
                int groundHeight = this.skyShip.getLevel().getHeight(Heightmap.Types.WORLD_SURFACE_WG, entityPos.getX(), entityPos.getZ());
                if (this.skyShip.getEyePosition().y() < groundHeight + 10) {
                    vertical = 1;
                }

                int yawDirection = 0;

                float delta = getDelta(destination);
                if (delta < -5) {
                    yawDirection = -1;
                } else if (delta > 5) {
                    yawDirection = 1;
                }

                this.skyShip.navigateBoat(yawDirection, yawDirection == 0 ? 1 : 0, vertical);
            }
        }
    }

    private float getDelta(BlockPos destination) {
        BlockPos entityPos = this.skyShip.getOnPos();
        int xOffset = entityPos.getX() - destination.getX();
        int zOffset = entityPos.getZ() - destination.getZ();
        float expectedYaw = (float) (-Math.toDegrees(Math.atan2(xOffset, zOffset)) + 180F);

        float delta = expectedYaw - this.skyShip.getYRot();

        while (delta < -180 || delta > 180) {
            if (delta < -180) {
                delta += 360;
            } else {
                delta -= 360;
            }
        }

        return delta;
    }

    public void resetChecks() {
        this.navigationItemResult = Result.waiting();
        this.aeroporteResult = Result.waiting();
    }

    private Optional<GlobalPos> findNavigationItem() {
        int maxAttempts = this.navigationInventory.getSlots();

        Optional<GlobalPos> globalPos = Optional.empty();
        int attempts = 0;
        while (globalPos.isEmpty() && attempts < maxAttempts) {
            int slot = (currentSlot + attempts) % maxAttempts;
            ItemStack itemStack = this.navigationInventory.getStackInSlot(slot);

            if (!itemStack.isEmpty()) {
                globalPos = NavigationProviderRegistry.getInstance()
                        .getPositionFor(itemStack, this.skyShip.getLevel());
            }

            if (globalPos.isPresent()) {
                this.currentSlot = slot;
            }
            attempts++;
        }
        return globalPos;
    }

    private Optional<BlockPos> findAeroporte() {
        if (this.navigationItemResult.isSuccess()) {
            GlobalPos destination = this.navigationItemResult.value();
            if (destination.dimension() == this.skyShip.getLevel().dimension()) {
                if (this.skyShip.getLevel() instanceof ServerLevel serverLevel) {
                    return serverLevel.getPoiManager()
                            .findClosest(
                                    holder -> holder.is(SkyShipsPOITypes.AEROPORTE.getKey()),
                                    destination.pos(),
                                    32,
                                    PoiManager.Occupancy.HAS_SPACE
                            );
                }
            }
        }

        return Optional.empty();
    }

}
