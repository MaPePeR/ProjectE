package moze_intel.projecte.network.customSlotClick;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public interface ICustomSlotClick
{
	public boolean isCustomSlotClickSlot(int slot, int button, int flag, EntityPlayer player);
	public ItemStack slotClick(int slot, int button, int flag, EntityPlayer player, ItemStack itemStackFromClient);
}
