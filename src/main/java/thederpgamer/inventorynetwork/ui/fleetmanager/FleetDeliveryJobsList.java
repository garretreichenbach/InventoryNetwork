package thederpgamer.inventorynetwork.ui.fleetmanager;

import org.schema.schine.graphicsengine.forms.gui.GUIElement;
import org.schema.schine.graphicsengine.forms.gui.GUIElementList;
import org.schema.schine.graphicsengine.forms.gui.newgui.ScrollableTableList;
import org.schema.schine.input.InputState;
import thederpgamer.inventorynetwork.data.fleets.fleetdeliveryjob.FleetDeliveryJobData;
import thederpgamer.inventorynetwork.data.fleets.fleetmanager.FleetManagerData;

import java.util.Collection;
import java.util.Comparator;
import java.util.Set;

/**
 * [Description]
 *
 * @author TheDerpGamer
 */
public class FleetDeliveryJobsList extends ScrollableTableList<FleetDeliveryJobData> {

	private final FleetManagerData data;

	public FleetDeliveryJobsList(InputState state, GUIElement anchor, FleetManagerData data) {
		super(state, 300, 300, anchor);
		this.data = data;
	}


	@Override
	protected Collection<FleetDeliveryJobData> getElementList() {
		return data.getDeliveryJobs();
	}

	@Override
	public void initColumns() {
		addColumn("Fleet Name", 10.0f, new Comparator<FleetDeliveryJobData>() {
			@Override
			public int compare(FleetDeliveryJobData o1, FleetDeliveryJobData o2) {
				return o1.getFleetName().compareTo(o2.getFleetName());
			}
		});
		addColumn("Target Sector", 3.5f, new Comparator<FleetDeliveryJobData>() {
			@Override
			public int compare(FleetDeliveryJobData o1, FleetDeliveryJobData o2) {
				return o1.getTargetSector(false).compareTo(o2.getTargetSector(false));
			}
		});
		addColumn("Distance", 3.5f, new Comparator<FleetDeliveryJobData>() {
			@Override
			public int compare(FleetDeliveryJobData o1, FleetDeliveryJobData o2) {
				return o1.getDistance(false) - o2.getDistance(false);
			}
		});
		addColumn("Volume", 3.5f, new Comparator<FleetDeliveryJobData>() {
			@Override
			public int compare(FleetDeliveryJobData o1, FleetDeliveryJobData o2) {
				return (int) (o1.getTotalVolume() - o2.getTotalVolume());
			}
		});
		activeSortColumnIndex = 0;
	}

	@Override
	public void updateListEntries(GUIElementList guiElementList, Set<FleetDeliveryJobData> set) {

	}
}
