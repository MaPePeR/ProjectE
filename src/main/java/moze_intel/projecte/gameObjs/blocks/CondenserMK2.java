package moze_intel.projecte.gameObjs.blocks;

import moze_intel.projecte.PECore;
import moze_intel.projecte.gameObjs.ObjHandler;
import moze_intel.projecte.gameObjs.tiles.CondenserMK2Tile;
import moze_intel.projecte.utils.Constants;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.util.Random;

public class CondenserMK2 extends Condenser implements ITileEntityProvider
{
	public CondenserMK2()
	{
		super();
		this.setBlockName("pe_condenser_mk2");
	}

	@Override
	public Item getItemDropped(int par1, Random random, int par2)
	{
		return Item.getItemFromBlock(ObjHandler.condenserMk2);
	}

	@Override
	public int getRenderType()
	{
		return Constants.CONDENSER_MK2_RENDER_ID;
	}

	@Override
	public TileEntity createNewTileEntity(World var1, int var2)
	{
		return new CondenserMK2Tile();
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ)
	{
		if (!world.isRemote)
		{
			player.openGui(PECore.instance, Constants.CONDENSER_MK2_GUI, world, x, y, z);
		}

		return true;
	}
}
