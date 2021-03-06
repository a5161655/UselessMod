package tk.themcbros.uselessmod.lists;

import java.util.List;
import java.util.Random;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import tk.themcbros.uselessmod.closet.ClosetRegistry;
import tk.themcbros.uselessmod.closet.IClosetMaterial;

public class ModItemGroups {

	public static final ItemGroup USELESS_ITEM_GROUP = new ItemGroup("uselessmod") {
		
		@Override
		public ItemStack createIcon() {
			return new ItemStack(ModItems.USELESS_INGOT);
		}
		
		public boolean hasSearchBar() { return false; };
		
		public net.minecraft.util.ResourceLocation getBackgroundImage() {
			return new net.minecraft.util.ResourceLocation("uselessmod:textures/gui/container/creative_inventory/tab_useless.png");
		};
		
		public net.minecraft.util.ResourceLocation getTabsImage() {
			return new net.minecraft.util.ResourceLocation("uselessmod:textures/gui/container/creative_inventory/tabs.png"); 
		};
	};
	
	public static final ItemGroup CLOSET_GROUP = new ItemGroup("uselessmod.closets") {
		
		private Random random = new Random();
		private int counter = 0;
		private ItemStack currentIcon = ItemStack.EMPTY;

		@Override
		@OnlyIn(Dist.CLIENT)
		public ItemStack createIcon() {
			return ClosetRegistry.createItemStack(this.pickRandomString(ClosetRegistry.CASINGS.getKeys()),
					this.pickRandomString(ClosetRegistry.BEDDINGS.getKeys()));
		}
		
		@Override
		@OnlyIn(Dist.CLIENT)
		public ItemStack getIcon() {
			if(this.currentIcon.isEmpty() || counter > 250) {
				this.currentIcon = this.createIcon();
				counter = 0;
			}
			counter++;
			return this.currentIcon;
		}
		
		public boolean hasSearchBar() { return false; };
		
		public net.minecraft.util.ResourceLocation getBackgroundImage() {
			return new net.minecraft.util.ResourceLocation("uselessmod:textures/gui/container/creative_inventory/tab_useless.png");
		};
		
		public net.minecraft.util.ResourceLocation getTabsImage() {
			return new net.minecraft.util.ResourceLocation("uselessmod:textures/gui/container/creative_inventory/tabs.png");
		};

		public IClosetMaterial pickRandomString(List<IClosetMaterial> strs) {
			return strs.get(this.random.nextInt(strs.size()));
		}
	};
	
}
