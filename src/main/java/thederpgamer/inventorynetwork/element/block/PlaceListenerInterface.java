package thederpgamer.inventorynetwork.element.block;

import api.listener.events.block.SegmentPieceAddByMetadataEvent;
import api.listener.events.block.SegmentPieceAddEvent;

/**
 * [Description]
 *
 * @author TheDerpGamer
 */
public interface PlaceListenerInterface {

	void onPlaceByPlayer(SegmentPieceAddEvent event);

	void onLoad(SegmentPieceAddByMetadataEvent event);
}
