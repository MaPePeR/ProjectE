package moze_intel.projecte.network.customSlotClick;

import moze_intel.projecte.utils.PELogger;

import cpw.mods.fml.common.network.FMLNetworkEvent;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.play.client.C0EPacketClickWindow;

public class CustomSlotClickWindowPacketHandler extends SimpleChannelInboundHandler<C0EPacketClickWindow>
{
	EntityPlayerMP playerEntity;
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, C0EPacketClickWindow msg) throws Exception
	{
		PELogger.logFatal("opencontainer: " + playerEntity.openContainer.getClass().getName());
		PELogger.logFatal(packetToString(msg));

		ctx.fireChannelRead(msg);
	}

	private String packetToString(C0EPacketClickWindow packet) {
		ItemStack itemStack = packet.func_149546_g();
		return String.format("id=%d, slot=%d, button=%d, type=%d, itemid=%d, itemcount=%d, itemaux=%d", new Object[] {
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
		event.manager.channel().pipeline().addBefore("packet_handler", "projecte_clickwindow_packet_handler", this);
		PELogger.logFatal("SERVER CONNECTION FROM CLIENT");
	}
}
