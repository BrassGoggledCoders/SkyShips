package xyz.brassgoggledcoders.skyships.item;

import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.skyships.entity.SkyShip;

import javax.annotation.ParametersAreNonnullByDefault;

public class SkyShipItem extends Item {
    public SkyShipItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    @NotNull
    @ParametersAreNonnullByDefault
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand) {
        ItemStack itemStack = pPlayer.getItemInHand(pHand);
        HitResult hitResult = getPlayerPOVHitResult(pLevel, pPlayer, ClipContext.Fluid.NONE);
        if (hitResult.getType() == HitResult.Type.MISS) {
            return InteractionResultHolder.pass(itemStack);
        } else if (hitResult.getType() == HitResult.Type.BLOCK) {
            SkyShip skyShip = this.getSkyShip(pLevel, hitResult, itemStack);
            skyShip.setYRot(pPlayer.getYRot());
            if (!pLevel.noCollision(skyShip, skyShip.getBoundingBox())) {
                return InteractionResultHolder.fail(itemStack);
            } else {
                if (!pLevel.isClientSide) {
                    pLevel.addFreshEntity(skyShip);
                    pLevel.gameEvent(pPlayer, GameEvent.ENTITY_PLACE, hitResult.getLocation());
                    if (!pPlayer.getAbilities().instabuild) {
                        itemStack.shrink(1);
                    }
                }

                pPlayer.awardStat(Stats.ITEM_USED.get(this));
                return InteractionResultHolder.sidedSuccess(itemStack, pLevel.isClientSide());
            }
        } else {
            return InteractionResultHolder.pass(itemStack);
        }
    }

    protected SkyShip getSkyShip(Level level, HitResult hitResult, ItemStack itemStack) {
        return new SkyShip(level, hitResult.getLocation());
    }
}
