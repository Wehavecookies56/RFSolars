package uk.co.wehavecookies56.rfsolars;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;


public class BlockBase extends Block {

    public BlockBase(String name) {
        super(Material.IRON);
        this.setRegistryName(name);
        this.setUnlocalizedName(this.getRegistryName().toString());
        this.setCreativeTab(Common.tab);
    }
}
