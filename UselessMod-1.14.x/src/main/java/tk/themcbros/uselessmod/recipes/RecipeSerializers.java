package tk.themcbros.uselessmod.recipes;

import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipeSerializer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;
import tk.themcbros.uselessmod.UselessMod;

@ObjectHolder(UselessMod.MOD_ID)
public class RecipeSerializers {

	@ObjectHolder("crushing")
	public static final IRecipeSerializer<CrusherRecipe> CRUSHING = null;
	@ObjectHolder("compressing")
	public static final IRecipeSerializer<CompressorRecipe> COMPRESSING = null;
	@ObjectHolder("closet")
	public static final IRecipeSerializer<ClosetRecipe> CLOSET = null;
	@ObjectHolder("paint_brush")
	public static final IRecipeSerializer<PaintBrushRecipe> PAINT_BRUSH = null;
	
	@Mod.EventBusSubscriber(modid = UselessMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
	public static class Registration {
		@SubscribeEvent
		public static void onRegister(final RegistryEvent.Register<IRecipeSerializer<?>> event) {
			IForgeRegistry<IRecipeSerializer<?>> registry = event.getRegistry();
			
			registry.register(new CrusherRecipe.Serializer().setRegistryName(new ResourceLocation(UselessMod.MOD_ID, "crushing")));
			registry.register(new CompressorRecipe.Serializer().setRegistryName(new ResourceLocation(UselessMod.MOD_ID, "compressing")));
			registry.register(new SpecialRecipeSerializer<>(ClosetRecipe::new).setRegistryName(new ResourceLocation(UselessMod.MOD_ID, "closet")));
			registry.register(new SpecialRecipeSerializer<>(PaintBrushRecipe::new).setRegistryName(new ResourceLocation(UselessMod.MOD_ID, "paint_brush")));

			UselessMod.LOGGER.info("Registred Recipe Serializer");
		}
	}
	
}
