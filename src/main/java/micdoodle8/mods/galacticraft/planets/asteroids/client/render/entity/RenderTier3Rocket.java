package micdoodle8.mods.galacticraft.planets.asteroids.client.render.entity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import micdoodle8.mods.galacticraft.api.prefab.entity.EntitySpaceshipBase;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModelCustom;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderTier3Rocket extends Render {
    protected IModelCustom rocketModelObj;
    private ResourceLocation rocketTexture;

    public RenderTier3Rocket(IModelCustom spaceshipModel, String textureDomain, String texture) {
        this.rocketModelObj = spaceshipModel;
        this.rocketTexture = new ResourceLocation(textureDomain, "textures/model/" + texture + ".png");
        this.shadowSize = 2.0F;
    }



    protected ResourceLocation getEntityTexture(Entity par1Entity) {
        return this.rocketTexture;
    }

    public void renderSpaceship(EntitySpaceshipBase entity, double par2, double par4, double par6, float par8, float par9) {
        GL11.glDisable(32826);
        GL11.glPushMatrix();
        float var24 = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * par9 + 180.0F;
        float var25 = entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * par9 + 45.0F;
        GL11.glTranslatef((float) par2, (float) par4 - 1.7F, (float) par6);
        GL11.glRotatef(180.0F - par8, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(-var24, 0.0F, 0.0F, 1.0F);
        float var26 = entity.rollAmplitude / 3.0F - par9;
        float var27 = entity.shipDamage - par9;
        if (var27 < 0.0F) {
            var27 = 0.0F;
        }
        if (var26 > 0.0F) {
            float i = entity.getLaunched() ? ((5 - MathHelper.floor_double((entity.timeUntilLaunch / 85D))) / 10.0F) : 0.3F;
            GL11.glRotatef(MathHelper.sin(var26) * var26 * i * par9, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(MathHelper.sin(var26) * var26 * i * par9, 1.0F, 0.0F, 1.0F);
        }
        bindTexture(this.rocketTexture);
        GL11.glScalef(-1.0F, -1.0F, 1.0F);
        GL11.glScalef(0.9F, 0.9F, 0.9F);
        this.rocketModelObj.renderAll();
        GL11.glColor3f(1.0F, 1.0F, 1.0F);
        GL11.glPopMatrix();
    }


    public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9) {
        renderSpaceship((EntitySpaceshipBase) par1Entity, par2, par4, par6, par8, par9);
    }
}

