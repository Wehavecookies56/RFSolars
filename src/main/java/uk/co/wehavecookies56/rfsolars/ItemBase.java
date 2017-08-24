package uk.co.wehavecookies56.rfsolars;

import net.minecraft.item.Item;

public class ItemBase extends Item {

    public ItemBase(String name) {
        setCreativeTab(Common.tab);
        setRegistryName(name);
        setUnlocalizedName(getRegistryName().toString());
    }
}
