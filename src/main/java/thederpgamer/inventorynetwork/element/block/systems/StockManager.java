package thederpgamer.inventorynetwork.element.block.systems;

import api.config.BlockConfig;
import api.listener.events.block.SegmentPieceActivateByPlayer;
import api.listener.events.block.SegmentPieceActivateEvent;
import org.schema.game.common.data.element.ElementKeyMap;
import org.schema.game.common.data.element.FactoryResource;
import org.schema.schine.graphicsengine.core.GraphicsContext;
import thederpgamer.inventorynetwork.InventoryNetwork;
import thederpgamer.inventorynetwork.element.block.ActivationInterface;
import thederpgamer.inventorynetwork.element.block.Block;
import thederpgamer.inventorynetwork.manager.ResourceManager;
import thederpgamer.inventorynetwork.ui.stockmanager.StockManagerDialog;

/**
 * [Description]
 *
 * @author TheDerpGamer
 */
public class StockManager extends Block implements ActivationInterface {

	public StockManager() {
		super("Stock Manager", ElementKeyMap.getInfo(120).getType());
	}

	@Override
	public void initialize() {
		blockInfo.setDescription("");
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
				blockInfo.setBuildIconNum(ResourceManager.getTexture("stock_manager_icon").getTextureId());
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
}
