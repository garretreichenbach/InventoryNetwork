package thederpgamer.inventorynetwork.utils;

import org.schema.game.common.controller.Planet;
import org.schema.game.common.controller.SegmentController;
import org.schema.game.common.controller.Ship;
import org.schema.game.common.controller.SpaceStation;
import org.schema.game.common.controller.elements.ManagerContainer;

/**
 * <Description>
 *
 * @author TheDerpGamer
 * @since 08/13/2021
 */
public class EntityUtils {

	public static ManagerContainer<?> getManagerContainer(SegmentController segmentController) {
		switch(segmentController.getType()) {
			case SHIP:
				return ((Ship) segmentController).getManagerContainer();
			case SPACE_STATION:
				return ((SpaceStation) segmentController).getManagerContainer();
			case PLANET_SEGMENT:
				return ((Planet) segmentController).getManagerContainer();
			default:
				return null;
		}
	}
}
