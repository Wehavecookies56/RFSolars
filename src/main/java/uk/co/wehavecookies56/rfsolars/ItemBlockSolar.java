package uk.co.wehavecookies56.rfsolars;

import net.minecraft.block.Block;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockSolar extends ItemBlock {

    public ItemBlockSolar(Block block) {
        super(block);
        setHasSubtypes(true);
    }

    @Override
    public int getMetadata(int damage) {
        return damage;
    }

    @Override
    public EnumRarity getRarity(ItemStack stack) {
        switch (EnumUpgradeType.byMetadata(stack.getMetadata())) {
            case BASIC:
            case HARDENED:
                return EnumRarity.COMMON;
            case REINFORCED:
            case SIGNALUM:
                return EnumRarity.UNCOMMON;
            case RESONANT:
                return EnumRarity.RARE;
        }
        return EnumRarity.COMMON;
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        EnumUpgradeType upgrade = EnumUpgradeType.byMetadata(stack.getMetadata());
        return super.getUnlocalizedName(stack) + "." + upgrade.getName();
    }
}
