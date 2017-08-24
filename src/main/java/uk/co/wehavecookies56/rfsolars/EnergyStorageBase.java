package uk.co.wehavecookies56.rfsolars;

import cofh.redstoneflux.impl.EnergyStorage;
import net.minecraft.nbt.NBTTagCompound;

public class EnergyStorageBase extends EnergyStorage {

    public EnergyStorageBase(int capacity) {
        super(capacity);
    }

    public EnergyStorageBase(int capacity, int maxTransfer) {
        super(capacity, maxTransfer);
    }

    public EnergyStorageBase(int capacity, int maxReceive, int maxExtract) {
        super(capacity, maxReceive, maxExtract);
    }

    @Override
    public EnergyStorage readFromNBT(NBTTagCompound nbt) {
        return super.readFromNBT(nbt);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        return super.writeToNBT(nbt);
    }

    @Override
    public EnergyStorage setCapacity(int capacity) {
        return super.setCapacity(capacity);
    }

    @Override
    public EnergyStorage setMaxTransfer(int maxTransfer) {
        return super.setMaxTransfer(maxTransfer);
    }

    @Override
    public EnergyStorage setMaxReceive(int maxReceive) {
        return super.setMaxReceive(maxReceive);
    }

    @Override
    public EnergyStorage setMaxExtract(int maxExtract) {
        return super.setMaxExtract(maxExtract);
    }

    @Override
    public int getMaxReceive() {
        return super.getMaxReceive();
    }

    @Override
    public int getMaxExtract() {
        return super.getMaxExtract();
    }

    @Override
    public void setEnergyStored(int energy) {
        super.setEnergyStored(energy);
    }

    @Override
    public void modifyEnergyStored(int energy) {
        super.modifyEnergyStored(energy);
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        return super.receiveEnergy(maxReceive, simulate);
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        return super.extractEnergy(maxExtract, simulate);
    }

    @Override
    public int getEnergyStored() {
        return super.getEnergyStored();
    }

    @Override
    public int getMaxEnergyStored() {
        return super.getMaxEnergyStored();
    }
}
