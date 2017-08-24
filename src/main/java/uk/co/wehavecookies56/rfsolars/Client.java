package uk.co.wehavecookies56.rfsolars;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod.EventBusSubscriber
public class Client extends Common implements IProxy {

    @Override
    public void preInit() {
        super.preInit();
    }

    @Override
    public void init() {
        super.init();
    }

    @Override
    public void postInit() {
        super.postInit();
    }

    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent e) {
        OBJLoader.INSTANCE.addDomain(RFSolars.MODID);
        for (Block b : Common.BLOCKS) {
            if (b instanceof BlockSolar) {
                for (EnumUpgradeType t : EnumUpgradeType.values()) {
                    registerModel(b, t.ordinal(), "upgrade", t.getName());
                }
            }
            registerModel(b);
        }
        for (Item i : Common.ITEMS) {
            registerModel(i);
        }
    }

    public static void registerModel(Item i) {
        ModelLoader.setCustomModelResourceLocation(i, 0, new ModelResourceLocation(RFSolars.MODID + ":" + i.getRegistryName().getResourcePath(), "inventory"));
    }

    public static void registerModel(Block b) {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(b), 0, new ModelResourceLocation(RFSolars.MODID + ":" + b.getRegistryName().getResourcePath(), "inventory"));
    }

    public static void registerModel(Block b, int meta, String variant, String value) {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(b), meta, new ModelResourceLocation(RFSolars.MODID + ":" + b.getRegistryName().getResourcePath(), variant + "=" + value));
    }

}
