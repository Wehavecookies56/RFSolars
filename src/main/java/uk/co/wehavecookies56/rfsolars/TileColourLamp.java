package uk.co.wehavecookies56.rfsolars;

import elucent.albedo.lighting.ILightProvider;
import elucent.albedo.lighting.Light;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.Optional;

@Optional.Interface(modid = "albedo", iface = "elucent.albedo.lighting.ILightProvider")
public class TileColourLamp extends TileEntity implements ILightProvider {

    @Override
    public Light provideLight() {
        return new Light(pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F, 160, 87, 19, 0.05F, 10);
    }
}
