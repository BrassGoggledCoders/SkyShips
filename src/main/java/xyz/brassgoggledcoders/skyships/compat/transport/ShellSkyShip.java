package xyz.brassgoggledcoders.skyships.compat.transport;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.HasCustomInventoryScreen;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.brassgoggledcoders.skyships.entity.SkyShip;
import xyz.brassgoggledcoders.transport.api.shell.IShell;
import xyz.brassgoggledcoders.transport.api.shellcontent.ShellContent;
import xyz.brassgoggledcoders.transport.api.shellcontent.ShellContentCreatorInfo;
import xyz.brassgoggledcoders.transport.api.shellcontent.holder.IShellContentHolder;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

public class ShellSkyShip extends SkyShip implements IShell {
    private final IShellContentHolder holder;

    public ShellSkyShip(EntityType<?> type, Level level) {
        super(type, level);
        this.holder = IShellContentHolder.createForSide(this);
    }

    public ShellSkyShip(Level level, Vec3 location, ShellContent shellContent) {
        super(SkyShipsTransport.SHELL_SKY_SHIP.get(), level, location);
        this.holder = IShellContentHolder.createForSide(this);
        this.holder.update(shellContent);
    }

    @Override
    protected boolean canAddPassenger(@Nonnull Entity pPassenger) {
        return this.getPassengers().isEmpty();
    }

    @Override
    public IShellContentHolder getHolder() {
        return this.holder;
    }

    @Override
    public Entity getSelf() {
        return this;
    }

    @Override
    public ItemStack asItemStack() {
        return SkyShipsTransport.SHELL_SKY_SHIP_ITEM.asStack();
    }

    @Override
    protected void readAdditionalSaveData(@NotNull CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        this.getHolder().deserializeNBT(pCompound.getCompound(ShellContentCreatorInfo.NBT_TAG_ELEMENT));
    }

    @Override
    protected void addAdditionalSaveData(@NotNull CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.put(ShellContentCreatorInfo.NBT_TAG_ELEMENT, this.getHolder().serializeNBT());
    }

    @Override
    public void destroy(@NotNull DamageSource damageSource) {
        this.kill();
        this.getContent().destroy(damageSource);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        this.getContent().invalidateCaps();
    }

    @Override
    public void reviveCaps() {
        super.reviveCaps();
        this.getContent().reviveCaps();
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        LazyOptional<T> shellCap = this.getContent().getCapability(cap, side);
        if (shellCap.isPresent()) {
            return shellCap;
        }

        return super.getCapability(cap, side);
    }

    @Override
    @Nonnull
    @ParametersAreNonnullByDefault
    public InteractionResult interact(Player player, InteractionHand hand) {
        InteractionResult result = super.interact(player, hand);
        if (result.consumesAction()) {
            return result;
        }

        result = this.getContent().interact(player, hand);
        if (result.consumesAction()) {
            return result;
        }

        return InteractionResult.PASS;
    }

    @Override
    @Nonnull
    public Component getName() {
        if (this.hasCustomName()) {
            return super.getName();
        } else {
            return this.getHolder().getName();
        }
    }
}
