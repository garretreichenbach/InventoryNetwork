package thederpgamer.inventorynetwork.manager;

import api.network.packets.PacketUtil;
import thederpgamer.inventorynetwork.network.SendDataPacket;
import thederpgamer.inventorynetwork.network.SyncRequestPacket;

public class PacketManager {

	public static void initialize() {
		PacketUtil.registerPacket(SyncRequestPacket.class);
		PacketUtil.registerPacket(SendDataPacket.class);
	}
}