package thederpgamer.inventorynetwork.data.stockmanager;

import api.network.PacketReadBuffer;
import api.network.PacketWriteBuffer;
import org.json.JSONArray;
import org.json.JSONObject;
import thederpgamer.inventorynetwork.data.SerializableData;
import thederpgamer.inventorynetwork.data.DataManager;

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
	private int entityID;
	private final Set<StockManagerDataEntry> data = new HashSet<>();

	public StockManagerData(long blockIndex, int entityID) {
		super(DataType.STOCK_MANAGER_DATA, java.util.UUID.randomUUID().toString());
		this.blockIndex = blockIndex;
		this.entityID = entityID;
	}

	@Override
	public JSONObject serialize() {
		JSONObject json = new JSONObject();
		json.put("version", VERSION);
		json.put("dataUUID", dataUUID);
		json.put("blockIndex", blockIndex);
		json.put("entityID", entityID);
		json.put("data", data.toArray(new StockManagerDataEntry[0]));
		return json;
	}

	@Override
	public void deserialize(JSONObject json) {
		byte version = (byte) json.getInt("version");
		dataUUID = json.getString("dataUUID");
		blockIndex = json.getLong("blockIndex");
		entityID = json.getInt("entityID");
		JSONArray dataArray = json.getJSONArray("data");
		for(int i = 0; i < dataArray.length(); i++) data.add(new StockManagerDataEntry(dataArray.getJSONObject(i)));
	}

	@Override
	public void serializeNetwork(PacketWriteBuffer writeBuffer) throws IOException {
		writeBuffer.writeString(dataUUID);
		writeBuffer.writeLong(blockIndex);
		writeBuffer.writeInt(entityID);
		writeBuffer.writeInt(data.size());
		for(StockManagerDataEntry entry : data) entry.serializeNetwork(writeBuffer);
	}

	@Override
	public void deserializeNetwork(PacketReadBuffer readBuffer) throws IOException {
		dataUUID = readBuffer.readString();
		blockIndex = readBuffer.readLong();
		entityID = readBuffer.readInt();
		int dataSize = readBuffer.readInt();
		for(int i = 0; i < dataSize; i++) data.add(new StockManagerDataEntry(readBuffer));
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

	public int getEntityID() {
		return entityID;
	}

	public Set<StockManagerDataEntry> getData() {
		return Collections.unmodifiableSet(data);
	}

	public void addData(StockManagerDataEntry entry) {
		data.add(entry);
		StockManagerDataManager.getInstance().sendPacket(this, DataManager.UPDATE_DATA, true);
	}

	public void removeData(StockManagerDataEntry entry) {
		data.remove(entry);
		StockManagerDataManager.getInstance().sendPacket(this, DataManager.UPDATE_DATA, true);
	}

	public static class StockManagerDataEntry extends SerializableData {

		private final byte VERSION = 0;
		private short priority;
		private int minStock;
		private int maxStock;
		private short type;
		private boolean toggle;

		public StockManagerDataEntry(int minStock, int maxStock, short type) {
			super(DataType.STOCK_MANAGER_DATA_ENTRY, java.util.UUID.randomUUID().toString());
			this.minStock = minStock;
			this.maxStock = maxStock;
			this.type = type;
			toggle = true;
			priority = 0;
		}

		public StockManagerDataEntry(JSONObject data) {
			super(DataType.STOCK_MANAGER_DATA_ENTRY, java.util.UUID.randomUUID().toString());
			deserialize(data);
		}

		public StockManagerDataEntry(PacketReadBuffer readBuffer) throws IOException {
			super(DataType.STOCK_MANAGER_DATA_ENTRY, java.util.UUID.randomUUID().toString());
			deserializeNetwork(readBuffer);
		}

		@Override
		public JSONObject serialize() {
			JSONObject json = new JSONObject();
			json.put("version", VERSION);
			json.put("dataUUID", dataUUID);
			json.put("priority", priority);
			json.put("minStock", minStock);
			json.put("maxStock", maxStock);
			json.put("type", type);
			json.put("toggle", toggle);
			return json;
		}

		@Override
		public void deserialize(JSONObject data) {
			byte version = (byte) data.getInt("version");
			dataUUID = data.getString("dataUUID");
			priority = (short) data.getInt("priority");
			minStock = data.getInt("minStock");
			maxStock = data.getInt("maxStock");
			type = (short) data.getInt("type");
			toggle = data.getBoolean("toggle");
		}

		@Override
		public void serializeNetwork(PacketWriteBuffer writeBuffer) throws IOException {
			writeBuffer.writeShort(priority);
			writeBuffer.writeInt(minStock);
			writeBuffer.writeInt(maxStock);
			writeBuffer.writeShort(type);
			writeBuffer.writeBoolean(toggle);
		}

		@Override
		public void deserializeNetwork(PacketReadBuffer readBuffer) throws IOException {
			priority = readBuffer.readShort();
			minStock = readBuffer.readInt();
			maxStock = readBuffer.readInt();
			type = readBuffer.readShort();
			toggle = readBuffer.readBoolean();
		}

		public int getPriority() {
			return priority;
		}

		public void setPriority(short priority) {
			this.priority = priority;
			StockManagerDataManager.getInstance().sendPacket(this, DataManager.UPDATE_DATA, true);
		}

		public int getMinStock() {
			return minStock;
		}

		public void setMinStock(int minStock) {
			this.minStock = minStock;
			StockManagerDataManager.getInstance().sendPacket(this, DataManager.UPDATE_DATA, true);
		}

		public int getMaxStock() {
			return maxStock;
		}

		public void setMaxStock(int maxStock) {
			this.maxStock = maxStock;
			StockManagerDataManager.getInstance().sendPacket(this, DataManager.UPDATE_DATA, true);
		}

		public short getType() {
			return type;
		}

		public boolean getToggle() {
			return toggle;
		}

		public void setToggle(boolean toggle) {
			this.toggle = toggle;
			StockManagerDataManager.getInstance().sendPacket(this, DataManager.UPDATE_DATA, true);
		}
	}
}
