package uk.co.wehavecookies56.rfsolars;

import cofh.core.util.OreDictionaryProxy;
import cofh.core.util.helpers.ItemHelper;
import cofh.core.util.helpers.RecipeHelper;
import cofh.thermalexpansion.init.TEItems;
import cofh.thermalexpansion.util.managers.machine.PulverizerManager;
import cofh.thermalexpansion.util.managers.machine.SmelterManager;
import cofh.thermalfoundation.ThermalFoundation;
import cofh.thermalfoundation.init.TFBlocks;
import cofh.thermalfoundation.init.TFItems;
import cofh.thermalfoundation.init.TFProps;
import cofh.thermalfoundation.util.TFCrafting;
import net.minecraft.block.Block;
import net.minecraft.client.util.RecipeItemHelper;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

@Mod.EventBusSubscriber
public class Common implements IProxy {

    public static Block solar;

    public static final Block[] BLOCKS = {
            solar = new BlockSolar().add()
    };

    public static Item silicon, crystallineSilicon, crystallineSiliconFilm;

    public static final Item[] ITEMS = {
            silicon = new ItemBase("silicon"),
            crystallineSilicon = new ItemBase("crystalline_silicon"),
            crystallineSiliconFilm = new ItemBase("crystalline_silicon_film")
    };

    public static CreativeTabs tab;

    public static class Tab extends CreativeTabs {
        public Tab(String label) {
            super(CreativeTabs.getNextID(), label);
        }

        @Override
        public ItemStack getTabIconItem() {
            return new ItemStack(Common.solar);
        }
    }

    @Override
    public void preInit() {
        tab = new Tab("rfsolars_tab");
        for (Block b : BLOCKS) {
            b.setCreativeTab(tab);
        }
        for (Item i : ITEMS) {
            i.setCreativeTab(tab);
        }
        GameRegistry.registerTileEntity(TileSolar.class, RFSolars.MODID + ":solar_tile");

    }

    @Override
    public void init() {
        OreDictionary.registerOre("itemSilicon", silicon);
        List<ItemStack> siliconOres = OreDictionary.getOres("itemSilicon", false);
        if (!siliconOres.isEmpty() && !PulverizerManager.recipeExists((ItemStack)OreDictionary.getOres("itemSilicon", false).get(0))) {
            SmelterManager.addRecipe(SmelterManager.DEFAULT_ENERGY, ItemHelper.cloneStack((ItemStack)siliconOres.get(0), 1), new ItemStack(Items.QUARTZ), new ItemStack(crystallineSilicon));
        }
        List<ItemStack> sandOres = OreDictionary.getOres("sand", false);
        if (!sandOres.isEmpty() && !PulverizerManager.recipeExists((ItemStack)OreDictionary.getOres("sand", false).get(0))) {
            PulverizerManager.addRecipe(PulverizerManager.DEFAULT_ENERGY, ItemHelper.cloneStack((ItemStack)sandOres.get(0), 1), ItemHelper.cloneStack(new ItemStack(silicon), 1));
        }
        RecipeHelper.addShapedOreRecipe(new ItemStack(crystallineSiliconFilm),
                "GGG",
                "CCC",
                "LPL",
                'G', TFBlocks.blockGlass,
                'C', crystallineSilicon,
                'L', "plateLead",
                'P', Items.PAPER
        );
        RecipeHelper.addShapedOreRecipe(new ItemStack(solar),
                "CCC",
                "LDL",
                "LRL",
                'C', crystallineSiliconFilm,
                'D', Blocks.DAYLIGHT_DETECTOR,
                'L', "plateLead",
                'R', new ItemStack(TFItems.itemMaterial, 1, 515)
        );
    }

    @Override
    public void postInit() {

    }

    @SubscribeEvent
    public static void registerBlock(RegistryEvent.Register<Block> e) {
        e.getRegistry().registerAll(BLOCKS);
    }

    @SubscribeEvent
    public static void registerItem(RegistryEvent.Register<Item> e) {
        for (Block b : BLOCKS) {
            if (b instanceof IUpgradeableBlock) {
                e.getRegistry().register(new ItemBlockSolar(b).setRegistryName(b.getRegistryName()));
            } else {
                e.getRegistry().register(new ItemBlock(b).setRegistryName(b.getRegistryName()));
            }
        }
        for (Item i : ITEMS) {
            e.getRegistry().register(i);
        }
    }

    @SubscribeEvent
    public static void attachTECap(AttachCapabilitiesEvent<TileEntity> e) {
        if (e.getObject() instanceof TileSolar) {

        }
    }
}
