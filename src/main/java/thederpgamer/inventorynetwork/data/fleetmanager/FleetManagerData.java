package thederpgamer.inventorynetwork.data.fleetmanager;

import api.network.PacketReadBuffer;
import api.network.PacketWriteBuffer;
import org.json.JSONObject;
import thederpgamer.inventorynetwork.data.SerializableData;

import java.io.IOException;
import java.util.UUID;

/**
 * [Description]
 *
 * @author TheDerpGamer
 */
public class FleetManagerData extends SerializableData {

	private final byte VERSION = 0;
	private long blockIndex;
	private int entityID;

	public FleetManagerData(long blockIndex, int entityID) {
		super(DataType.FLEET_MANAGER_DATA, UUID.randomUUID().toString());
		this.blockIndex = blockIndex;
		this.entityID = entityID;
	}

	public FleetManagerData(PacketReadBuffer readBuffer) throws IOException {
		super(readBuffer);
	}

	public FleetManagerData(JSONObject data) {
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
