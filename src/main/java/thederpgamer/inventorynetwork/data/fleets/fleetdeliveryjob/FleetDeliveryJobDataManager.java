package thederpgamer.inventorynetwork.data.fleets.fleetdeliveryjob;

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
public class FleetDeliveryJobDataManager extends DataManager<FleetDeliveryJobData> {

	private final Set<FleetDeliveryJobData> clientCache = new HashSet<>();

	private static FleetDeliveryJobDataManager instance;

	public static FleetDeliveryJobDataManager getInstance() {
		return instance;
	}

	public static void initialize(boolean client) {
		instance = new FleetDeliveryJobDataManager();
		if(client) instance.requestFromServer();
	}

	@Override
	public FleetDeliveryJobData getFromSegmentPiece(SegmentPiece segmentPiece) {
		return null;
	}

	@Override
	public Set<FleetDeliveryJobData> getServerCache() {
		return Collections.emptySet();
	}

	@Override
	public Set<FleetDeliveryJobData> getClientCache() {
		return Collections.emptySet();
	}

	@Override
	public void addToClientCache(FleetDeliveryJobData data) {

	}

	@Override
	public void removeFromClientCache(FleetDeliveryJobData data) {

	}

	@Override
	public void updateClientCache(FleetDeliveryJobData data) {

	}
}
