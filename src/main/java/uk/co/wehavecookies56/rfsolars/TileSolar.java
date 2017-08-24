package uk.co.wehavecookies56.rfsolars;

import cofh.api.core.IAccelerable;
import cofh.api.item.IUpgradeItem;
import cofh.api.tileentity.IUpgradeable;
import cofh.redstoneflux.api.IEnergyConnection;
import cofh.redstoneflux.api.IEnergyProvider;
import cofh.redstoneflux.api.IEnergyReceiver;
import cofh.redstoneflux.impl.EnergyStorage;
import cofh.thermalexpansion.block.dynamo.TileDynamoBase;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nullable;

public class TileSolar extends TileEntity implements IEnergyConnection, IEnergyProvider, IEnergyStorage, ITickable, IUpgradeable {

    EnergyStorage storage;
    int level = 0;

    public TileSolar() {
        storage = new EnergyStorage(getCapacityByLevel(level), getPowerByLevel(level));
    }

    public TileSolar(int level) {
        this.level = level;
        storage = new EnergyStorage(getCapacityByLevel(level), getPowerByLevel(level));
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        return 0;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        return storage.extractEnergy(maxExtract, simulate);
    }

    @Override
    public int getEnergyStored() {
        return storage.getEnergyStored();
    }

    @Override
    public int getMaxEnergyStored() {
        return storage.getMaxEnergyStored();
    }

    @Override
    public boolean canExtract() {
        return true;
    }

    @Override
    public boolean canReceive() {
        return false;
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        return writeToNBT(new NBTTagCompound());
    }

    @Override
    public void handleUpdateTag(NBTTagCompound tag) {
        readFromNBT(tag);
    }

    @Nullable
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        return super.getUpdatePacket();
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        super.onDataPacket(net, pkt);
    }

    @Override
    public int extractEnergy(EnumFacing from, int maxExtract, boolean simulate) {
        int extract = Math.min(storage.getEnergyStored(), storage.getMaxExtract());
        if  (!simulate) {
            storage.setEnergyStored(storage.getEnergyStored() - extract);
            return extract;
        }
        return 0;
    }

    @Override
    public int getEnergyStored(EnumFacing from) {
        return storage.getEnergyStored();
    }

    @Override
    public int getMaxEnergyStored(EnumFacing from) {
        return storage.getMaxEnergyStored();
    }

    @Override
    public boolean canConnectEnergy(EnumFacing from) {
        return from == EnumFacing.DOWN;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        storage.readFromNBT(compound);
        level = compound.getInteger("level");
        super.readFromNBT(compound);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        storage.writeToNBT(compound);
        compound.setInteger("level", level);
        return super.writeToNBT(compound);
    }

    public void generateEnergy(int generate) {
        if (storage.getEnergyStored() != storage.getMaxEnergyStored())
            storage.setEnergyStored(storage.getEnergyStored()+generate);
    }

    @Override
    public boolean canUpgrade(ItemStack itemStack) {
        return true;
    }

    @Override
    public boolean installUpgrade(ItemStack itemStack) {
        if (itemStack.getItem() instanceof IUpgradeItem) {
            IUpgradeItem upgradeItem = (IUpgradeItem) itemStack.getItem();
            if (level + 1 == upgradeItem.getUpgradeLevel(itemStack)) {
                level = upgradeItem.getUpgradeLevel(itemStack);
                storage.setCapacity(getCapacityByLevel(level));
                IBlockState upgradedState = world.getBlockState(pos).withProperty(BlockSolar.UPGRADE, EnumUpgradeType.byMetadata(level));
                world.setBlockState(pos, upgradedState);
            } else {
                return false;
            }
            return true;
        }
        return false;
    }

    @Override
    public int getLevel() {
        return level;
    }

    public int getPowerByLevel(int level) {
        switch (level) {
            case 0: return 10;
            case 1: return 20;
            case 2: return 40;
            case 3: return 70;
            case 4: return 110;
            default: return 10;
        }
    }

    public int getCapacityByLevel(int level) {
        switch (level) {
            case 0: return 2000;
            case 1: return 4000;
            case 2: return 8000;
            case 3: return 16000;
            case 4: return 32000;
            default: return 2000;
        }
    }

    @Override
    public void update() {
        if (!world.isRemote) {
            generateEnergy(amountToGenerate());
            TileEntity teReceiver = world.getTileEntity(pos.down());
            if (teReceiver != null) {
                if (teReceiver.hasCapability(CapabilityEnergy.ENERGY, EnumFacing.UP)) {
                    IEnergyStorage receiver = teReceiver.getCapability(CapabilityEnergy.ENERGY, EnumFacing.UP);
                    if (receiver != null) {
                        if (receiver.canReceive()) {
                            if (storage.getEnergyStored() >= 1) {
                                int extract = extractEnergy(EnumFacing.DOWN, storage.getMaxExtract(), false);
                                receiver.receiveEnergy(extract, false);
                            }
                        }
                    }
                }
            }
        }
    }

    public float calcEfficiency() {
        long actualTime = world.getWorldTime() % 24000;
        float eff = 0;
        if (actualTime >= 0 && actualTime <= 12000) {
            if (actualTime >= 3000 && actualTime <= 9000) {
                eff = 100;
                if (world.isRainingAt(pos.up())) {
                    eff /= 2;
                }
                return eff;
            } else {
                if (actualTime <= 3000) {
                    eff = (actualTime / 3000F) * 100F;
                }
                if (actualTime >= 9000) {
                    eff = Math.abs(((actualTime-12000F) / 3000F) * 100F);
                }
                if (world.isRainingAt(pos.up())) {
                    eff /= 2;
                }
                return eff;
            }
        } else {
            return eff;
        }
    }

    public int amountToGenerate() {
        if (storage.getEnergyStored() != storage.getMaxEnergyStored()) {
            BlockPos tempPos = this.pos;
            while (tempPos.getY() < 256) {
                tempPos = tempPos.up();
                if (world.getBlockState(tempPos).getBlock() != Blocks.AIR) {
                    return 0;
                }
            }
            return (int) ((getPowerByLevel(level) / 100F) * calcEfficiency());
        } else {
            return 0;
        }
    }

    public boolean hasCapability(Capability<?> capability, EnumFacing from) {
        return super.hasCapability(capability, from) || capability == CapabilityEnergy.ENERGY;
    }

    public <T> T getCapability(Capability<T> capability, final EnumFacing from) {
        return capability == CapabilityEnergy.ENERGY ? CapabilityEnergy.ENERGY.cast(new net.minecraftforge.energy.IEnergyStorage() {
            public int receiveEnergy(int maxReceive, boolean simulate) {
                return 0;
            }

            public int extractEnergy(int maxExtract, boolean simulate) {
                return TileSolar.this.extractEnergy(from, maxExtract, simulate);
            }

            public int getEnergyStored() {
                return TileSolar.this.getEnergyStored(from);
            }

            public int getMaxEnergyStored() {
                return TileSolar.this.getMaxEnergyStored(from);
            }

            public boolean canExtract() {
                return true;
            }

            public boolean canReceive() {
                return false;
            }
        }) : super.getCapability(capability, from);
    }
}
