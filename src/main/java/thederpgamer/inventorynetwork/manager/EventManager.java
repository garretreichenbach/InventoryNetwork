package thederpgamer.inventorynetwork.manager;

import api.listener.Listener;
import api.listener.events.block.SegmentPieceActivateByPlayer;
import api.listener.events.block.SegmentPieceAddByMetadataEvent;
import api.listener.events.block.SegmentPieceAddEvent;
import api.mod.StarLoader;
import thederpgamer.inventorynetwork.InventoryNetwork;
import thederpgamer.inventorynetwork.element.ElementManager;
import thederpgamer.inventorynetwork.element.block.ActivationInterface;
import thederpgamer.inventorynetwork.element.block.Block;
import thederpgamer.inventorynetwork.element.block.PlaceListenerInterface;

public class EventManager {

	public static void initialize(InventoryNetwork instance) {
		StarLoader.registerListener(SegmentPieceActivateByPlayer.class, new Listener<SegmentPieceActivateByPlayer>() {
			@Override
			public void onEvent(SegmentPieceActivateByPlayer event) {
				for(Block block : ElementManager.getAllBlocks()) {
					if(block instanceof ActivationInterface && block.getId() == event.getSegmentPiece().getType()) {
						((ActivationInterface) block).onPlayerActivation(event);
						return;
					}
				}
			}
		}, instance);

		StarLoader.registerListener(SegmentPieceAddEvent.class, new Listener<SegmentPieceAddEvent>() {
			@Override
			public void onEvent(SegmentPieceAddEvent event) {
				for(Block block : ElementManager.getAllBlocks()) {
					if(block instanceof PlaceListenerInterface) {
						((PlaceListenerInterface) block).onPlaceByPlayer(event);
					}
				}
			}
		}, instance);

		StarLoader.registerListener(SegmentPieceAddByMetadataEvent.class, new Listener<SegmentPieceAddByMetadataEvent>() {
			@Override
			public void onEvent(SegmentPieceAddByMetadataEvent event) {
				for(Block block : ElementManager.getAllBlocks()) {
					if(block instanceof PlaceListenerInterface) {
						((PlaceListenerInterface) block).onLoad(event);
					}
				}
			}
		}, instance);
	}
}
