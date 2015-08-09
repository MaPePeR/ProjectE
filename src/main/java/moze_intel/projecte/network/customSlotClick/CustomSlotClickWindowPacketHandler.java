package moze_intel.projecte.network.customSlotClick;

import moze_intel.projecte.utils.PELogger;

import cpw.mods.fml.common.network.FMLNetworkEvent;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.play.client.C0EPacketClickWindow;
import net.minecraft.network.play.server.S32PacketConfirmTransaction;
import net.minecraft.util.IntHashMap;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class CustomSlotClickWindowPacketHandler extends SimpleChannelInboundHandler<C0EPacketClickWindow>
{
	EntityPlayerMP playerEntity;
	NetHandlerPlayServer netHandlerPlayServer;
	IntHashMap intHashMapFromHandler;
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, C0EPacketClickWindow msg) throws Exception
	{
		PELogger.logFatal("opencontainer: " + playerEntity.openContainer.getClass().getName());
		PELogger.logFatal(packetToString(msg));

		if (!(playerEntity.openContainer instanceof ICustomSlotClick) || !((ICustomSlotClick) playerEntity.openContainer).isCustomSlotClickSlot(getSlot(msg), getButton(msg), getFlag(msg), playerEntity)) {
			ctx.fireChannelRead(msg);
			return;
		}
		PELogger.logFatal("Detected ICustomSlotClickContainer");
		processClickEvent(msg, (ICustomSlotClick) playerEntity.openContainer);

	}

	//Code from NetHandlerPlayServer.processClickEvent
	private void processClickEvent(C0EPacketClickWindow msg, ICustomSlotClick container)
	{
		this.playerEntity.func_143004_u();

		if (this.playerEntity.openContainer.windowId == getWindowId(msg) && this.playerEntity.openContainer.isPlayerNotUsingContainer(this.playerEntity))
		{
			ItemStack itemstack = container.slotClick(msg.func_149544_d(), msg.func_149543_e(), msg.func_149542_h(), this.playerEntity, getItemStack(msg));

			if (ItemStack.areItemStacksEqual(msg.func_149546_g(), itemstack))
			{
				this.playerEntity.playerNetServerHandler.sendPacket(new S32PacketConfirmTransaction(msg.func_149548_c(), msg.func_149547_f(), true));
				this.playerEntity.isChangingQuantityOnly = true;
				this.playerEntity.openContainer.detectAndSendChanges();
				this.playerEntity.updateHeldItem();
				this.playerEntity.isChangingQuantityOnly = false;
			}
			else
			{
				this.intHashMapFromHandler.addKey(this.playerEntity.openContainer.windowId, Short.valueOf(msg.func_149547_f()));
				this.playerEntity.playerNetServerHandler.sendPacket(new S32PacketConfirmTransaction(msg.func_149548_c(), msg.func_149547_f(), false));
				this.playerEntity.openContainer.setPlayerIsPresent(this.playerEntity, false);
				ArrayList arraylist = new ArrayList();

				for (int i = 0; i < this.playerEntity.openContainer.inventorySlots.size(); ++i)
				{
					arraylist.add(((Slot)this.playerEntity.openContainer.inventorySlots.get(i)).getStack());
				}

				this.playerEntity.sendContainerAndContentsToPlayer(this.playerEntity.openContainer, arraylist);
			}
		}
	}

	private String packetToString(C0EPacketClickWindow packet) {
		ItemStack itemStack = packet.func_149546_g();
		return String.format("id=%d, slot=%d, button=%d, type=%d, itemid=%d, itemcount=%d, itemaux=%d", new Object[]{
				Integer.valueOf(packet.func_149548_c()),
				Integer.valueOf(packet.func_149544_d()),
				Integer.valueOf(packet.func_149543_e()),
				Integer.valueOf(packet.func_149542_h()),
				Integer.valueOf(itemStack == null ? -1 : Item.getIdFromItem(itemStack.getItem())),
				Integer.valueOf(itemStack == null ? -1 : itemStack.stackSize),
				Integer.valueOf(itemStack == null ? -1 : itemStack.getItemDamage())});
	}

	private int getWindowId(C0EPacketClickWindow packet) {
		return packet.func_149548_c();
	}
	private ItemStack getItemStack(C0EPacketClickWindow packet) {
		return packet.func_149546_g();
	}

	private int getSlot(C0EPacketClickWindow packet) {
		return packet.func_149544_d();
	}

	private int getButton(C0EPacketClickWindow packet) {
		return packet.func_149543_e();
	}

	private int getFlag(C0EPacketClickWindow packet) {
		return packet.func_149542_h();
	}

	private short getTransactionID(C0EPacketClickWindow packet) {
		return packet.func_149547_f();
	}

	public void serverConnectionFromClient(FMLNetworkEvent.ServerConnectionFromClientEvent event) {
		if (!(event.handler instanceof NetHandlerPlayServer)) {
			PELogger.logFatal("Could not get NetHandlerPlayServer from ServerConnectionFromClientEvent");
			return;
		}
		NetHandlerPlayServer handler = (NetHandlerPlayServer) event.handler;
		this.playerEntity = handler.playerEntity;
		this.netHandlerPlayServer = handler;
		this.intHashMapFromHandler = getIntHashMapFromHandler(handler);
		if (this.intHashMapFromHandler == null) {
			PELogger.logFatal("Could not get IntHashMap from NetHandlerPlayServer!");
			return;
		}
		event.manager.channel().pipeline().addBefore("packet_handler", "projecte_clickwindow_packet_handler", this);
		PELogger.logFatal("SERVER CONNECTION FROM CLIENT");
	}

	private IntHashMap getIntHashMapFromHandler(NetHandlerPlayServer handler)
	{
		Field[] fields = handler.getClass().getDeclaredFields();
		IntHashMap result = null;
		for (Field f: fields) {
			if (f.getType().equals(IntHashMap.class)) {
				if (result != null) {
					PELogger.logFatal("Found multiple IntHashMap's in NetHandlerPlayServer");
					return null;
				}
				try
				{
					f.setAccessible(true);
					result = (IntHashMap) f.get(handler);
				} catch (IllegalAccessException e)
				{
					e.printStackTrace();
				}
			}
		}

		return result;
	}
}
