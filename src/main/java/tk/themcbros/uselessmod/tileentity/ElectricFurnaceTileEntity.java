package tk.themcbros.uselessmod.tileentity;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.Direction;
import net.minecraft.util.IIntArray;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import tk.themcbros.uselessmod.blocks.MachineBlock;
import tk.themcbros.uselessmod.config.MachineConfig;
import tk.themcbros.uselessmod.container.ElectricFurnaceContainer;
import tk.themcbros.uselessmod.lists.ModTileEntities;
import tk.themcbros.uselessmod.machine.MachineTier;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ElectricFurnaceTileEntity extends MachineTileEntity {

	private static final int RF_PER_TICK = MachineConfig.furnace_rf_per_tick.get();

	private IIntArray fields = new IIntArray() {
		@Override
		public int size() {
			return 4;
		}

		@Override
		public void set(int id, int value) {
			switch (id) {
			case 0:
				ElectricFurnaceTileEntity.this.energyStorage.setEnergyStored(value);
				break;
			case 1:
				ElectricFurnaceTileEntity.this.energyStorage.setCapacity(value);
				break;
			case 2:
				ElectricFurnaceTileEntity.this.processTime = value;
				break;
			case 3:
				ElectricFurnaceTileEntity.this.processTimeTotal = value;
				break;
			}
		}

		@Override
		public int get(int id) {
			switch (id) {
			case 0:
				return ElectricFurnaceTileEntity.this.getEnergyStored();
			case 1:
				return ElectricFurnaceTileEntity.this.getMaxEnergyStored();
			case 2:
				return ElectricFurnaceTileEntity.this.processTime;
			case 3:
				return ElectricFurnaceTileEntity.this.processTimeTotal;
			default:
				return 0;
			}
		}
	};

	public ElectricFurnaceTileEntity() {
		super(ModTileEntities.ELECTRIC_FURNACE, MachineTier.STANDARD, false);
	}

	@Override
	public ITextComponent getDisplayName() {
		return new TranslationTextComponent("container.uselessmod.electric_furnace");
	}

	@Override
	public Container createMenu(int windowId, PlayerInventory playerInventory, PlayerEntity player) {
		return new ElectricFurnaceContainer(windowId, playerInventory, this, this.upgradeInventory, fields);
	}

	private boolean isActive() {
		return this.processTime > 0 && this.energyStorage.getEnergyStored() >= RF_PER_TICK;
	}

	@Override
	public void tick() {

		boolean flag = this.isActive();
		boolean flag1 = false;

		assert this.world != null;
		if (!this.world.isRemote) {
			if (this.isActive() || this.energyStorage.getEnergyStored() >= RF_PER_TICK && !this.items.get(0).isEmpty()) {
				FurnaceRecipe furnaceRecipe = this.world.getRecipeManager().getRecipe(IRecipeType.SMELTING, this, this.world).orElse(null);
				if (!this.isActive() && this.canSmelt(furnaceRecipe)) {
					this.energyStorage.modifyEnergyStored(-RF_PER_TICK);
					processTime++;
				}

				if (this.isActive() && this.canSmelt(furnaceRecipe)) {
					this.processTime++;
					this.energyStorage.modifyEnergyStored(-RF_PER_TICK);
					if (this.processTime == this.processTimeTotal) {
						this.processTime = 0;
						this.processTimeTotal = this.getCookTime();
						this.smeltItem(furnaceRecipe);
						flag1 = true;
					}
				} else {
					this.processTime = 0;
				}
			}

			if (flag != this.isActive()) {
				flag1 = true;
				this.world.setBlockState(this.pos, this.world.getBlockState(this.pos).with(MachineBlock.ACTIVE, this.isActive()), 3);
			}
		}

		if (flag1) {
			this.markDirty();
		}

	}

	private int getCookTime() {
		assert this.world != null;
		int cookTime = this.world.getRecipeManager().getRecipe(IRecipeType.SMELTING, this, this.world)
				.map(FurnaceRecipe::getCookTime).orElse(200);
		return this.getProcessTime(cookTime);
	}

	private boolean canSmelt(@Nullable FurnaceRecipe furnaceRecipe) {
		if (!this.items.get(0).isEmpty() && furnaceRecipe != null) {
			ItemStack recipeOutput = furnaceRecipe.getRecipeOutput();
			if (recipeOutput.isEmpty()) {
				return false;
			} else {
				ItemStack outputStack = this.items.get(1);
				if (outputStack.isEmpty()) {
					return true;
				} else if (!outputStack.isItemEqual(recipeOutput)) {
					return false;
				} else if (outputStack.getCount() + recipeOutput.getCount() <= this.getInventoryStackLimit()
						&& outputStack.getCount() + recipeOutput.getCount() <= outputStack.getMaxStackSize()) {
					return true;
				} else {
					return outputStack.getCount() + recipeOutput.getCount() <= recipeOutput.getMaxStackSize();
				}
			}
		} else {
			return false;
		}
	}

	private void smeltItem(@Nullable FurnaceRecipe furnaceRecipe) {
		if (furnaceRecipe != null && this.canSmelt(furnaceRecipe)) {
			ItemStack itemstack = this.items.get(0);
			ItemStack itemstack1 = furnaceRecipe.getRecipeOutput();
			ItemStack itemstack2 = this.items.get(1);
			if (itemstack2.isEmpty()) {
				this.items.set(1, itemstack1.copy());
			} else if (itemstack2.getItem() == itemstack1.getItem()) {
				itemstack2.grow(itemstack1.getCount());
			}
			itemstack.shrink(1);
		}
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		return index != 1;
	}

	@Nonnull
	@Override
	public int[] getSlotsForFace(@Nonnull Direction side) {
		return side == Direction.DOWN ? new int[] { 1 } : new int[] { 0 };
	}

	@Override
	public boolean canInsertItem(int index, ItemStack itemStackIn, Direction direction) {
		return this.isItemValidForSlot(index, itemStackIn);
	}

	@Override
	public boolean canExtractItem(int index, ItemStack stack, Direction direction) {
		return index == 1;
	}

	@Override
	public int getSizeInventory() {
		return 2;
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		ItemStack itemstack = this.items.get(index);
		boolean flag = !stack.isEmpty() && stack.isItemEqual(itemstack)
				&& ItemStack.areItemStackTagsEqual(stack, itemstack);
		this.items.set(index, stack);
		if (stack.getCount() > this.getInventoryStackLimit()) {
			stack.setCount(this.getInventoryStackLimit());
		}

		if (index == 0 && !flag) {
			this.processTimeTotal = this.getCookTime();
			this.processTime = 0;
			this.markDirty();
		}

	}

	@Override
	protected Container createMenu(int id, PlayerInventory player) {
		return new ElectricFurnaceContainer(id, player);
	}

}
