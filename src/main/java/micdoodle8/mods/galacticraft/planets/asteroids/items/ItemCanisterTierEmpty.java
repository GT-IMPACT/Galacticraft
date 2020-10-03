package micdoodle8.mods.galacticraft.planets.asteroids.items;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.ItemFluidContainer;

import java.util.List;

public abstract class ItemCanisterTierEmpty extends ItemFluidContainer {
    public final static int EMPTY = FluidContainerRegistry.BUCKET_VOLUME + 1;
    public String allowedFluid = null;
    public int mCapacityMeta;

    public ItemCanisterTierEmpty(String assetName, int capacity) {
        super(0, capacity);
        this.setMaxDamage(capacity + 1);
        this.mCapacityMeta = capacity + 1;
        this.setMaxStackSize(1);
        this.setNoRepair();
        this.setUnlocalizedName(assetName);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public EnumRarity getRarity(ItemStack par1ItemStack) {
        return ClientProxyCore.galacticraftItem;
    }

    @Override
    public CreativeTabs getCreativeTab() {
        return GalacticraftCore.galacticraftItemsTab;
    }

    @Override
    public ItemStack getContainerItem(ItemStack itemStack) {
        return new ItemStack(this.getContainerItem(), 1, this.mCapacityMeta);
    }

}