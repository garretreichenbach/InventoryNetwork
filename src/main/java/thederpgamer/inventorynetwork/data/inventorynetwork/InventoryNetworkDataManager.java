package thederpgamer.inventorynetwork.data.inventorynetwork;

import api.mod.config.PersistentObjectUtil;
import org.schema.game.common.data.SegmentPiece;
import thederpgamer.inventorynetwork.InventoryNetwork;
import thederpgamer.inventorynetwork.data.DataManager;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * [Description]
 *
 * @author TheDerpGamer
 */
public class InventoryNetworkDataManager extends DataManager<InventoryNetworkData> {

	private final Set<InventoryNetworkData> clientCache = new HashSet<>();

	private static InventoryNetworkDataManager instance;

	public static InventoryNetworkDataManager getInstance() {
		return instance;
	}

	public static void initialize(boolean client) {
		instance = new InventoryNetworkDataManager();
		if(client) instance.requestFromServer();
	}

	@Override
	public InventoryNetworkData getFromSegmentPiece(SegmentPiece segmentPiece) {
		Set<InventoryNetworkData> cache = getCache(segmentPiece.getSegmentController().isOnServer());
		for(InventoryNetworkData data : cache) {
			if(data.getBlockIndex() == segmentPiece.getAbsoluteIndex() && data.getEntityID() == segmentPiece.getSegmentController().dbId) return data;
		}
		return null;
	}

	@Override
	public Set<InventoryNetworkData> getServerCache() {
		List<Object> objects = PersistentObjectUtil.getObjects(InventoryNetwork.getInstance().getSkeleton(), InventoryNetworkData.class);
		Set<InventoryNetworkData> data = new HashSet<>();
		for(Object object : objects) data.add((InventoryNetworkData) object);
		return data;
	}

	@Override
	public Set<InventoryNetworkData> getClientCache() {
		return Collections.unmodifiableSet(clientCache);
	}

	@Override
	public void addToClientCache(InventoryNetworkData data) {
		clientCache.add(data);
	}

	@Override
	public void removeFromClientCache(InventoryNetworkData data) {
		clientCache.remove(data);
	}

	@Override
	public void updateClientCache(InventoryNetworkData data) {
		clientCache.remove(data);
		clientCache.add(data);
	}
}
