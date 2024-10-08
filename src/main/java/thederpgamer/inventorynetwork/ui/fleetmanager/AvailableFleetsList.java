package thederpgamer.inventorynetwork.ui.fleetmanager;

import org.schema.game.common.data.fleet.Fleet;
import org.schema.schine.graphicsengine.forms.gui.GUIElement;
import org.schema.schine.graphicsengine.forms.gui.GUIElementList;
import org.schema.schine.graphicsengine.forms.gui.newgui.ScrollableTableList;
import org.schema.schine.input.InputState;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

/**
 * [Description]
 *
 * @author TheDerpGamer
 */
public class AvailableFleetsList extends ScrollableTableList<Fleet> {

	private final GUIElement anchor;

	public AvailableFleetsList(InputState state, GUIElement anchor) {
		super(state, 300, 300, anchor);
		this.anchor = anchor;
	}

	@Override
	public void initColumns() {

	}

	@Override
	protected Collection<Fleet> getElementList() {
		return Collections.emptyList();
	}

	@Override
	public void updateListEntries(GUIElementList guiElementList, Set<Fleet> set) {

	}
}
