package tk.themcbros.uselessmod.items;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import tk.themcbros.uselessmod.config.EntityConfig;
import tk.themcbros.uselessmod.entity.GrenadeEntity;

public class GrenadeItem extends Item {

	public GrenadeItem(Properties properties) {
		super(properties);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerEntity, Hand handIn) {
		if (EntityConfig.grenade_enabled.get()) {
			ItemStack stack = playerEntity.getHeldItem(handIn);
			if (!playerEntity.abilities.isCreativeMode) {
				stack.shrink(1);
			}

			BlockPos pos = playerEntity.getPosition();
			worldIn.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ENTITY_EGG_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (random.nextFloat() * 0.4F + 0.8F));
			playerEntity.getCooldownTracker().setCooldown(this, 20);
			if (!worldIn.isRemote) {
				GrenadeEntity grenadeEntity = new GrenadeEntity(worldIn, playerEntity);
				grenadeEntity.setItem(stack);
				grenadeEntity.shoot(playerEntity, playerEntity.rotationPitch, playerEntity.rotationYaw, 0.0F, 1.5F, 1.0F);
				worldIn.addEntity(grenadeEntity);
			}

			playerEntity.addStat(Stats.ITEM_USED.get(this));
			return new ActionResult<ItemStack>(ActionResultType.SUCCESS, stack);
		}
		return super.onItemRightClick(worldIn, playerEntity, handIn);
	}

}
