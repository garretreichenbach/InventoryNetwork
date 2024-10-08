package thederpgamer.inventorynetwork.data.job;

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
public class JobDataManager extends DataManager<JobData> {

	private final Set<JobData> clientCache = new HashSet<>();

	private static JobDataManager instance;

	public static JobDataManager getInstance() {
		return instance;
	}

	public static void initialize(boolean client) {
		instance = new JobDataManager();
		if(client) instance.requestFromServer();
	}

	@Override
	public JobData getFromSegmentPiece(SegmentPiece segmentPiece) {
		return null;
	}

	@Override
	public Set<JobData> getServerCache() {
		return Collections.emptySet();
	}

	@Override
	public Set<JobData> getClientCache() {
		return Collections.emptySet();
	}

	@Override
	public void addToClientCache(JobData data) {

	}

	@Override
	public void removeFromClientCache(JobData data) {

	}

	@Override
	public void updateClientCache(JobData data) {

	}
}
