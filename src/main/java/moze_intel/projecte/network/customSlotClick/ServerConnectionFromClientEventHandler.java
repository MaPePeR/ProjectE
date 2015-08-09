package moze_intel.projecte.network.customSlotClick;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent;

public class ServerConnectionFromClientEventHandler
{

	@SubscribeEvent
	public void serverConnectionFromClient(FMLNetworkEvent.ServerConnectionFromClientEvent event) {
		new CustomSlotClickWindowPacketHandler().serverConnectionFromClient(event);
	}
}
