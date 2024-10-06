package thederpgamer.inventorynetwork.data;

import api.network.PacketReadBuffer;
import api.network.PacketWriteBuffer;
import org.json.JSONArray;
import org.json.JSONObject;
import thederpgamer.inventorynetwork.manager.DataManager;
import thederpgamer.inventorynetwork.manager.StockDataManager;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * [Description]
 *
 * @author TheDerpGamer
 */
public class StockManagerData extends SerializableData {

	private final byte VERSION = 0;
	private long blockIndex;
	private long entityID;
	private final Set<StockManagerSerializableDataEntry> data = new HashSet<>();
	private boolean flagUpdate;

	public StockManagerData(long blockIndex, long entityID) {
		super(DataType.STOCK_MANAGER_DATA, java.util.UUID.randomUUID().toString());
		this.blockIndex = blockIndex;
		this.entityID = entityID;
		flagUpdate = true;
	}

	@Override
	public JSONObject serialize() {
		JSONObject json = new JSONObject();
		json.put("version", VERSION);
		json.put("dataUUID", dataUUID);
		json.put("blockIndex", blockIndex);
		json.put("entityID", entityID);
		json.put("data", data.toArray(new StockManagerSerializableDataEntry[0]));
		json.put("flagUpdate", flagUpdate);
		return json;
	}

	@Override
	public void deserialize(JSONObject json) {
		byte version = (byte) json.getInt("version");
		dataUUID = json.getString("dataUUID");
		blockIndex = json.getLong("blockIndex");
		entityID = json.getLong("entityID");
		JSONArray dataArray = json.getJSONArray("data");
		for(int i = 0; i < dataArray.length(); i++) data.add(new StockManagerSerializableDataEntry(dataArray.getJSONObject(i)));
		flagUpdate = json.getBoolean("flagUpdate");
	}

	@Override
	public void serializeNetwork(PacketWriteBuffer writeBuffer) throws IOException {
		writeBuffer.writeString(dataUUID);
		writeBuffer.writeLong(blockIndex);
		writeBuffer.writeLong(entityID);
		writeBuffer.writeInt(data.size());
		for(StockManagerSerializableDataEntry entry : data) entry.serializeNetwork(writeBuffer);
		writeBuffer.writeBoolean(flagUpdate);
	}

	@Override
	public void deserializeNetwork(PacketReadBuffer readBuffer) throws IOException {
		dataUUID = readBuffer.readString();
		blockIndex = readBuffer.readLong();
		entityID = readBuffer.readLong();
		int dataSize = readBuffer.readInt();
		for(int i = 0; i < dataSize; i++) data.add(new StockManagerSerializableDataEntry(readBuffer));
		flagUpdate = readBuffer.readBoolean();
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof StockManagerData) {
			StockManagerData data = (StockManagerData) obj;
			return data.dataUUID.equals(dataUUID);
		}
		return false;
	}

	public long getBlockIndex() {
		return blockIndex;
	}

	public long getEntityID() {
		return entityID;
	}

	public Set<StockManagerSerializableDataEntry> getData() {
		return Collections.unmodifiableSet(data);
	}

	public void addData(StockManagerSerializableDataEntry entry) {
		data.add(entry);
		flagUpdate = true;
	}

	public void removeData(StockManagerSerializableDataEntry entry) {
		data.remove(entry);
		flagUpdate = true;
	}

	public boolean needsUpdate() {
		return flagUpdate;
	}

	public static class StockManagerSerializableDataEntry extends SerializableData {

		private final byte VERSION = 0;
		private short priority;
		private int amount;
		private short type;
		private boolean toggle;

		public StockManagerSerializableDataEntry(int amount, short type) {
			super(DataType.STOCK_MANAGER_DATA_ENTRY, java.util.UUID.randomUUID().toString());
			this.amount = amount;
			this.type = type;
			toggle = true;
			priority = 0;
		}

		public StockManagerSerializableDataEntry(JSONObject data) {
			super(DataType.STOCK_MANAGER_DATA_ENTRY, java.util.UUID.randomUUID().toString());
			deserialize(data);
		}

		public StockManagerSerializableDataEntry(PacketReadBuffer readBuffer) throws IOException {
			super(DataType.STOCK_MANAGER_DATA_ENTRY, java.util.UUID.randomUUID().toString());
			deserializeNetwork(readBuffer);
		}

		@Override
		public JSONObject serialize() {
			JSONObject json = new JSONObject();
			json.put("version", VERSION);
			json.put("dataUUID", dataUUID);
			json.put("priority", priority);
			json.put("amount", amount);
			json.put("type", type);
			json.put("toggle", toggle);
			return json;
		}

		@Override
		public void deserialize(JSONObject data) {
			byte version = (byte) data.getInt("version");
			dataUUID = data.getString("dataUUID");
			priority = (short) data.getInt("priority");
			amount = data.getInt("amount");
			type = (short) data.getInt("type");
			toggle = data.getBoolean("toggle");
		}

		@Override
		public void serializeNetwork(PacketWriteBuffer writeBuffer) throws IOException {
			writeBuffer.writeShort(priority);
			writeBuffer.writeInt(amount);
			writeBuffer.writeShort(type);
			writeBuffer.writeBoolean(toggle);
		}

		@Override
		public void deserializeNetwork(PacketReadBuffer readBuffer) throws IOException {
			priority = readBuffer.readShort();
			amount = readBuffer.readInt();
			type = readBuffer.readShort();
			toggle = readBuffer.readBoolean();
		}

		@Override
		public boolean equals(Object obj) {
			if(obj instanceof StockManagerSerializableDataEntry) {
				StockManagerSerializableDataEntry data = (StockManagerSerializableDataEntry) obj;
				return data.dataUUID.equals(dataUUID);
			}
			return false;
		}

		public int getPriority() {
			return priority;
		}

		public void setPriority(short priority) {
			this.priority = priority;
			StockDataManager.getInstance().sendPacket(this, DataManager.UPDATE_DATA, true);
		}

		public int getAmount() {
			return amount;
		}

		public void setAmount(int amount) {
			this.amount = amount;
			StockDataManager.getInstance().sendPacket(this, DataManager.UPDATE_DATA, true);
		}

		public short getType() {
			return type;
		}

		public boolean getToggle() {
			return toggle;
		}

		public void setToggle(boolean toggle) {
			this.toggle = toggle;
			StockDataManager.getInstance().sendPacket(this, DataManager.UPDATE_DATA, true);
		}
	}
}
