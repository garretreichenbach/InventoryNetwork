package thederpgamer.inventorynetwork.ui.stockmanager;

import api.common.GameClient;
import org.schema.game.client.controller.PlayerInput;
import org.schema.game.client.view.gui.GUIInputInterface;
import org.schema.game.common.data.SegmentPiece;
import org.schema.schine.graphicsengine.core.GLFrame;
import org.schema.schine.graphicsengine.core.MouseEvent;
import org.schema.schine.graphicsengine.forms.gui.newgui.GUIMainWindow;

/**
 * [Description]
 *
 * @author TheDerpGamer
 */
public class StockManagerDialog extends PlayerInput {

	private final StockManagerPanel panel;

	public StockManagerDialog(SegmentPiece segmentPiece) {
		super(GameClient.getClientState());
		(panel = new StockManagerPanel(segmentPiece)).onInit();
	}

	@Override
	public void activate() {
		GameClient.getClientState().getWorldDrawer().getGuiDrawer().getPlayerInteractionManager().deactivateAll();
		super.activate();
	}

	@Override
	public void onDeactivate() {
		panel.cleanUp();
	}

	@Override
	public void handleMouseEvent(MouseEvent mouseEvent) {

	}

	@Override
	public StockManagerPanel getInputPanel() {
		return panel;
	}

	public static class StockManagerPanel extends GUIMainWindow implements GUIInputInterface {

		private final SegmentPiece segmentPiece;

		public StockManagerPanel(SegmentPiece segmentPiece) {
			super(GameClient.getClientState(), (int) (GLFrame.getWidth() / 1.5f), (int) (GLFrame.getHeight() / 1.5f), "stock_manager_panel");
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



			setSelectedTab(lastTab);
		}
	}
}
