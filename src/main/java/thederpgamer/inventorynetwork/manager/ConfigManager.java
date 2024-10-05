package thederpgamer.inventorynetwork.manager;

import api.mod.config.FileConfiguration;
import thederpgamer.inventorynetwork.InventoryNetwork;

public class ConfigManager {

	private static FileConfiguration mainConfig;
	private static final String[] defaultMainConfig = {
			"debug-mode: false"
	};

	public static void initialize(InventoryNetwork instance) {
		mainConfig = instance.getConfig("config");
		mainConfig.saveDefault(defaultMainConfig);
	}

	public static FileConfiguration getMainConfig() {
		return mainConfig;
	}
}
