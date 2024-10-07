package thederpgamer.inventorynetwork.data.inventorynetwork;

import api.network.PacketReadBuffer;
import api.network.PacketWriteBuffer;
import org.json.JSONObject;
import thederpgamer.inventorynetwork.data.SerializableData;

import java.io.IOException;
import java.util.UUID;

/**
 * The Inventory Network Node is the core of an Inventory Network.
 * </br>It is responsible for managing the various systems of the network and functionality between them.
 *
 * @author TheDerpGamer
 */
public class InventoryNetworkData extends SerializableData {

	private final byte VERSION = 0;
	private long blockIndex;
	private int entityID;

	protected InventoryNetworkData(long blockIndex, int entityID) {
		super(DataType.INVENTORY_NETWORK_DATA, UUID.randomUUID().toString());
		this.blockIndex = blockIndex;
		this.entityID = entityID;
	}

	protected InventoryNetworkData(PacketReadBuffer readBuffer) throws IOException {
		super(readBuffer);
	}

	protected InventoryNetworkData(JSONObject data) {
		super(data);
	}

	@Override
	public JSONObject serialize() {
		JSONObject json = new JSONObject();
		json.put("version", VERSION);
		json.put("dataUUID", dataUUID);
		json.put("blockIndex", blockIndex);
		json.put("entityID", entityID);
		return json;
	}

	@Override
	public void deserialize(JSONObject data) {
		byte version = (byte) data.getInt("version");
		dataUUID = data.getString("dataUUID");
		blockIndex = data.getLong("blockIndex");
		entityID = data.getInt("entityID");
	}

	@Override
	public void serializeNetwork(PacketWriteBuffer writeBuffer) throws IOException {
		writeBuffer.writeByte(VERSION);
		writeBuffer.writeString(dataUUID);
		writeBuffer.writeLong(blockIndex);
		writeBuffer.writeInt(entityID);
	}

	@Override
	public void deserializeNetwork(PacketReadBuffer readBuffer) throws IOException {
		byte version = readBuffer.readByte();
		dataUUID = readBuffer.readString();
		blockIndex = readBuffer.readLong();
		entityID = readBuffer.readInt();
	}

	public long getBlockIndex() {
		return blockIndex;
	}

	public int getEntityID() {
		return entityID;
	}
}
