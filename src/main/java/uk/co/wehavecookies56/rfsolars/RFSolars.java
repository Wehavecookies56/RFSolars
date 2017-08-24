package uk.co.wehavecookies56.rfsolars;

import net.minecraft.init.Blocks;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = RFSolars.MODID, name = RFSolars.NAME, version = RFSolars.VERSION, dependencies = "required-after:thermalexpansion")
public class RFSolars {
    public static final String MODID = "rfsolars";
    public static final String NAME = "RF Solars";
    public static final String VERSION = "1.0";

    @SidedProxy(clientSide = "uk.co.wehavecookies56.rfsolars.Client", serverSide = "uk.co.wehavecookies56.rfsolars.Common")
    public static IProxy proxy;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        proxy.preInit();
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init();
    }
}
