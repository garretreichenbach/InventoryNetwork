package thederpgamer.inventorynetwork.element;

import api.config.BlockConfig;
import org.apache.commons.lang3.StringUtils;
import org.schema.game.common.data.element.ElementCategory;
import org.schema.game.common.data.element.ElementInformation;
import org.schema.game.common.data.element.ElementKeyMap;
import thederpgamer.inventorynetwork.element.block.Block;
import thederpgamer.inventorynetwork.element.items.Item;

import java.util.HashSet;
import java.util.Set;

public class ElementManager {

	public enum FactoryType {NONE, CAPSULE_REFINERY, MICRO_ASSEMBLER, BASIC_FACTORY, STANDARD_FACTORY, ADVANCED_FACTORY}

	private static final Set<Block> blockList = new HashSet<>();
	private static final Set<Item> itemList = new HashSet<>();

	public static void initialize() {
		for(Block blockElement : blockList) blockElement.initialize();
		for(Item item : itemList) item.initialize();
	}

	public static Set<Block> getAllBlocks() {
		return blockList;
	}

	public static Set<Item> getAllItems() {
		return itemList;
	}

	public static Block getBlock(short id) {
		for(Block blockElement : blockList) if(blockElement.getBlockInfo().getId() == id) return blockElement;
		return null;
	}

	public static Block getBlock(String blockName) {
		for(Block block : blockList) {
			if(block.getBlockInfo().getName().equalsIgnoreCase(blockName)) return block;
		}
		return null;
	}

	public static Item getItem(short itemId) {
		for(Item item : itemList) {
			if(item.getItemInfo().getId() == itemId) return item;
		}
		return null;
	}

	public static Item getItem(String itemName) {
		for(Item item : itemList) {
			if(item.getItemInfo().getName().equalsIgnoreCase(itemName)) return item;
		}
		return null;
	}

	public static void addBlock(Block block) {
		blockList.add(block);
	}

	public static void addItem(Item item) {
		itemList.add(item);
	}

	public static ElementCategory getCategory(String path) {
		String[] split = path.split("\\.");
		ElementCategory category = ElementKeyMap.getCategoryHirarchy();
		for(String s : split) {
			boolean createNew = false;
			if(category.hasChildren()) {
				for(ElementCategory child : category.getChildren()) {
					if(child.getCategory().equalsIgnoreCase(s)) {
						category = child;
						break;
					}
					createNew = true;
				}
			} else createNew = true;
			if(createNew) category = BlockConfig.newElementCategory(category, StringUtils.capitalize(s));
		}
		return category;
	}

	public static ElementInformation getInfo(String name) {
		Block block = getBlock(name);
		if(block != null) return block.getBlockInfo();
		else {
			Item item = getItem(name);
			if(item != null) return item.getItemInfo();
			else {
				for(ElementInformation elementInfo : ElementKeyMap.getInfoArray()) {
					if(elementInfo.getName().equalsIgnoreCase(name)) return elementInfo;
				}
			}
			return null;
		}
	}
}
