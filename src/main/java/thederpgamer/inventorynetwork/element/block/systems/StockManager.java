package thederpgamer.inventorynetwork.element.block.systems;

import api.config.BlockConfig;
import api.listener.events.block.SegmentPieceActivateByPlayer;
import api.listener.events.block.SegmentPieceActivateEvent;
import api.listener.events.block.SegmentPieceAddByMetadataEvent;
import api.listener.events.block.SegmentPieceAddEvent;
import org.schema.game.common.data.SegmentPiece;
import org.schema.game.common.data.element.ElementKeyMap;
import org.schema.game.common.data.element.FactoryResource;
import org.schema.schine.graphicsengine.core.GraphicsContext;
import thederpgamer.inventorynetwork.InventoryNetwork;
import thederpgamer.inventorynetwork.data.SerializableData;
import thederpgamer.inventorynetwork.data.stockmanager.StockManagerData;
import thederpgamer.inventorynetwork.element.block.ActivationInterface;
import thederpgamer.inventorynetwork.element.block.Block;
import thederpgamer.inventorynetwork.element.block.SerializableDataInterface;
import thederpgamer.inventorynetwork.data.stockmanager.StockManagerDataManager;
import thederpgamer.inventorynetwork.ui.stockmanager.StockManagerDialog;

/**
 * [Description]
 *
 * @author TheDerpGamer
 */
public class StockManager extends Block implements ActivationInterface, SerializableDataInterface {

	public StockManager() {
		super("Stock Manager", ElementKeyMap.getInfo(120).getType());
	}

	@Override
	public void initialize() {
		blockInfo.setDescription("Stock Managers are used to automatically keep an amount of items in it's connected inventories.\n" +
				"If the amount of items in the connected inventories is below the set amount, the Stock Manager will automatically request them from any providers in it's assigned Inventory Network.\n" +
				"The Stock Manager's current stock levels can also be measured using a Sensor.\n\n" +
				"The Stock Manager's request system can be configured in a variety of different modes:\n" +
				" - Nearest: The Stock Manager will always request from the provider closest to it, regardless of priority.\n" +
				" - Priority: The Stock Manager will always request from the provider with the lowest priority, regardless of distance.\n" +
				" - Random: The Stock Manager will randomly select a provider to request from, regardless of priority or distance.\n" +
				" - Single Tag: The Stock Manager will only request from requesters that match at least one of the assigned tags.\n" +
				" - Multi Tag: The Stock Manager will only request from requesters that match all of the assigned tags.\n\n");
		blockInfo.setCanActivate(true);
		blockInfo.setInRecipe(true);
		blockInfo.setShoppable(true);
		blockInfo.setPrice(ElementKeyMap.getInfo(120).price);
		blockInfo.setOrientatable(true);
		blockInfo.setIndividualSides(ElementKeyMap.getInfo(120).getIndividualSides());
		blockInfo.setBlockStyle(ElementKeyMap.getInfo(120).getBlockStyle().id);
		blockInfo.controlling.addAll(ElementKeyMap.getInfo(120).controlling);
		blockInfo.controlledBy.addAll(ElementKeyMap.getInfo(120).controlledBy);
		for(short id : ElementKeyMap.getInfo(120).controlledBy) blockInfo.controlledBy.add(id);
		for(short id : ElementKeyMap.getInfo(120).controlling) blockInfo.controlling.add(id);
		if(GraphicsContext.initialized) {
			try {
				//Todo: Textures and icon
			} catch(Exception exception) {
				InventoryNetwork.getInstance().logException("Failed to set build icon for Stock Manager", exception);
			}
		}
		BlockConfig.addRecipe(blockInfo, ElementKeyMap.getInfo(120).getProducedInFactoryType(), (int) ElementKeyMap.getInfo(120).getFactoryBakeTime(), new FactoryResource(1, (short) 120), new FactoryResource(1, (short) 976));
		BlockConfig.add(blockInfo);
	}


	@Override
	public void onPlayerActivation(SegmentPieceActivateByPlayer event) {
		(new StockManagerDialog(event.getSegmentPiece())).activate();
	}

	@Override
	public void onLogicActivation(SegmentPieceActivateEvent event) {

	}

	@Override
	public SerializableData getDataFromSegmentPiece(SegmentPiece segmentPiece) {
		return StockManagerDataManager.getInstance().getFromSegmentPiece(segmentPiece);
	}

	@Override
	public SerializableData getDataFromUUID(String uuid, boolean server) {
		return StockManagerDataManager.getInstance().getFromUUID(uuid, server);
	}

	@Override
	public void onPlaceByPlayer(SegmentPieceAddEvent event) {
		StockManagerDataManager.getInstance().addToServerCache(new StockManagerData(event.getAbsIndex(), event.getSegmentController().getId()));
	}

	@Override
	public void onLoad(SegmentPieceAddByMetadataEvent event) {
//		StockDataManager.getInstance().addToServerCache(new StockManagerData(event.getAbsIndex(), event.getSegment().getSegmentController().dbId));
	}
}
