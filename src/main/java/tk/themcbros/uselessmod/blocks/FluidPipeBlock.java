package tk.themcbros.uselessmod.blocks;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.fluid.Fluids;
import net.minecraft.fluid.IFluidState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.IProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tags.FluidTags;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import tk.themcbros.uselessmod.energy.ConnectionType;
import tk.themcbros.uselessmod.fluids.FluidPipeNetworkManager;
import tk.themcbros.uselessmod.helper.IHammer;
import tk.themcbros.uselessmod.helper.ShapeUtils;
import tk.themcbros.uselessmod.tileentity.FluidPipeTileEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

public class FluidPipeBlock extends Block implements IWaterLoggable, IHammer {

	public static final EnumProperty<ConnectionType> NORTH = EnumProperty.create("north", ConnectionType.class);
	public static final EnumProperty<ConnectionType> EAST = EnumProperty.create("east", ConnectionType.class);
	public static final EnumProperty<ConnectionType> SOUTH = EnumProperty.create("south", ConnectionType.class);
	public static final EnumProperty<ConnectionType> WEST = EnumProperty.create("west", ConnectionType.class);
	public static final EnumProperty<ConnectionType> UP = EnumProperty.create("up", ConnectionType.class);
	public static final EnumProperty<ConnectionType> DOWN = EnumProperty.create("down", ConnectionType.class);
	public static final Map<Direction, EnumProperty<ConnectionType>> FACING_TO_PROPERTY_MAP = Util.make(Maps.newEnumMap(Direction.class), (map) -> {
		map.put(Direction.NORTH, NORTH);
		map.put(Direction.EAST, EAST);
		map.put(Direction.SOUTH, SOUTH);
		map.put(Direction.WEST, WEST);
		map.put(Direction.UP, UP);
		map.put(Direction.DOWN, DOWN);
	});
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

	private static final VoxelShape CENTER_SHAPE = Block.makeCuboidShape(5, 5, 5, 11, 11, 11);
	private static final VoxelShape NORTH_SHAPE = Block.makeCuboidShape(5, 5, 0, 11, 11, 5);
	private static final VoxelShape SOUTH_SHAPE = Block.makeCuboidShape(5, 5, 11, 11, 11, 16);
	private static final VoxelShape EAST_SHAPE = Block.makeCuboidShape(11, 5, 5, 16, 11, 11);
	private static final VoxelShape WEST_SHAPE = Block.makeCuboidShape(0, 5, 5, 5, 11, 11);
	private static final VoxelShape UP_SHAPE = Block.makeCuboidShape(5, 11, 5, 11, 16, 11);
	private static final VoxelShape DOWN_SHAPE = Block.makeCuboidShape(5, 0, 5, 11, 5, 11);

	public FluidPipeBlock(Properties properties) {
		super(properties);

		this.setDefaultState(this.stateContainer.getBaseState()
				.with(NORTH, ConnectionType.NONE)
				.with(SOUTH, ConnectionType.NONE)
				.with(EAST, ConnectionType.NONE)
				.with(WEST, ConnectionType.NONE)
				.with(UP, ConnectionType.NONE)
				.with(DOWN, ConnectionType.NONE)
				.with(WATERLOGGED, Boolean.FALSE));
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(NORTH, SOUTH, EAST, WEST, UP, DOWN, WATERLOGGED);
	}

