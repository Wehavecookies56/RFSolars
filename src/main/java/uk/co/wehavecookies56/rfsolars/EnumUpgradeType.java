package uk.co.wehavecookies56.rfsolars;

import net.minecraft.util.IStringSerializable;

public enum EnumUpgradeType implements IStringSerializable {
    BASIC, HARDENED, REINFORCED, SIGNALUM, RESONANT;

    String name;

    EnumUpgradeType() {
        this.name = this.toString();
    }

    @Override
    public String getName() {
        return name.toLowerCase();
    }

    public static EnumUpgradeType byMetadata(int meta) {
        return EnumUpgradeType.values()[meta];
    }
}
