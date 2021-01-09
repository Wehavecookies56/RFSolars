package uk.co.wehavecookies56.rfsolars;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockColourLamp extends BlockBase implements ITileEntityProvider {

    public BlockColourLamp() {
        super("colour_lamp");
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileColourLamp();
    }
}
