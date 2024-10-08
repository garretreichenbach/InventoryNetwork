package thederpgamer.inventorynetwork.ui.fleetmanager;

import api.common.GameClient;
import org.schema.game.client.controller.PlayerInput;
import org.schema.game.client.view.gui.GUIInputInterface;
import org.schema.game.common.data.SegmentPiece;
import org.schema.schine.common.language.Lng;
import org.schema.schine.graphicsengine.core.GLFrame;
import org.schema.schine.graphicsengine.core.MouseEvent;
import org.schema.schine.graphicsengine.forms.gui.newgui.GUIContentPane;
import org.schema.schine.graphicsengine.forms.gui.newgui.GUIHorizontalButtonTablePane;
import org.schema.schine.graphicsengine.forms.gui.newgui.GUIMainWindow;
import thederpgamer.inventorynetwork.data.fleets.fleetmanager.FleetManagerData;
import thederpgamer.inventorynetwork.data.fleets.fleetmanager.FleetManagerDataManager;

/**
 * [Description]
 *
 * @author TheDerpGamer
 */
public class FleetManagerDialog extends PlayerInput {

	private final FleetManagerPanel panel;

	public FleetManagerDialog(SegmentPiece segmentPiece) {
		super(GameClient.getClientState());
		(panel = new FleetManagerPanel(segmentPiece)).onInit();
	}

	@Override
	public void activate() {
		GameClient.getClientState().getWorldDrawer().getGuiDrawer().getPlayerInteractionManager().deactivateAll();
		super.activate();
	}

	@Override
	public void onDeactivate() {

	}

	@Override
	public void handleMouseEvent(MouseEvent mouseEvent) {

	}

	@Override
	public FleetManagerPanel getInputPanel() {
		return panel;
	}

	public static class FleetManagerPanel extends GUIMainWindow implements GUIInputInterface {

		private final SegmentPiece segmentPiece;

		public FleetManagerPanel(SegmentPiece segmentPiece) {
			super(GameClient.getClientState(), (int) (GLFrame.getWidth() / 1.5f), (int) (GLFrame.getHeight() / 1.5f), "fleet_manager_panel");
			this.segmentPiece = segmentPiece;
		}

		@Override
		public void onInit() {
			super.onInit();
			recreateTabs();
		}

		public void recreateTabs() {
			int lastTab = getSelectedTab();
			if(!getTabs().isEmpty()) clearTabs();
			FleetManagerData data = FleetManagerDataManager.getInstance().getFromSegmentPiece(segmentPiece);
			if(data != null) {
				GUIContentPane fleetsPane = addTab(Lng.str("MANAGED FLEETS"));
				fleetsPane.setTextBoxHeightLast(300);
				fleetsPane.addDivider(450);
				fleetsPane.addNewTextBox(0, 32);
				fleetsPane.addNewTextBox(1, 32);

				FleetDeliveryJobsList deliveryJobsList = new FleetDeliveryJobsList(getState(), fleetsPane.getContent(0, 0), data);
				deliveryJobsList.onInit();
				fleetsPane.getContent(0, 0).attach(deliveryJobsList);
				GUIHorizontalButtonTablePane deliveryJobButtonPane = new GUIHorizontalButtonTablePane(getState(), 1, 1, fleetsPane.getContent(0, 1));
				deliveryJobButtonPane.onInit();

				AvailableFleetsList availableFleetsList = new AvailableFleetsList(getState(), fleetsPane.getContent(1, 0));
				availableFleetsList.onInit();
				fleetsPane.getContent(1, 0).attach(availableFleetsList);
				GUIHorizontalButtonTablePane availableFleetsButtonPane = new GUIHorizontalButtonTablePane(getState(), 1, 1, fleetsPane.getContent(1, 1));
				availableFleetsButtonPane.onInit();
			}
			setSelectedTab(lastTab);
		}
	}
}
