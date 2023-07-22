package xyz.brassgoggledcoders.skyships.compat.transport;

import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;
import xyz.brassgoggledcoders.skyships.entity.SkyShip;
import xyz.brassgoggledcoders.skyships.item.SkyShipItem;
import xyz.brassgoggledcoders.transport.api.TransportAPI;
import xyz.brassgoggledcoders.transport.api.shellcontent.ShellContent;
import xyz.brassgoggledcoders.transport.api.shellcontent.ShellContentCreatorInfo;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Collection;
import java.util.List;

public class ShellSkyShipItem extends SkyShipItem {
    public ShellSkyShipItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    protected SkyShip getSkyShip(Level level, HitResult hitResult, ItemStack itemStack) {
        CompoundTag shellContentTag = itemStack.getTagElement("ShellContent");
        ShellContent shellContent = TransportAPI.SHELL_CONTENT_CREATOR.get().create(shellContentTag);
        return new ShellSkyShip(level, hitResult.getLocation(), shellContent);
    }

    @Override
    @ParametersAreNonnullByDefault
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        TransportAPI.ITEM_HELPER.get()
                .appendTextForShellItems(pStack, pTooltipComponents::add);
    }

    @Override
    @ParametersAreNonnullByDefault
    public void fillItemCategory(CreativeModeTab pCategory, NonNullList<ItemStack> pItems) {
        if (this.allowedIn(pCategory)) {
            Collection<ShellContentCreatorInfo> creatorInfos = TransportAPI.SHELL_CONTENT_CREATOR.get().getAll();
            if (creatorInfos.isEmpty()) {
                pItems.add(new ItemStack(this));
            } else {
                for (ShellContentCreatorInfo info : creatorInfos) {
                    ItemStack itemStack = new ItemStack(this);
                    info.embedNBT(itemStack);
                    pItems.add(itemStack);
                }
            }
        }
    }
}
