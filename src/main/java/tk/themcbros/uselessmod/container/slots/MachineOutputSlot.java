package tk.themcbros.uselessmod.container.slots;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import tk.themcbros.uselessmod.handler.UselessPlayerEvents;
import tk.themcbros.uselessmod.tileentity.CompressorTileEntity;
import tk.themcbros.uselessmod.tileentity.CrusherTileEntity;
import tk.themcbros.uselessmod.tileentity.ElectricCrusherTileEntity;
import tk.themcbros.uselessmod.tileentity.ElectricFurnaceTileEntity;

public class MachineOutputSlot extends Slot {
	private final PlayerEntity player;
	private int removeCount;

	public MachineOutputSlot(PlayerEntity player, IInventory inventoryIn, int slotIndex, int xPosition, int yPosition) {
		super(inventoryIn, slotIndex, xPosition, yPosition);
		this.player = player;
	}

	/**
	 * Check if the stack is allowed to be placed in this slot, used for armor slots
	 * as well as furnace fuel.
	 */
	public boolean isItemValid(ItemStack stack) {
		return false;
	}

	/**
	 * Decrease the size of the stack in slot (first int arg) by the amount of the
	 * second int arg. Returns the new stack.
	 */
	public ItemStack decrStackSize(int amount) {
		if (this.getHasStack()) {
			this.removeCount += Math.min(amount, this.getStack().getCount());
		}

		return super.decrStackSize(amount);
	}

	public ItemStack onTake(PlayerEntity thePlayer, ItemStack stack) {
		this.onCrafting(stack);
		super.onTake(thePlayer, stack);
		return stack;
	}

	/**
	 * the itemStack passed in is the output - ie, iron ingots, and pickaxes, not
	 * ore and wood. Typically increases an internal count then calls
	 * onCrafting(item).
	 */
	protected void onCrafting(ItemStack stack, int amount) {
		this.removeCount += amount;
		this.onCrafting(stack);
	}

	/**
	 * the itemStack passed in is the output - ie, iron ingots, and pickaxes, not
	 * ore and wood.
	 */
	protected void onCrafting(ItemStack stack) {
		stack.onCrafting(this.player.world, this.player, this.removeCount);

		this.removeCount = 0;
		if (this.inventory instanceof CrusherTileEntity || this.inventory instanceof ElectricCrusherTileEntity)
			MinecraftForge.EVENT_BUS.post(new UselessPlayerEvents.ItemCrushedEvent(this.player, stack));
		else if (this.inventory instanceof ElectricFurnaceTileEntity)
			net.minecraftforge.fml.hooks.BasicEventHooks.firePlayerSmeltedEvent(this.player, stack);
		if (this.inventory instanceof CompressorTileEntity)
			MinecraftForge.EVENT_BUS.post(new UselessPlayerEvents.ItemCompressedEvent(this.player, stack));
	}
}
