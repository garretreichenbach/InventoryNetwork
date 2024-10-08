package thederpgamer.inventorynetwork.ui.stockmanager;

import api.utils.gui.SimplePlayerTextInput;
import org.schema.game.common.data.element.ElementKeyMap;
import org.schema.schine.common.language.Lng;
import org.schema.schine.graphicsengine.core.MouseEvent;
import org.schema.schine.graphicsengine.forms.gui.*;
import org.schema.schine.graphicsengine.forms.gui.newgui.*;
import org.schema.schine.input.InputState;
import thederpgamer.inventorynetwork.data.stockmanager.StockManagerData;
import thederpgamer.inventorynetwork.data.DataManager;
import thederpgamer.inventorynetwork.data.stockmanager.StockManagerDataManager;

import java.util.Collection;
import java.util.Comparator;
import java.util.Locale;
import java.util.Set;

/**
 * [Description]
 *
 * @author TheDerpGamer
 */
public class StockManagerEntryList extends ScrollableTableList<StockManagerData.StockManagerDataEntry> implements GUIActiveInterface {

	private final GUIElement element;
	private final StockManagerData data;

	public StockManagerEntryList(InputState state, GUIElement element, StockManagerData data) {
		super(state, 300, 300, element);
		this.element = element;
		this.data = data;
	}

	@Override
	protected Collection<StockManagerData.StockManagerDataEntry> getElementList() {
		return data.getData();
	}

	@Override
	public void initColumns() {
		addColumn("Priority", 3.0f, new Comparator<StockManagerData.StockManagerDataEntry>() {
			@Override
			public int compare(StockManagerData.StockManagerDataEntry o1, StockManagerData.StockManagerDataEntry o2) {
				return o1.getPriority() - o2.getPriority();
			}
		});
		addColumn("Type", 10.0f, new Comparator<StockManagerData.StockManagerDataEntry>() {
			@Override
			public int compare(StockManagerData.StockManagerDataEntry o1, StockManagerData.StockManagerDataEntry o2) {
				return o1.getType() - o2.getType();
			}
		});
		addColumn("Amount", 5.0f, new Comparator<StockManagerData.StockManagerDataEntry>() {
			@Override
			public int compare(StockManagerData.StockManagerDataEntry o1, StockManagerData.StockManagerDataEntry o2) {
				return 0;
			}
		});
		addColumn("Enabled", 3.0f, new Comparator<StockManagerData.StockManagerDataEntry>() {
			@Override
			public int compare(StockManagerData.StockManagerDataEntry o1, StockManagerData.StockManagerDataEntry o2) {
				return Boolean.compare(o1.getToggle(), o2.getToggle());
			}
		});
		addTextFilter(new GUIListFilterText<StockManagerData.StockManagerDataEntry>() {
			@Override
			public boolean isOk(String s, StockManagerData.StockManagerDataEntry entry) {
				return ElementKeyMap.getInfo(entry.getType()).getName().trim().toLowerCase(Locale.ROOT).contains(s.toLowerCase(Locale.ROOT).trim());
			}
		}, Lng.str("Search"), ControllerElement.FilterRowStyle.FULL);
		activeSortColumnIndex = 0;
	}

	@Override
	public void updateListEntries(GUIElementList guiElementList, Set<StockManagerData.StockManagerDataEntry> set) {
		guiElementList.deleteObservers();
		guiElementList.addObserver(this);
		for(StockManagerData.StockManagerDataEntry data : set) {
			if(data == null) continue;
			GUIClippedRow priorityRow = getSimpleRow(data.getPriority(), this);
			GUIClippedRow typeRow = getSimpleRow(ElementKeyMap.getInfo(data.getType()).getName(), this);
			GUIClippedRow amountRow = getSimpleRow(data.getMinStock(), this);
			GUIClippedRow toggleRow = getSimpleRow(data.getToggle(), this);
			StockManagerEntryListRow entryListRow = new StockManagerEntryListRow(getState(), data, priorityRow, typeRow, amountRow, toggleRow);
			GUIAncor anchor = new GUIAncor(getState(), element.getWidth() - 107.0f, 28.0f) {
				@Override
				public void draw() {
					setWidth(element.getWidth() - 107.0f);
					super.draw();
				}
			};
			GUIHorizontalButtonTablePane buttonTablePane = redrawButtonPane(data, anchor);
			anchor.attach(buttonTablePane);
			entryListRow.expanded = new GUIElementList(getState());
			entryListRow.expanded.add(new GUIListElement(anchor, getState()));
			entryListRow.expanded.attach(anchor);
			entryListRow.onInit();
			guiElementList.add(entryListRow);
		}
		guiElementList.updateDim();
	}

