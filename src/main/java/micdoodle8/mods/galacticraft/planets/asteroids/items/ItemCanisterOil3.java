package micdoodle8.mods.galacticraft.planets.asteroids.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.planets.asteroids.AsteroidsModule;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import java.util.HashMap;
import java.util.List;

public class ItemCanisterOil3 extends ItemCanisterTierEmpty {
    private static HashMap<ItemStack, Integer> craftingvalues = new HashMap();
    protected IIcon[] icons = new IIcon[7];
    private int mPreCapacity;

    public ItemCanisterOil3(String assetName) {
        super(assetName, 32000);
        this.mPreCapacity = 32000;
        this.setAllowedFluid(ConfigManagerCore.useOldFuelFluidID ? "fuelgc" : "fuel");
        this.setTextureName(AsteroidsModule.TEXTURE_PREFIX + "canisterLOX/" + assetName);
        this.setContainerItem(AsteroidsItems.canisterLOX3);
    }

    public static void saveDamage(ItemStack itemstack, int damage) {
        ItemCanisterOil3.craftingvalues.put(itemstack, Integer.valueOf(damage));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        for (int i = 0; i < this.icons.length; i++) {
            this.icons[i] = iconRegister.registerIcon(this.getIconString() + "_" + i);
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack itemStack) {
        if (this.mPreCapacity + 1 - itemStack.getItemDamage() == 0) {
            return "item.emptyCanisterT3";
        }

        if (itemStack.getItemDamage() == 1) {
            return "item.canister.fuel3.full";
        }

        return "item.canister.fuel3.partial";
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
        par3List.add(new ItemStack(par1, 1, 1));
    }

    @Override
    public IIcon getIconFromDamage(int par1) {
        final int damage = 6 * par1 / this.mPreCapacity;

        if (this.icons.length > damage) {
            return this.icons[this.icons.length - damage - 1];
        }

        return super.getIconFromDamage(damage);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
        if (this.mPreCapacity + 1 - par1ItemStack.getItemDamage() > 0) {
            par3List.add(GCCoreUtil.translate("gui.message.fuel.name") + ": " + (this.mPreCapacity + 1 - par1ItemStack.getItemDamage()));
        }
    }

    @Override
    public ItemStack getContainerItem(ItemStack itemstack) {
        Integer saved = ItemCanisterOil3.craftingvalues.get(itemstack);
        if (saved != null) {
            if (saved < this.mPreCapacity + 1) {
                ItemCanisterOil3.craftingvalues.remove(itemstack);
                itemstack.setItemDamage(saved);
                return itemstack;
            }
            return new ItemStack(this.getContainerItem(), 1, this.mPreCapacity + 1);
        }
        return super.getContainerItem(itemstack);
    }

    @Override
    public void onUpdate(ItemStack par1ItemStack, World par2World, Entity par3Entity, int par4, boolean par5) {
        if (this.mPreCapacity + 1 == par1ItemStack.getItemDamage()) {
            if (par1ItemStack.getItem() != AsteroidsItems.canisterLOX3) {
                this.replaceEmptyCanisterItem(par1ItemStack, AsteroidsItems.canisterLOX3);
                par1ItemStack.setItemDamage(32001);
            }
            par1ItemStack.stackTagCompound = null;
        } else if (par1ItemStack.getItemDamage() <= 0) par1ItemStack.setItemDamage(1);
    }

    public String getAllowedFluid() {
        return this.allowedFluid;
    }

    public void setAllowedFluid(String name) {
        this.allowedFluid = new String(name);
    }

    @Override
    public int fill(ItemStack container, FluidStack resource, boolean doFill) {
        if (resource == null || resource.getFluid() == null || resource.amount == 0 || container == null || container.getItemDamage() <= 1 || !(container.getItem() instanceof ItemCanisterTierEmpty)) {
            return 0;
        }

        String fluidName = resource.getFluid().getName();
        if (container.getItemDamage() == this.mPreCapacity + 1) {
            //Empty canister - find a new canister to match the fluid
            for (String key : GalacticraftCore.itemList.keySet()) {
                if (key.contains("CanisterFull")) {
                    Item i = GalacticraftCore.itemList.get(key).getItem();
                    if (i instanceof ItemCanisterTierEmpty && fluidName.equalsIgnoreCase(((ItemCanisterTierEmpty) i).allowedFluid)) {
                        if (!doFill) return Math.min(resource.amount, this.capacity);

                        this.replaceEmptyCanisterItem(container, i);
                        break;
                    }
                }
            }
            //Delete any Forge fluid contents
            container.stackTagCompound = null;
        } else {
            //Refresh the Forge fluid contents
            container.stackTagCompound = null;
            super.fill(container, this.getFluid(container), true);
        }

        if (fluidName.equalsIgnoreCase(((ItemCanisterTierEmpty) container.getItem()).allowedFluid)) {
            int added = super.fill(container, resource, doFill);
            if (doFill && added > 0) container.setItemDamage(Math.max(1, container.getItemDamage() - added));
            return added;
        }

        return 0;
    }

    @Override
    public FluidStack drain(ItemStack container, int maxDrain, boolean doDrain) {
        if (this.allowedFluid == null || container.getItemDamage() >= this.mPreCapacity + 1)
            return null;

        //Refresh the Forge fluid contents
        container.stackTagCompound = null;
        super.fill(container, this.getFluid(container), true);

        FluidStack used = super.drain(container, maxDrain, doDrain);
        if (doDrain && used != null && used.amount > 0) {
            this.setNewDamage(container, container.getItemDamage() + used.amount);
        }
        return used;
    }

    protected void setNewDamage(ItemStack container, int newDamage) {
        if (newDamage < this.mPreCapacity + 1) {
            newDamage = Math.min(newDamage, this.mPreCapacity + 1);
        } else {
            newDamage = 32001;
            container.stackTagCompound = null;
            if (container.getItem() != AsteroidsItems.canisterLOX3) {
                this.replaceEmptyCanisterItem(container, AsteroidsItems.canisterLOX3);
                return;
            }
        }
        container.setItemDamage(newDamage);
    }

    private void replaceEmptyCanisterItem(ItemStack container, Item newItem) {
        //This is a neat trick to change the item ID in an ItemStack
        final int stackSize = container.stackSize;
        NBTTagCompound tag = new NBTTagCompound();
        tag.setShort("id", (short) Item.getIdFromItem(newItem));
        tag.setByte("Count", (byte) stackSize);
        tag.setShort("Damage", (short) (this.mPreCapacity + 1));
        container.readFromNBT(tag);
    }

    @Override
    public FluidStack getFluid(ItemStack container) {
        String fluidName = ((ItemCanisterTierEmpty) container.getItem()).allowedFluid;
        if (fluidName == null || this.mPreCapacity + 1 == container.getItemDamage())
            return null;

        Fluid fluid = FluidRegistry.getFluid(fluidName);
        if (fluid == null) return null;

        return new FluidStack(fluid, this.mPreCapacity + 1 - container.getItemDamage());
    }
}
