package thederpgamer.inventorynetwork.data;

import api.common.GameServer;
import api.mod.config.PersistentObjectUtil;
import api.network.packets.PacketUtil;
import org.schema.game.common.data.SegmentPiece;
import org.schema.game.common.data.player.PlayerState;
import thederpgamer.inventorynetwork.InventoryNetwork;
import thederpgamer.inventorynetwork.data.fleetmanager.FleetManagerDataManager;
import thederpgamer.inventorynetwork.data.inventorynetwork.InventoryNetworkDataManager;
import thederpgamer.inventorynetwork.data.stockmanager.StockManagerDataManager;
import thederpgamer.inventorynetwork.data.supplier.SupplierDataManager;
import thederpgamer.inventorynetwork.network.SendDataPacket;
import thederpgamer.inventorynetwork.network.SyncRequestPacket;

import java.util.Set;

/**
 * [Description]
 *
 * @author TheDerpGamer
 */
public abstract class DataManager<E extends SerializableData> {

	public static final int ADD_DATA = 0;
	public static final int REMOVE_DATA = 1;
	public static final int UPDATE_DATA = 2;

	public static void initialize(boolean client) {
		InventoryNetworkDataManager.initialize(client);
		StockManagerDataManager.initialize(client);
		SupplierDataManager.initialize(client);
		FleetManagerDataManager.initialize(client);
	}

	public void sendDataToAllPlayers(SerializableData data, int type) {
		for(PlayerState player : GameServer.getServerState().getPlayerStatesByName().values()) sendDataToPlayer(player, data, type);
	}

	public void sendDataToPlayer(PlayerState player, SerializableData data, int type) {
		PacketUtil.sendPacket(player, new SendDataPacket(data, type));
	}

	public void sendAllDataToPlayer(PlayerState player) {
		Set<E> cache = getCache(true);
		for(E data : cache) sendDataToPlayer(player, data, ADD_DATA);
	}

	public void requestFromServer() {
		PacketUtil.sendPacketToServer(new SyncRequestPacket());
	}

	public void sendPacket(SerializableData data, int type, boolean toServer) {
		if(toServer) PacketUtil.sendPacketToServer(new SendDataPacket(data, type));
		else sendDataToAllPlayers(data, type);
	}

	public Set<E> getCache(boolean isServer) {
		return isServer ? getServerCache() : getClientCache();
	}

	public void addData(E data, boolean server) {
		if(server) addToServerCache(data);
		else addToClientCache(data);
	}

	public void removeData(E data, boolean server) {
		if(server) removeFromServerCache(data);
		else removeFromClientCache(data);
	}

	public void updateData(E data, boolean server) {
		if(server) updateServerCache(data);
		else updateClientCache(data);
	}

	public void handlePacket(SerializableData data, int type, boolean server) {
		switch(type) {
			case ADD_DATA:
				addData((E) data, server);
				break;
			case REMOVE_DATA:
				removeData((E) data, server);
				break;
			case UPDATE_DATA:
				updateData((E) data, server);
				break;
		}
	}

	public E getFromUUID(String uuid, boolean server) {
		Set<E> cache = getCache(server);
		for(E data : cache) if(data.getUUID().equals(uuid)) return data;
		return null;
	}

	public abstract E getFromSegmentPiece(SegmentPiece segmentPiece);

	public abstract Set<E> getServerCache();

	public void addToServerCache(E data) {
		PersistentObjectUtil.addObject(InventoryNetwork.getInstance().getSkeleton(), data);
		PersistentObjectUtil.save(InventoryNetwork.getInstance().getSkeleton());
		sendDataToAllPlayers(data, ADD_DATA);
	}

	public void removeFromServerCache(E data) {
		PersistentObjectUtil.removeObject(InventoryNetwork.getInstance().getSkeleton(), data);
		PersistentObjectUtil.save(InventoryNetwork.getInstance().getSkeleton());
		sendDataToAllPlayers(data, REMOVE_DATA);
	}

	public void updateServerCache(E data) {
		PersistentObjectUtil.removeObject(InventoryNetwork.getInstance().getSkeleton(), data);
		PersistentObjectUtil.addObject(InventoryNetwork.getInstance().getSkeleton(), data);
		PersistentObjectUtil.save(InventoryNetwork.getInstance().getSkeleton());
		sendDataToAllPlayers(data, UPDATE_DATA);
	}

	public abstract Set<E> getClientCache();

	public abstract void addToClientCache(E data);

	public abstract void removeFromClientCache(E data);

	public abstract void updateClientCache(E data);
}
