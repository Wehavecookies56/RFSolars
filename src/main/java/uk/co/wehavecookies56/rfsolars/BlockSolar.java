package uk.co.wehavecookies56.rfsolars;

import cofh.api.block.IBlockInfo;
import cofh.api.tileentity.IUpgradeable;
import cofh.core.util.helpers.StringHelper;
import cofh.core.util.helpers.WrenchHelper;
import cofh.thermaldynamics.duct.tiles.TileDuctFluid;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class BlockSolar extends BlockBase implements ITileEntityProvider, IUpgradeableBlock, IBlockInfo {

    private AxisAlignedBB BOUNDS = new AxisAlignedBB(0, 0, 0, 1, 0.37D, 1);

    public static final IProperty<EnumUpgradeType> UPGRADE = PropertyEnum.create("upgrade", EnumUpgradeType.class);

    public BlockSolar() {
        super("solar_panel");
        setHardness(3F);
        setResistance(1F);
        setHarvestLevel("pickaxe", 1);
        this.setDefaultState(getDefaultState().withProperty(UPGRADE, EnumUpgradeType.BASIC));
        this.setCreativeTab(Common.tab);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!worldIn.isRemote && worldIn.getTileEntity(pos) != null) {
            TileSolar solar = (TileSolar) worldIn.getTileEntity(pos);
            solar.markDirty();
            if (playerIn.isSneaking()) {
                if (WrenchHelper.isHoldingUsableWrench(playerIn, new RayTraceResult(new Vec3d(hitX, hitY, hitZ), facing, pos))) {
                    worldIn.setBlockState(pos, Blocks.AIR.getDefaultState());
                    worldIn.setTileEntity(pos, null);
                    EntityItem drop = new EntityItem(worldIn,(double) pos.getX() + hitX,(double) pos.getY() + hitY, (double) pos.getZ() + hitZ);
                    drop.setItem(new ItemStack(Common.solar, 1, state.getValue(UPGRADE).ordinal()));
                    worldIn.spawnEntity(drop);
                    return true;
                }
            }
        }
        if (facing == EnumFacing.UP) {
            return true;
        }
        return false;
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        return new ItemStack(this, 1, getMetaFromState(state));
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Item.getItemFromBlock(Common.solar);
    }

    @Override
    public int damageDropped(IBlockState state) {
        return  state.getValue(UPGRADE).ordinal();
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(UPGRADE, EnumUpgradeType.byMetadata(meta));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(UPGRADE).ordinal();
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        return state;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] { UPGRADE });
    }

    @Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
        return super.canPlaceBlockAt(worldIn, pos);
    }

    @Override
    public boolean canPlaceTorchOnTop(IBlockState state, IBlockAccess world, BlockPos pos) {
        return false;
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileSolar(meta);
    }

    @Override
    public boolean isFullBlock(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }


    @Nullable
    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
        return BOUNDS;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return BOUNDS;
    }

    @Override
    public void getBlockInfo(List<ITextComponent> list, IBlockAccess iBlockAccess, BlockPos blockPos, EnumFacing enumFacing, EntityPlayer entityPlayer, boolean b) {
        list.add(new TextComponentTranslation("info.rfsolars.solar_panel.blockinfo.0", ((TileSolar)iBlockAccess.getTileEntity(blockPos)).amountToGenerate()));
        list.add(new TextComponentTranslation("info.rfsolars.solar_panel.blockinfo.1", ((TileSolar)iBlockAccess.getTileEntity(blockPos)).calcEfficiency()));
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced) {
        if (StringHelper.displayShiftForDetail && !StringHelper.isShiftKeyDown()) {
            tooltip.add(StringHelper.shiftForDetails());
        }
        if (!StringHelper.isShiftKeyDown()) {
            return;
        }
        tooltip.add(StringHelper.getInfoText("info.rfsolars.solar_panel.0"));
        tooltip.add(StringHelper.getNoticeText("info.rfsolars.solar_panel.1"));
    }
}
