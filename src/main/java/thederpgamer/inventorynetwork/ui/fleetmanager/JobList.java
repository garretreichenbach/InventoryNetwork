package thederpgamer.inventorynetwork.ui.fleetmanager;

import org.schema.schine.graphicsengine.forms.gui.GUIElement;
import org.schema.schine.graphicsengine.forms.gui.GUIElementList;
import org.schema.schine.graphicsengine.forms.gui.newgui.ScrollableTableList;
import org.schema.schine.input.InputState;
import thederpgamer.inventorynetwork.data.inventorynetwork.InventoryNetworkData;
import thederpgamer.inventorynetwork.data.job.JobData;
import thederpgamer.inventorynetwork.data.fleets.fleetmanager.FleetManagerData;

import java.util.Collection;
import java.util.Comparator;
import java.util.Set;

/**
 * [Description]
 *
 * @author TheDerpGamer
 */
public class JobList extends ScrollableTableList<JobData> {

	private final InventoryNetworkData data;

	public JobList(InputState state, GUIElement anchor, InventoryNetworkData data) {
		super(state, 300, 300, anchor);
		this.data = data;
	}


	@Override
	protected Collection<JobData> getElementList() {
		return data.getJobs();
	}

	@Override
	public void initColumns() {

		activeSortColumnIndex = 0;
	}

	@Override
	public void updateListEntries(GUIElementList guiElementList, Set<JobData> set) {

	}
}
