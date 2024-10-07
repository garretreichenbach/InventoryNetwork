package thederpgamer.inventorynetwork.data.fleetmanager;

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
public class FleetManagerDataManager extends DataManager<FleetManagerData> {

	private final Set<FleetManagerData> clientCache = new HashSet<>();

	private static FleetManagerDataManager instance;

	public static FleetManagerDataManager getInstance() {
		return instance;
	}

	public static void initialize(boolean client) {
		instance = new FleetManagerDataManager();
		if(client) instance.requestFromServer();
	}


	@Override
	public FleetManagerData getFromSegmentPiece(SegmentPiece segmentPiece) {
		Set<FleetManagerData> cache = getCache(segmentPiece.getSegmentController().isOnServer());
		for(FleetManagerData data : cache) {
			if(data.getBlockIndex() == segmentPiece.getAbsoluteIndex() && data.getEntityID() == segmentPiece.getSegmentController().dbId) return data;
		}
		return null;
	}

	@Override
	public Set<FleetManagerData> getServerCache() {
		List<Object> objects = PersistentObjectUtil.getObjects(InventoryNetwork.getInstance().getSkeleton(), FleetManagerData.class);
		Set<FleetManagerData> data = new HashSet<>();
		for(Object object : objects) data.add((FleetManagerData) object);
		return data;
	}

	@Override
	public Set<FleetManagerData> getClientCache() {
		return Collections.unmodifiableSet(clientCache);
	}

	@Override
	public void addToClientCache(FleetManagerData data) {
		clientCache.add(data);
	}

	@Override
	public void removeFromClientCache(FleetManagerData data) {
		clientCache.remove(data);
	}

	@Override
	public void updateClientCache(FleetManagerData data) {
		clientCache.remove(data);
		clientCache.add(data);
	}
}