	@Nonnull
	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		List<VoxelShape> shapes = Lists.newArrayList(CENTER_SHAPE);
		if(getConnection(state, Direction.NORTH).canConnect()) shapes.add(NORTH_SHAPE);
		if(getConnection(state, Direction.SOUTH).canConnect()) shapes.add(SOUTH_SHAPE);
		if(getConnection(state, Direction.EAST).canConnect()) shapes.add(EAST_SHAPE);
		if(getConnection(state, Direction.WEST).canConnect()) shapes.add(WEST_SHAPE);
		if(getConnection(state, Direction.UP).canConnect()) shapes.add(UP_SHAPE);
		if(getConnection(state, Direction.DOWN).canConnect()) shapes.add(DOWN_SHAPE);
		return ShapeUtils.combineAll(shapes);
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new FluidPipeTileEntity();
	}

	@Override
	public ActionResultType onHammer(ItemUseContext context) {
		BlockPos pos = context.getPos();
		World world = context.getWorld();
		BlockState state = world.getBlockState(pos);
		Vec3d relative = context.getHitVec().subtract(pos.getX(), pos.getY(), pos.getZ());

		Direction side = getClickedConnection(relative);
		if (side != null) {
			TileEntity other = world.getTileEntity(pos.offset(side));
			if (!(other instanceof FluidPipeTileEntity)) {
				BlockState state1 = cycleProperty(state, FACING_TO_PROPERTY_MAP.get(side));
				world.setBlockState(pos, state1, 18);
				FluidPipeNetworkManager.invalidateNetwork(world, pos);
				return ActionResultType.SUCCESS;
			}
		}

		return ActionResultType.PASS;
	}

	@Nullable
	private static Direction getClickedConnection(Vec3d relative) {
		if (relative.x < 0.25)
			return Direction.WEST;
		if (relative.x > 0.75)
			return Direction.EAST;
		if (relative.y < 0.25)
			return Direction.DOWN;
		if (relative.y > 0.75)
			return Direction.UP;
		if (relative.z < 0.25)
			return Direction.NORTH;
		if (relative.z > 0.75)
			return Direction.SOUTH;
		return null;
	}

	@SuppressWarnings("unchecked")
	private static <T extends Comparable<T>> BlockState cycleProperty(BlockState state, IProperty<T> propertyIn) {
		T value = getAdjacentValue(propertyIn.getAllowedValues(), state.get(propertyIn));
		if (value == ConnectionType.NONE || value == ConnectionType.BLOCKED)
			value = (T) ConnectionType.INPUT;
		return state.with(propertyIn, value);
	}

	private static <T> T getAdjacentValue(Iterable<T> p_195959_0_, @Nullable T p_195959_1_) {
		return Util.getElementAfter(p_195959_0_, p_195959_1_);
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		IFluidState ifluidstate = context.getWorld().getFluidState(context.getPos());
		return makeConnections(context.getWorld(), context.getPos()).with(WATERLOGGED,
				ifluidstate.isTagged(FluidTags.WATER) && ifluidstate.getLevel() == 8);
	}

	public BlockState makeConnections(IBlockReader worldIn, BlockPos pos) {
		return this.getDefaultState()
				.with(DOWN, createConnection(worldIn, pos.down(), Direction.DOWN, ConnectionType.NONE))
				.with(UP, createConnection(worldIn, pos.up(), Direction.UP, ConnectionType.NONE))
				.with(NORTH, createConnection(worldIn, pos.north(), Direction.NORTH, ConnectionType.NONE))
				.with(EAST, createConnection(worldIn, pos.east(), Direction.SOUTH, ConnectionType.NONE))
				.with(SOUTH, createConnection(worldIn, pos.south(), Direction.EAST, ConnectionType.NONE))
				.with(WEST, createConnection(worldIn, pos.west(), Direction.WEST, ConnectionType.NONE));
	}

	private static ConnectionType createConnection(IBlockReader worldIn, BlockPos pos, Direction facing, ConnectionType current) {
		TileEntity tileEntity = worldIn.getTileEntity(pos);
		if (tileEntity instanceof FluidPipeTileEntity)
			return ConnectionType.BOTH;
		else if (tileEntity != null && tileEntity.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, facing.getOpposite()).isPresent()) {
			return current == ConnectionType.NONE ? ConnectionType.BOTH : current;
		}
		return ConnectionType.NONE;
	}

	@Nonnull
	@Override
	public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
		if (stateIn.get(WATERLOGGED)) {
			worldIn.getPendingFluidTicks().scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickRate(worldIn));
		}

		if (worldIn.getTileEntity(facingPos) instanceof FluidPipeTileEntity)
			FluidPipeNetworkManager.invalidateNetwork(worldIn, currentPos);

		EnumProperty<ConnectionType> property = FACING_TO_PROPERTY_MAP.get(facing);
		ConnectionType current = stateIn.get(property);
		return stateIn.with(property, createConnection(worldIn, facingPos, facing, current));
	}

	@Nonnull
	public IFluidState getFluidState(@Nonnull BlockState state) {
		return state.get(WATERLOGGED) ? Fluids.WATER.getStillFluidState(false) : Fluids.EMPTY.getDefaultState();
	}

	@Nonnull
	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}

	@Nonnull
	public static ConnectionType getConnection(BlockState state, Direction side) {
		return state.get(FACING_TO_PROPERTY_MAP.get(side));
	}
}
