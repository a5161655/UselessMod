package tk.themcbros.uselessmod.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CropsBlock;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.PlantType;
import tk.themcbros.uselessmod.lists.ModItems;

import javax.annotation.Nonnull;

public class UselessCropsBlock extends CropsBlock {

	private boolean wild;
	
	public UselessCropsBlock(Properties properties, boolean wild) {
		super(properties);
		this.wild = wild;
	}
	
	@Override
	protected boolean isValidGround(BlockState state, IBlockReader worldIn, BlockPos pos) {
		if(wild) {
			return state.getBlock() == Blocks.GRASS_BLOCK;
		}
		return super.isValidGround(state, worldIn, pos);
	}

	@Nonnull
	@Override
	protected IItemProvider getSeedsItem() {
		return ModItems.USELESS_WHEAT_SEEDS;
	}
	
	@Override
	public PlantType getPlantType(IBlockReader world, BlockPos pos) {
		return PlantType.Crop;
	}
	
	public BlockState getMaturePlant() {
		return withAge(getMaxAge());
	}

}
