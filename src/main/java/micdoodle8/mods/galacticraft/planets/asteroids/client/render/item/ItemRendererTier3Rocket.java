package micdoodle8.mods.galacticraft.planets.asteroids.client.render.item;

import cpw.mods.fml.client.FMLClientHandler;
import micdoodle8.mods.galacticraft.api.entity.IRocketType;
import micdoodle8.mods.galacticraft.api.entity.IRocketType.EnumRocketType;
import micdoodle8.mods.galacticraft.core.entities.EntityTier1Rocket;
import micdoodle8.mods.galacticraft.planets.asteroids.AsteroidsModule;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.model.ModelChest;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.model.IModelCustom;
import org.lwjgl.Sys;
import org.lwjgl.opengl.GL11;

public class ItemRendererTier3Rocket implements IItemRenderer
{
    protected static final ResourceLocation chestTexture = new ResourceLocation("textures/entity/chest/normal.png");
    protected IModelCustom modelSpaceship;
    protected final ModelChest chestModel = new ModelChest();
    protected static RenderItem drawItems = new RenderItem();
    protected ResourceLocation texture = new ResourceLocation(AsteroidsModule.ASSET_PREFIX, "textures/model/tier3rocket.png");

    public ItemRendererTier3Rocket(IModelCustom model) {
        this.modelSpaceship = model;
    }

    protected void renderSpaceship(ItemRenderType type, RenderBlocks render, ItemStack item, float translateX, float translateY, float translateZ) {
        GL11.glPushMatrix();
        transform(item, type);
        (FMLClientHandler.instance().getClient()).renderEngine.bindTexture(this.texture);
        this.modelSpaceship.renderAll();
        GL11.glPopMatrix();
        if (type == ItemRenderType.INVENTORY) {
            int index = Math.min(Math.max(item.getItemDamage(), 0), (IRocketType.EnumRocketType.values()).length - 1);
            if (IRocketType.EnumRocketType.values()[index].getInventorySpace() > 3) {
                ModelChest modelChest = this.chestModel;
                (FMLClientHandler.instance().getClient()).renderEngine.bindTexture(chestTexture);
                GL11.glPushMatrix();
                GL11.glDisable(2929);
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                GL11.glScalef(0.5F, -0.5F, -0.5F);
                GL11.glTranslatef(1.5F, 1.95F, 1.7F);
                short short1 = 0;
                GL11.glRotatef(0.0F, 0.0F, 1.0F, 0.0F);
                GL11.glTranslatef(-1.5F, -1.5F, -1.5F);
                float f1 = 0.0F;
                f1 = 1.0F - f1;
                f1 = 1.0F - f1 * f1 * f1;
                modelChest.chestLid.rotateAngleX = -(f1 * 1.5707964F);
                modelChest.chestBelow.render(0.0625F);
                modelChest.chestLid.render(0.0625F);
                modelChest.chestKnob.render(0.0625F);
                GL11.glEnable(2929);
                GL11.glPopMatrix();
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            }
        }
    }

    public void transform(ItemStack itemstack, ItemRenderType type) {
        EntityClientPlayerMP entityClientPlayerMP = (FMLClientHandler.instance().getClient()).thePlayer;
        if (type == ItemRenderType.EQUIPPED) {
            GL11.glRotatef(70.0F, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(-10.0F, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(50.0F, 0.0F, 1.0F, 1.0F);
            GL11.glTranslatef(-0.8F, -8.2F, 0.0F);
            GL11.glScalef(5.2F, 5.2F, 5.2F);
            if (entityClientPlayerMP != null && entityClientPlayerMP.ridingEntity instanceof EntityTier1Rocket) {
                GL11.glScalef(0.0F, 0.0F, 0.0F);
            }
        }
        if (type == ItemRenderType.EQUIPPED_FIRST_PERSON) {
            GL11.glTranslatef(8.5F, 5.9F, 1.0F);
            GL11.glRotatef(28.0F, 0.0F, 0.0F, 1.0F);
            GL11.glRotatef(230.0F, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(73.0F, 1.0F, 0.0F, 0.0F);
            GL11.glScalef(5.2F, 5.2F, 5.2F);
            if (entityClientPlayerMP != null && entityClientPlayerMP.ridingEntity instanceof EntityTier1Rocket) {
                GL11.glScalef(0.0F, 0.0F, 0.0F);
            }
        }
        GL11.glTranslatef(0.0F, 0.1F, 0.0F);
        GL11.glScalef(-0.4F, -0.4F, 0.4F);
        if (type == ItemRenderType.INVENTORY || type == ItemRenderType.ENTITY) {
            if (type == ItemRenderType.INVENTORY) {
                GL11.glRotatef(85.0F, 1.0F, 0.0F, 1.0F);
                GL11.glRotatef(20.0F, 1.0F, 0.0F, 0.0F);
                GL11.glScalef(0.5F, 0.5F, 0.5F);
                GL11.glTranslatef(0.8F, 5.4F, -0.4F);
            } else {

                GL11.glTranslatef(0.0F, -0.9F, 0.0F);
                GL11.glScalef(0.5F, 0.5F, 0.5F);
            }
            GL11.glScalef(1.3F, 1.3F, 1.3F);
            GL11.glTranslatef(0.0F, -0.6F, 0.0F);
            GL11.glRotatef((float) Sys.getTime() / 30.0F % 360.0F + 45.0F, 0.0F, 1.0F, 0.0F);
        }
        GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
    }

    /**
     * IItemRenderer implementation *
     */

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        switch (type) {
            case ENTITY:
            case INVENTORY:
            case EQUIPPED_FIRST_PERSON:
            case EQUIPPED:
                return true;
        }

        return false;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper)
    {
        return true;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
        switch (type) {
            case EQUIPPED:
            case EQUIPPED_FIRST_PERSON:
            case INVENTORY:
            case ENTITY:
                renderSpaceship(type, (RenderBlocks) data[0], item, -0.5F, -0.5F, -0.5F);
                break;
        }
    }

}
