package thederpgamer.inventorynetwork.data.supplier;

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
public class SupplierDataManager extends DataManager<SupplierData> {

	private final Set<SupplierData> clientCache = new HashSet<>();

	private static SupplierDataManager instance;

	public static SupplierDataManager getInstance() {
		return instance;
	}

	public static void initialize(boolean client) {
		instance = new SupplierDataManager();
		if(client) instance.requestFromServer();
	}

	@Override
	public SupplierData getFromSegmentPiece(SegmentPiece segmentPiece) {
		Set<SupplierData> cache = getCache(segmentPiece.getSegmentController().isOnServer());
		for(SupplierData data : cache) {
			if(data.getBlockIndex() == segmentPiece.getAbsoluteIndex() && data.getEntityID() == segmentPiece.getSegmentController().dbId) return data;
		}
		return null;
	}

	@Override
	public Set<SupplierData> getServerCache() {
		List<Object> objects = PersistentObjectUtil.getObjects(InventoryNetwork.getInstance().getSkeleton(), SupplierData.class);
		Set<SupplierData> data = new HashSet<>();
		for(Object object : objects) data.add((SupplierData) object);
		return data;
	}

	@Override
	public Set<SupplierData> getClientCache() {
		return Collections.unmodifiableSet(clientCache);
	}

	@Override
	public void addToClientCache(SupplierData data) {
		clientCache.add(data);
	}

	@Override
	public void removeFromClientCache(SupplierData data) {
		clientCache.remove(data);
	}

	@Override
	public void updateClientCache(SupplierData data) {
		clientCache.remove(data);
		clientCache.add(data);
	}
}
