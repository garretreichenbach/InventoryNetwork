package thederpgamer.inventorynetwork.data.stockmanager;

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
public class StockManagerDataManager extends DataManager<StockManagerData> {

	private final Set<StockManagerData> clientCache = new HashSet<>();

	private static StockManagerDataManager instance;

	public static StockManagerDataManager getInstance() {
		return instance;
	}

	public static void initialize(boolean client) {
		instance = new StockManagerDataManager();
		if(client) instance.requestFromServer();
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