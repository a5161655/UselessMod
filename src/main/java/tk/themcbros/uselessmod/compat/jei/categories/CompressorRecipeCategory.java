package tk.themcbros.uselessmod.compat.jei.categories;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IGuiIngredientGroup;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import tk.themcbros.uselessmod.UselessMod;
import tk.themcbros.uselessmod.compat.jei.RecipeCategoryUid;
import tk.themcbros.uselessmod.compat.jei.ingredients.EnergyIngredient;
import tk.themcbros.uselessmod.compat.jei.ingredients.EnergyIngredientRenderer;
import tk.themcbros.uselessmod.lists.ModBlocks;
import tk.themcbros.uselessmod.recipes.CompressorRecipe;
import tk.themcbros.uselessmod.tileentity.CompressorTileEntity;

import java.util.List;

public class CompressorRecipeCategory implements IRecipeCategory<CompressorRecipe> {

    private final ResourceLocation TEXTURES = UselessMod.getId("textures/gui/container/jei_machines.png");
	
	private static final int input = 0;
	private static final int output = 1;

    private final IDrawableAnimated animatedEnergyBar;
	private final IDrawableAnimated animatedArrow;
	
	private final IDrawable background, icon;
	private final String name;
	
	public CompressorRecipeCategory(IGuiHelper helper) {
        IDrawableStatic staticEnergyBar = helper.createDrawable(TEXTURES, 239, 1, 16, 45);
		animatedEnergyBar = helper.createAnimatedDrawable(staticEnergyBar, 300, IDrawableAnimated.StartDirection.TOP, true);

		IDrawableStatic staticArrow = helper.createDrawable(TEXTURES, 232, 82, 24, 17);
		animatedArrow = helper.createAnimatedDrawable(staticArrow, 200, IDrawableAnimated.StartDirection.LEFT, false);
		
		background = helper.createDrawable(TEXTURES, 0, 114, 114, 47);
		icon = helper.createDrawableIngredient(new ItemStack(ModBlocks.COMPRESSOR));
		name = new TranslationTextComponent("container.uselessmod.compressor").getFormattedText();
	}
	
	@Override
	public IDrawable getBackground() {
		return background;
	}
	
	@Override
	public void draw(CompressorRecipe recipe, double mouseX, double mouseY) {
		animatedArrow.draw(24, 15);
		
		Minecraft minecraft = Minecraft.getInstance();
		FontRenderer fontRenderer = minecraft.fontRenderer;
	}
	
	@Override
	public String getTitle() {
		return name;
	}
	
	@Override
	public ResourceLocation getUid() {
		return RecipeCategoryUid.COMPRESSOR;
	}
	
	@Override
	public void setRecipe(IRecipeLayout recipeLayout, CompressorRecipe recipe, IIngredients ingredients) {
		IGuiItemStackGroup stacks = recipeLayout.getItemStacks();
		stacks.init(input, true, 0, 15);
		stacks.init(output, false, 60, 15);
		stacks.set(ingredients);
		
		IGuiIngredientGroup<EnergyIngredient> energyGroup = recipeLayout.getIngredientsGroup(EnergyIngredient.TYPE);
		energyGroup.init(0, true, new EnergyIngredientRenderer(animatedEnergyBar), 97, 1, 16, 45, 0, 0);
		List<List<EnergyIngredient>> energyInputs = ingredients.getInputs(EnergyIngredient.TYPE);
		energyGroup.set(0, energyInputs.get(0));
	}

	@Override
	public Class<? extends CompressorRecipe> getRecipeClass() {
		return CompressorRecipe.class;
	}

	@Override
	public IDrawable getIcon() {
		return icon;
	}

	@Override
	public void setIngredients(CompressorRecipe recipe, IIngredients ingredients) {
		ingredients.setInput(EnergyIngredient.TYPE, new EnergyIngredient(recipe.getCompressTime() * CompressorTileEntity.RF_PER_TICK, true));
		ingredients.setInputIngredients(recipe.getIngredients());
		ingredients.setOutput(VanillaTypes.ITEM, recipe.getRecipeOutput());
	}
	
}