	private GUIHorizontalButtonTablePane redrawButtonPane(final StockManagerData.StockManagerDataEntry data, GUIAncor anchor) {
		GUIHorizontalButtonTablePane buttonPane = new GUIHorizontalButtonTablePane(getState(), 4, 1, anchor);
		buttonPane.onInit();
		buttonPane.addButton(0, 0, Lng.str("SET PRIORITY"), GUIHorizontalArea.HButtonColor.BLUE, new GUICallback() {
			@Override
			public void callback(GUIElement guiElement, MouseEvent mouseEvent) {
				if(mouseEvent.pressedLeftMouse()) {
					(new SimplePlayerTextInput("Input Priority", "Enter the priority of this item.\nItems with lower priority will be stocked first.") {
						@Override
						public boolean onInput(String s) {
							try {
								short priority = Short.parseShort(s);
								data.setPriority(priority);
								return true;
							} catch(Exception exception) {
								return false;
							}
						}
					}).activate();
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
		buttonPane.addButton(1, 0, Lng.str("SET AMOUNT"), GUIHorizontalArea.HButtonColor.BLUE, new GUICallback() {
			@Override
			public void callback(GUIElement guiElement, MouseEvent mouseEvent) {
				if(mouseEvent.pressedLeftMouse()) {
					(new SimplePlayerTextInput("Input Amount", "Enter the amount of items you want to keep in stock.\nSet to 0 to keep as much as possible.") {
						@Override
						public boolean onInput(String s) {
							try {
								int amount = Integer.parseInt(s);
								if(amount < 0) amount = 0; //<= 0 means stock as much as possible (infinite)
								data.setMinStock(amount);
								return true;
							} catch(Exception exception) {
								return false;
							}
						}
					}).activate();
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
		if(data.getToggle()) {
			buttonPane.addButton(2, 0, Lng.str("ENABLED"), GUIHorizontalArea.HButtonColor.GREEN, new GUICallback() {
				@Override
				public void callback(GUIElement guiElement, MouseEvent mouseEvent) {
					if(mouseEvent.pressedLeftMouse()) {
						data.setToggle(false);
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
		} else {
			buttonPane.addButton(2, 0, Lng.str("DISABLED"), GUIHorizontalArea.HButtonColor.RED, new GUICallback() {
				@Override
				public void callback(GUIElement guiElement, MouseEvent mouseEvent) {
					if(mouseEvent.pressedLeftMouse()) {
						data.setToggle(true);
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
		buttonPane.addButton(3, 0, Lng.str("REMOVE"), GUIHorizontalArea.HButtonColor.RED, new GUICallback() {
			@Override
			public void callback(GUIElement guiElement, MouseEvent mouseEvent) {
				if(mouseEvent.releasedLeftMouse()) {
					StockManagerDataManager.getInstance().sendPacket(data, DataManager.REMOVE_DATA, true);
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
		return buttonPane;
	}

	public class StockManagerEntryListRow extends ScrollableTableList<StockManagerData.StockManagerDataEntry>.Row {

		public StockManagerEntryListRow(InputState inputState, StockManagerData.StockManagerDataEntry entry, GUIElement... guiElements) {
			super(inputState, entry, guiElements);
			highlightSelect = true;
			highlightSelectSimple = true;
			setAllwaysOneSelected(true);
		}
	}
}
