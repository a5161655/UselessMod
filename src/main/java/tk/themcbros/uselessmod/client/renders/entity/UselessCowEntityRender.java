package tk.themcbros.uselessmod.client.renders.entity;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import tk.themcbros.uselessmod.UselessMod;
import tk.themcbros.uselessmod.client.models.entity.UselessCowEntityModel;
import tk.themcbros.uselessmod.entity.UselessCowEntity;

import javax.annotation.Nonnull;

@OnlyIn(Dist.CLIENT)
public class UselessCowEntityRender extends LivingRenderer<UselessCowEntity, UselessCowEntityModel>{

	public UselessCowEntityRender(EntityRendererManager manager) {
		super(manager, new UselessCowEntityModel(), 0f);
	}
	
	@Nonnull
	@Override
	public ResourceLocation getEntityTexture(@Nonnull UselessCowEntity entity) {
		return new ResourceLocation(UselessMod.MOD_ID, "textures/entity/useless_entity.png");
	}

}
