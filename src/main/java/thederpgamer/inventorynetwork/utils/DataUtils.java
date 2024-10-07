package thederpgamer.inventorynetwork.utils;

import api.common.GameCommon;
import thederpgamer.inventorynetwork.InventoryNetwork;

import java.lang.reflect.Field;

public class DataUtils {

	public static String getWorldDataPath() {
		return getResourcesPath() + "/data/" + GameCommon.getUniqueContextId();
	}

	public static String getResourcesPath() {
		return InventoryNetwork.getInstance().getSkeleton().getResourcesFolder().getPath().replace('\\', '/');
	}

	public static Object getPrivateField(Object object, String field) {
		try {
			Field f = object.getClass().getDeclaredField(field);
			f.setAccessible(true);
			return f.get(object);
		} catch(Exception exception) {
			InventoryNetwork.getInstance().logException("Failed to get private field: " + field, exception);
			return null;
		}
	}
}
