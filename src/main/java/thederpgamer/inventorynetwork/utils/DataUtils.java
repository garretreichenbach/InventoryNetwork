package thederpgamer.inventorynetwork.utils;

import api.common.GameCommon;
import thederpgamer.inventorynetwork.InventoryNetwork;

public class DataUtils {

	public static String getWorldDataPath() {
		return getResourcesPath() + "/data/" + GameCommon.getUniqueContextId();
	}

	public static String getResourcesPath() {
		return InventoryNetwork.getInstance().getSkeleton().getResourcesFolder().getPath().replace('\\', '/');
	}
}
