package tk.themcbros.uselessmod.tileentity;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.LockableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;
import tk.themcbros.uselessmod.blocks.MachineBlock;
import tk.themcbros.uselessmod.energy.CustomEnergyStorage;
import tk.themcbros.uselessmod.items.UpgradeItem;
import tk.themcbros.uselessmod.machine.MachineTier;
import tk.themcbros.uselessmod.machine.MachineUpgradeInventory;
import tk.themcbros.uselessmod.machine.RedstoneMode;
import tk.themcbros.uselessmod.machine.Upgrade;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class MachineTileEntity extends LockableTileEntity implements ITickableTileEntity, ISidedInventory {
	
	protected static final int DEFAULT_RF_PER_TICK = 15;

	public int processTime, processTimeTotal;
	protected CustomEnergyStorage energyStorage;
	protected MachineUpgradeInventory upgradeInventory;
	protected MachineTier machineTier;
	protected RedstoneMode redstoneMode;
	
	protected NonNullList<ItemStack> items = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);

	private final boolean isGenerator;

	public boolean isGenerator() {
		return isGenerator;
	}

	public MachineTileEntity(@Nonnull TileEntityType<?> tileEntityTypeIn, @Nonnull MachineTier machineTier, boolean generator) {
		super(tileEntityTypeIn);
		this.isGenerator = generator;
		this.machineTier = machineTier;
		this.energyStorage = new CustomEnergyStorage(machineTier.getMachineCapacity(), !generator ? machineTier.getMaxEnergyTransfer() : 0, generator ? machineTier.getMaxEnergyTransfer() : 0, 0);
		this.upgradeInventory = new MachineUpgradeInventory(this);
	}
	
	@Override
	public CompoundNBT write(CompoundNBT compound) {
		super.write(compound);
		compound.putInt("ProcessTime", this.processTime);
		compound.putInt("ProcessTimeTotal", this.processTimeTotal);
		compound.putString("Tier", this.machineTier != null ? this.machineTier.getName() : "error");
		compound.putString("RedstoneMode", this.redstoneMode != null ? this.redstoneMode.getName() : "ignored");
		compound.put("Energy", this.energyStorage.serializeNBT());
		this.upgradeInventory.write(compound);
		ItemStackHelper.saveAllItems(compound, items, false);
		return compound;
	}

	@Override
	public void read(CompoundNBT compound) {
		super.read(compound);
		this.processTime = compound.getInt("ProcessTime");
		this.processTimeTotal = compound.getInt("ProcessTimeTotal");
		this.machineTier = MachineTier.byName(compound.getString("Tier"));
		this.redstoneMode = RedstoneMode.byName(compound.getString("RedstoneMode"));
		this.energyStorage = CustomEnergyStorage.fromMachine(this, compound.getCompound("Energy"));
		this.items = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);
		ItemStackHelper.loadAllItems(compound, items);
		this.upgradeInventory = new MachineUpgradeInventory(this);
		this.upgradeInventory.read(compound);
	}
	
	void receiveEnergyFromSlot(int index) {
		final ItemStack energySlotStack = this.items.get(index);
		if(!energySlotStack.isEmpty()) {
			IEnergyStorage handler = energySlotStack.getCapability(CapabilityEnergy.ENERGY).orElse(null);
			if(handler == null) return;
			int accept = handler.extractEnergy(Math.min(this.getMaxEnergyStored() - this.getEnergyStored(), this.machineTier.getMaxEnergyTransfer()), true);
			if(this.getEnergyStored() <= this.getMaxEnergyStored() - accept)
				this.energyStorage.modifyEnergyStored(handler.extractEnergy(accept, false));
		}
	}

	int getProcessTime(int defaultTime) {
		float speed = this.machineTier.getMachineSpeed();
		for(ItemStack stack : this.upgradeInventory.getStacks()) {
			if(!stack.isEmpty() && stack.getItem() instanceof UpgradeItem) {
				if(((UpgradeItem) stack.getItem()).getUpgrade() == Upgrade.SPEED) {
					speed += (0.5f * stack.getCount());
				}
			}
		}
		defaultTime = (int) (defaultTime / speed);
		return defaultTime;
	}

	@Override
	public boolean isEmpty() {
		for (ItemStack itemstack : this.items) {
			if (!itemstack.isEmpty()) {
				return false;
			}
		}

		return true;
	}
	
	@Nonnull
	@Override
	public ItemStack getStackInSlot(int index) {
		return this.items.get(index);
	}
	
	@Nonnull
	@Override
	public ItemStack decrStackSize(int index, int count) {
		return ItemStackHelper.getAndSplit(this.items, index, count);
	}
	
	@Nonnull
	@Override
	public ItemStack removeStackFromSlot(int index) {
		return ItemStackHelper.getAndRemove(this.items, index);
	}
	
	@Override
	public void clear() {
		this.items.clear();
	}
	
	@Override
	public boolean isUsableByPlayer(@Nonnull PlayerEntity player) {
		assert this.world != null;
		if (this.world.getTileEntity(this.pos) != this) {
			return false;
		} else {
			return !(player.getDistanceSq((double) this.pos.getX() + 0.5D, (double) this.pos.getY() + 0.5D,
					(double) this.pos.getZ() + 0.5D) > 64.0D);
		}
	}
	
	@Override
	public int getInventoryStackLimit() {
		return 64;
	}
	
	private LazyOptional<? extends IEnergyStorage> energyHandler = LazyOptional.of(() -> this.energyStorage);
	private final LazyOptional<? extends IItemHandler>[] itemHandlers = SidedInvWrapper.create(this, Direction.UP, Direction.DOWN, Direction.NORTH);
	
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, Direction side) {
		if (!this.removed && cap == CapabilityEnergy.ENERGY) {
			return energyHandler.cast();
		} else if (!this.removed && cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			if (side == Direction.UP)
				return itemHandlers[0].cast();
			else if (side == Direction.DOWN)
				return itemHandlers[1].cast();
			else
				return itemHandlers[2].cast();
		}
		return LazyOptional.empty();
	}

	/**
	 * invalidates a tile entity
	 */
	@Override
	public void remove() {
		super.remove();
		energyHandler.invalidate();
		for (int x = 0; x < itemHandlers.length; x++)
			itemHandlers[x].invalidate();
	}

	@Deprecated
	public int getEnergyStored() {
		return this.energyStorage.getEnergyStored();
	}

	@Deprecated
	public int getMaxEnergyStored() {
		return this.machineTier.getMachineCapacity();
	}

	@Nonnull
	@Override
	public abstract ITextComponent getDisplayName();

	@Nonnull
	@Override
	protected ITextComponent getDefaultName() {
		return this.getDisplayName();
	}

	@Nullable
	public MachineTier getMachineTier() {
		return this.machineTier;
	}

	public RedstoneMode getRedstoneMode() {
		return redstoneMode;
	}

	public void setMachineTier(MachineTier machineTier) {
		this.machineTier = machineTier;
		this.energyStorage = CustomEnergyStorage.fromMachine(this, this.energyStorage.serializeNBT());
		this.markDirty();
	}

	public void setRedstoneMode(RedstoneMode redstoneMode) {
		this.redstoneMode = redstoneMode;
		this.markDirty();
	}

	protected void sendUpdate(BlockState newState, boolean force) {
		if (world == null) return;
		BlockState oldState = world.getBlockState(pos);
		if (oldState != newState || force) {
			world.setBlockState(pos, newState, 3);
			world.notifyBlockUpdate(pos, oldState, newState, 3);
		}
	}

	protected BlockState getActiveState() {
		assert this.world != null;
		return this.world.getBlockState(this.pos).with(MachineBlock.ACTIVE, Boolean.TRUE);
	}

	protected BlockState getInactiveState() {
		assert this.world != null;
		return this.world.getBlockState(this.pos).with(MachineBlock.ACTIVE, Boolean.FALSE);
	}

}
