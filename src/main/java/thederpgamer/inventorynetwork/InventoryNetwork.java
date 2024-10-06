package thederpgamer.inventorynetwork;

import api.config.BlockConfig;
import api.listener.events.controller.ClientInitializeEvent;
import api.listener.events.controller.ServerInitializeEvent;
import api.mod.StarMod;
import org.apache.commons.io.IOUtils;
import org.schema.schine.resource.ResourceLoader;
import thederpgamer.inventorynetwork.element.ElementManager;
import thederpgamer.inventorynetwork.element.block.systems.StockManager;
import thederpgamer.inventorynetwork.manager.*;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class InventoryNetwork extends StarMod {

	private static InventoryNetwork instance;
	private final String[] overwriteClasses = {};
	public InventoryNetwork() {
		instance = this;
	}
	public static InventoryNetwork getInstance() {
		return instance;
	}

	@Override
	public void onEnable() {
		super.onEnable();
		ConfigManager.initialize(this);
		EventManager.initialize(this);
		PacketManager.initialize();
	}

	@Override
	public void onServerCreated(ServerInitializeEvent event) {
		DataManager.initialize(false);
	}

	@Override
	public void onClientCreated(ClientInitializeEvent event) {
		DataManager.initialize(true);
	}

	@Override
	public void logInfo(String message) {
		System.out.println("[InventoryNetwork] [INFO]: " + message);
		super.logInfo(message);
	}

	@Override
	public void logWarning(String message) {
		System.out.println("[InventoryNetwork] [WARNING]: " + message);
		super.logWarning(message);
	}

	@Override
	public void logException(String message, Exception exception) {
		System.err.println("[InventoryNetwork] [EXCEPTION]: " + message + "\n" + exception.getMessage() + "\n" + Arrays.toString(exception.getStackTrace()));
		exception.printStackTrace();
		super.logException(message, exception);
	}

	@Override
	public void logFatal(String message, Exception exception) {
		System.err.println("[InventoryNetwork] [FATAL]: " + message + "\n" + exception.getMessage() + "\n" + Arrays.toString(exception.getStackTrace()));
		exception.printStackTrace();
		super.logFatal(message, exception);
	}

	@Override
	public byte[] onClassTransform(String className, byte[] byteCode) {
		for(String name : overwriteClasses) if(className.endsWith(name)) return overwriteClass(className, byteCode);
		return super.onClassTransform(className, byteCode);
	}

	@Override
	public void onResourceLoad(ResourceLoader loader) {
		ResourceManager.loadResources(this, loader);
	}

	@Override
	public void onBlockConfigLoad(BlockConfig config) {
		ElementManager.addBlock(new StockManager());
		ElementManager.initialize();
	}

	private byte[] overwriteClass(String className, byte[] byteCode) {
		byte[] bytes = null;
		try {
			ZipInputStream file = new ZipInputStream(Files.newInputStream(getSkeleton().getJarFile().toPath()));
			while(true) {
				ZipEntry nextEntry = file.getNextEntry();
				if(nextEntry == null) break;
				if(nextEntry.getName().endsWith(className + ".class")) bytes = IOUtils.toByteArray(file);
			}
			file.close();
		} catch(IOException exception) {
			exception.printStackTrace();
		}
		if(bytes != null) return bytes;
		else return byteCode;
	}
}
