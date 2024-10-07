package thederpgamer.inventorynetwork.ui.stockmanager;

import api.common.GameClient;
import org.schema.game.client.controller.PlayerInput;
import org.schema.game.client.view.gui.GUIInputInterface;
import org.schema.game.common.data.SegmentPiece;
import org.schema.schine.common.language.Lng;
import org.schema.schine.graphicsengine.core.GLFrame;
import org.schema.schine.graphicsengine.core.MouseEvent;
import org.schema.schine.graphicsengine.forms.gui.GUIActivationCallback;
import org.schema.schine.graphicsengine.forms.gui.GUICallback;
import org.schema.schine.graphicsengine.forms.gui.GUIElement;
import org.schema.schine.graphicsengine.forms.gui.newgui.GUIContentPane;
import org.schema.schine.graphicsengine.forms.gui.newgui.GUIHorizontalArea;
import org.schema.schine.graphicsengine.forms.gui.newgui.GUIHorizontalButtonTablePane;
import org.schema.schine.graphicsengine.forms.gui.newgui.GUIMainWindow;
import org.schema.schine.input.InputState;
import thederpgamer.inventorynetwork.data.stockmanager.StockManagerData;
import thederpgamer.inventorynetwork.data.stockmanager.StockManagerDataManager;

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
			StockManagerData data = StockManagerDataManager.getInstance().getFromSegmentPiece(segmentPiece);
			if(data != null) {
				GUIContentPane entriesPane = addTab(Lng.str("STOCK ENTRIES"));
				entriesPane.setTextBoxHeightLast(300);
				StockManagerEntryList entryList = new StockManagerEntryList(getState(), entriesPane.getContent(0), data);
				entryList.onInit();
				entriesPane.getContent(0).attach(entryList);
				entriesPane.addNewTextBox(30);
				GUIHorizontalButtonTablePane buttonPane = new GUIHorizontalButtonTablePane(getState(), 1, 1, entriesPane.getContent(1));
				buttonPane.onInit();
				buttonPane.addButton(0, 0, Lng.str("ADD ENTRY"), GUIHorizontalArea.HButtonColor.GREEN, new GUICallback() {
					@Override
					public void callback(GUIElement guiElement, MouseEvent mouseEvent) {
						if(mouseEvent.pressedLeftMouse()) {
							//Todo: Add entry dialog
						}
					}

					@Override
					public boolean isOccluded() {
						return false;
					}
				}, new GUIActivationCallback() {
					@Override
					public boolean isVisible(InputState inputState) {
						return true;
					}

					@Override
					public boolean isActive(InputState inputState) {
						return true;
					}
				});
			}
			setSelectedTab(lastTab);
		}
	}
}
