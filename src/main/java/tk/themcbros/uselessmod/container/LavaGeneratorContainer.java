package tk.themcbros.uselessmod.container;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IntArray;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import tk.themcbros.uselessmod.lists.ModContainerTypes;
import tk.themcbros.uselessmod.tileentity.LavaGeneratorTileEntity;

import javax.annotation.Nonnull;

public class LavaGeneratorContainer extends Container {

	private IInventory lavaGeneratorInventory;
	private IIntArray fields;
	private BlockPos pos;
	private World world;
	
	public LavaGeneratorContainer(int id, PlayerInventory playerInventory) {
		this(id, playerInventory, new LavaGeneratorTileEntity(), new IntArray(7));
	}

	public LavaGeneratorContainer(int id, @Nonnull PlayerInventory playerInventory, @Nonnull LavaGeneratorTileEntity lavaGeneratorInventory, IIntArray fields) {
		super(ModContainerTypes.LAVA_GENERATOR, id);
		this.lavaGeneratorInventory = lavaGeneratorInventory;
		this.fields = fields;
		this.world = playerInventory.player.world;
		this.pos = lavaGeneratorInventory.getPos();
		
		// Machine Slots
		this.addSlot(new Slot(lavaGeneratorInventory, 0, 28, 11) {
			@Override
			public boolean isItemValid(ItemStack stack) {
				return stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY)
						.map(handler -> handler.getFluidInTank(0).getRawFluid().equals(Fluids.LAVA)).orElse(false);
			}
		});
		this.addSlot(new Slot(lavaGeneratorInventory, 1, 28, 48) {
			@Override
			public boolean isItemValid(ItemStack p_75214_1_) {
				return false;
			}
		});
		
		// Upgrade Slots
		
		// Inventory Slots
		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 9; ++j) {
				this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}
		}

		for (int k = 0; k < 9; ++k) {
			this.addSlot(new Slot(playerInventory, k, 8 + k * 18, 142));
		}
		
		this.trackIntArray(fields);
	}
	
	@Override
	public boolean canInteractWith(PlayerEntity playerIn) {
		return this.lavaGeneratorInventory.isUsableByPlayer(playerIn);
	}
	
	@Nonnull
	@Override
	public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
		int machineSlotCount = 2;
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.inventorySlots.get(index);
		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
			if (index == 1) {
				if (!this.mergeItemStack(itemstack1, machineSlotCount, machineSlotCount + 36, true)) {
					return ItemStack.EMPTY;
				}

				slot.onSlotChange(itemstack1, itemstack);
			} else if (index >= machineSlotCount) {
				if (this.isValid(itemstack1)) {
					if (!this.mergeItemStack(itemstack1, 0, 1, false)) {
						return ItemStack.EMPTY;
					}
				} else if (index >= machineSlotCount && index < machineSlotCount + 27) {
					if (!this.mergeItemStack(itemstack1, machineSlotCount + 27, machineSlotCount + 36, false)) {
						return ItemStack.EMPTY;
					}
				} else if (index >= machineSlotCount + 27 && index < machineSlotCount + 36 && !this.mergeItemStack(itemstack1, machineSlotCount, machineSlotCount + 27, false)) {
					return ItemStack.EMPTY;
				}
			} else if (!this.mergeItemStack(itemstack1, machineSlotCount, machineSlotCount + 36, false)) {
				return ItemStack.EMPTY;
			}

			if (itemstack1.isEmpty()) {
				slot.putStack(ItemStack.EMPTY);
			} else {
				slot.onSlotChanged();
			}
		}

		return itemstack;
	}

	private boolean isValid(@Nonnull ItemStack stack) {
		return !stack.isEmpty() && stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY)
				.map(handler -> handler.getFluidInTank(0).getFluid().isIn(FluidTags.LAVA)).orElse(false);
	}

	/**
	 * Called when the container is closed.
	 */
	public void onContainerClosed(PlayerEntity playerIn) {
		super.onContainerClosed(playerIn);
		this.lavaGeneratorInventory.closeInventory(playerIn);
	}
	
	@OnlyIn(Dist.CLIENT)
	public int getEnergyStoredScaled(int height) {
		int i = this.getEnergyStored();
		int j = this.getMaxEnergyStored();
		return j != 0 && i != 0 ? i * height / j : 0;
	}
	
	@OnlyIn(Dist.CLIENT)
	public int getBurnTimeScaled(int scale) {
		int i = this.getBurnTime();
		int j = LavaGeneratorTileEntity.TICKS_PER_MB;
		return j != 0 && i != 0 ? i * scale / j : 0;
	}
	
	public int getEnergyStored() {
		return this.fields.get(4);
	}
	
	public int getMaxEnergyStored() {
		return this.fields.get(5);
	}
	
	public int getBurnTime() {
		return this.fields.get(0);
	}
	
	public int getFluidAmount() {
		return this.fields.get(1);
	}
	
	public int getMaxFluidAmount() {
		return this.fields.get(2);
	}
	
	public BlockPos getPos() {
		return pos;
	}
	
	public World getWorld() {
		return world;
	}

	@SuppressWarnings("deprecation")
	public Fluid getTankFluid() {
		return Registry.FLUID.getByValue(this.fields.get(3));
	}
	
	public FluidStack getTankStack() {
		return new FluidStack(getTankFluid(), getFluidAmount());
	}

	@OnlyIn(Dist.CLIENT)
	public IFluidHandler getFluidTankHandler() {
		return new IFluidHandler() {
			
			@Override
			public boolean isFluidValid(int tank, FluidStack stack) {
				return true;
			}
			
			@Override
			public int getTanks() {
				return 1;
			}
			
			@Override
			public int getTankCapacity(int tank) {
				return LavaGeneratorContainer.this.getMaxFluidAmount();
			}
			
			@Override
			public FluidStack getFluidInTank(int tank) {
				return LavaGeneratorContainer.this.getTankStack();
			}
			
			@Override
			public int fill(FluidStack resource, FluidAction action) {
				return 0;
			}
			
			@Override
			public FluidStack drain(int maxDrain, FluidAction action) {
				return FluidStack.EMPTY;
			}
			
			@Override
			public FluidStack drain(FluidStack resource, FluidAction action) {
				return resource;
			}
		};
	}
	
}
