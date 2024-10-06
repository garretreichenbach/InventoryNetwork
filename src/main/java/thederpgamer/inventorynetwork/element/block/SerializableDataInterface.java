package thederpgamer.inventorynetwork.element.block;

import org.schema.game.common.data.SegmentPiece;
import thederpgamer.inventorynetwork.data.SerializableData;

/**
 * [Description]
 *
 * @author TheDerpGamer
 */
public interface SerializableDataInterface extends PlaceListenerInterface {

	SerializableData getDataFromSegmentPiece(SegmentPiece segmentPiece);

	SerializableData getDataFromUUID(String uuid);
}
