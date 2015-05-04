package moze_intel.projecte.gameObjs.items.tools;

import moze_intel.projecte.api.IExtraFunction;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class DarkSword extends PEToolBase implements IExtraFunction
{
	public DarkSword() 
	{
		super("dm_sword", (byte)2, new String[] {});
		this.setNoRepair();
		this.peToolMaterial = "dm_tools";
		this.pePrimaryToolClass = "sword";
	}

	// Only for RedSword to use
	protected DarkSword(String name, byte numcharges, String[] modeDesc)
	{
		super(name, numcharges, modeDesc);
	}
	
	@Override
	public boolean hitEntity(ItemStack stack, EntityLivingBase damaged, EntityLivingBase damager)
	{
		attackWithCharge(stack, damaged, damager, DARKSWORD_BASE_ATTACK);
		return true;
	}

	@Override
	public float func_150893_a(ItemStack p_150893_1_, Block p_150893_2_)
	{
		if (p_150893_2_ == Blocks.web)
		{
			return 15.0F;
		}
		else
		{
			Material material = p_150893_2_.getMaterial();
			return material != Material.plants && material != Material.vine && material != Material.coral && material != Material.leaves && material != Material.gourd ? 1.0F : 1.5F;
		}
	}

	@Override
	public EnumAction getItemUseAction(ItemStack par1ItemStack)
	{
		return EnumAction.block;
	}
	
	@Override
	public int getMaxItemUseDuration(ItemStack par1ItemStack)
	{
		return 72000;
	}

	@Override
	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
	{
		par3EntityPlayer.setItemInUse(par1ItemStack, this.getMaxItemUseDuration(par1ItemStack));
		return par1ItemStack;
	}

	@Override
	public boolean func_150897_b(Block p_150897_1_)
	{
		return p_150897_1_ == Blocks.web;
	}

	@Override
	public void doExtraFunction(ItemStack stack, EntityPlayer player)
	{
		attackAOE(stack, player, false, DARKSWORD_BASE_ATTACK, 0);
	}
}
