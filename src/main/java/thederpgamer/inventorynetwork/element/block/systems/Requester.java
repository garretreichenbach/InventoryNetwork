package thederpgamer.inventorynetwork.element.block.systems;

import api.listener.events.block.SegmentPieceActivateByPlayer;
import api.listener.events.block.SegmentPieceActivateEvent;
import api.listener.events.block.SegmentPieceAddByMetadataEvent;
import api.listener.events.block.SegmentPieceAddEvent;
import org.schema.game.common.data.SegmentPiece;
import org.schema.game.common.data.element.ElementKeyMap;
import thederpgamer.inventorynetwork.data.SerializableData;
import thederpgamer.inventorynetwork.element.block.ActivationInterface;
import thederpgamer.inventorynetwork.element.block.Block;
import thederpgamer.inventorynetwork.element.block.SerializableDataInterface;

/**
 * [Description]
 *
 * @author TheDerpGamer
 */
public class Requester extends Block implements ActivationInterface, SerializableDataInterface {

	public Requester() {
		super("Requester", ElementKeyMap.getInfo(120).getType());
	}

	@Override
	public void initialize() {

	}

	@Override
	public void onPlayerActivation(SegmentPieceActivateByPlayer event) {

	}

	@Override
	public void onLogicActivation(SegmentPieceActivateEvent event) {

	}

	@Override
	public SerializableData getDataFromSegmentPiece(SegmentPiece segmentPiece) {
		return null;
	}

	@Override
	public SerializableData getDataFromUUID(String uuid) {
		return null;
	}

	@Override
	public void onPlaceByPlayer(SegmentPieceAddEvent event) {

	}

	@Override
	public void onLoad(SegmentPieceAddByMetadataEvent event) {

	}
}
