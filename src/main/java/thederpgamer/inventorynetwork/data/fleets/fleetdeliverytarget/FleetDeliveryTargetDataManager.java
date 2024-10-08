package thederpgamer.inventorynetwork.data.fleets.fleetdeliverytarget;

import org.schema.game.common.data.SegmentPiece;
import thederpgamer.inventorynetwork.data.DataManager;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * [Description]
 *
 * @author TheDerpGamer
 */
public class FleetDeliveryTargetDataManager extends DataManager<FleetDeliveryTargetData> {

	private final Set<FleetDeliveryTargetData> clientCache = new HashSet<>();

	private static FleetDeliveryTargetDataManager instance;

	public static FleetDeliveryTargetDataManager getInstance() {
		return instance;
	}

	public static void initialize(boolean client) {
		instance = new FleetDeliveryTargetDataManager();
		if(client) instance.requestFromServer();
	}

	@Override
	public FleetDeliveryTargetData getFromSegmentPiece(SegmentPiece segmentPiece) {
		return null;
	}

	@Override
	public Set<FleetDeliveryTargetData> getServerCache() {
		return Collections.emptySet();
	}

	@Override
	public Set<FleetDeliveryTargetData> getClientCache() {
		return Collections.emptySet();
	}

	@Override
	public void addToClientCache(FleetDeliveryTargetData data) {

	}

	@Override
	public void removeFromClientCache(FleetDeliveryTargetData data) {

	}

	@Override
	public void updateClientCache(FleetDeliveryTargetData data) {

	}
}
