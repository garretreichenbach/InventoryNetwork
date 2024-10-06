package thederpgamer.inventorynetwork.manager;

import api.mod.config.PersistentObjectUtil;
import api.network.packets.PacketUtil;
import org.schema.game.common.data.SegmentPiece;
import thederpgamer.inventorynetwork.InventoryNetwork;
import thederpgamer.inventorynetwork.data.SerializableData;
import thederpgamer.inventorynetwork.data.StockManagerData;
import thederpgamer.inventorynetwork.network.SendDataPacket;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * [Description]
 *
 * @author TheDerpGamer
 */
public class StockDataManager extends DataManager<StockManagerData> {

	private final Set<StockManagerData> clientCache = new HashSet<>();

	private static StockDataManager instance;

	public static StockDataManager getInstance() {
		return instance;
	}

	public static void initialize(boolean client) {
		instance = new StockDataManager();
		if(client) instance.requestFromServer();
	}

	public void handlePacket(SerializableData data, int type, boolean server) {
		StockManagerData stockManagerData = (StockManagerData) data;
		switch(type) {
			case ADD_DATA:
				if(server) instance.addToServerCache(stockManagerData);
				else instance.addToClientCache(stockManagerData);
				break;
			case REMOVE_DATA:
				if(server) instance.removeFromServerCache(stockManagerData);
				else instance.removeFromClientCache(stockManagerData);
				break;
			case UPDATE_DATA:
				if(server) instance.updateServerCache(stockManagerData);
				else instance.updateClientCache(stockManagerData);
				break;
		}
	}

	public void sendPacket(SerializableData data, int type, boolean toServer) {
		if(toServer) PacketUtil.sendPacketToServer(new SendDataPacket(data, type));
		else sendDataToAllPlayers(data, type);
	}

	@Override
	public StockManagerData getFromUUID(String uuid) {
		Set<StockManagerData> cache = getCache(false);
		for(StockManagerData data : cache) {
			if(data.getUUID().equals(uuid)) return data;
		}
		return null;
	}

	@Override
	public StockManagerData getFromSegmentPiece(SegmentPiece segmentPiece) {
		Set<StockManagerData> cache = getCache(segmentPiece.getSegmentController().isOnServer());
		for(StockManagerData data : cache) {
			if(data.getBlockIndex() == segmentPiece.getAbsoluteIndex() && data.getEntityID() == segmentPiece.getSegmentController().dbId) return data;
		}
		return null;
	}

	@Override
	public Set<StockManagerData> getServerCache() {
		List<Object> objects = PersistentObjectUtil.getObjects(InventoryNetwork.getInstance().getSkeleton(), StockManagerData.class);
		Set<StockManagerData> data = new HashSet<>();
		for(Object object : objects) data.add((StockManagerData) object);
		return data;
	}

	@Override
	public void addToServerCache(StockManagerData data) {
		PersistentObjectUtil.addObject(InventoryNetwork.getInstance().getSkeleton(), data);
		PersistentObjectUtil.save(InventoryNetwork.getInstance().getSkeleton());
		sendDataToAllPlayers(data, ADD_DATA);
	}

	@Override
	public void removeFromServerCache(StockManagerData data) {
		PersistentObjectUtil.removeObject(InventoryNetwork.getInstance().getSkeleton(), data);
		PersistentObjectUtil.save(InventoryNetwork.getInstance().getSkeleton());
		sendDataToAllPlayers(data, REMOVE_DATA);
	}

	@Override
	public void updateServerCache(StockManagerData data) {
		PersistentObjectUtil.removeObject(InventoryNetwork.getInstance().getSkeleton(), data);
		PersistentObjectUtil.addObject(InventoryNetwork.getInstance().getSkeleton(), data);
		PersistentObjectUtil.save(InventoryNetwork.getInstance().getSkeleton());
		sendDataToAllPlayers(data, UPDATE_DATA);
	}

	@Override
	public Set<StockManagerData> getClientCache() {
		return Collections.unmodifiableSet(clientCache);
	}

	@Override
	public void addToClientCache(StockManagerData data) {
		clientCache.add(data);
	}

	@Override
	public void removeFromClientCache(StockManagerData data) {
		clientCache.remove(data);
	}

	@Override
	public void updateClientCache(StockManagerData data) {
		clientCache.remove(data);
		clientCache.add(data);
	}
}