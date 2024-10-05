package thederpgamer.inventorynetwork.data;

import org.json.JSONArray;
import org.json.JSONObject;

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
	private final Set<StockManagerSerializableDataEntry> data = new HashSet<>();
	private boolean flagUpdate;

	public StockManagerData(long blockIndex) {
		super(java.util.UUID.randomUUID().toString());
		this.blockIndex = blockIndex;
		flagUpdate = true;
	}

	@Override
	public JSONObject serialize() {
		JSONObject json = new JSONObject();
		json.put("version", VERSION);
		json.put("dataUUID", dataUUID);
		json.put("blockIndex", blockIndex);
		json.put("data", data.toArray(new StockManagerSerializableDataEntry[0]));
		json.put("flagUpdate", flagUpdate);
		return json;
	}

	@Override
	public void deserialize(JSONObject json) {
		byte version = (byte) json.getInt("version");
		dataUUID = json.getString("dataUUID");
		blockIndex = json.getLong("blockIndex");
		JSONArray dataArray = json.getJSONArray("data");
		for(int i = 0; i < dataArray.length(); i++) data.add(new StockManagerSerializableDataEntry(dataArray.getJSONObject(i)));
		flagUpdate = json.getBoolean("flagUpdate");
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
		private int priority;
		private int amount;
		private short type;
		private final Set<String> pullFrom = new HashSet<>();

		public StockManagerSerializableDataEntry(int amount, short type) {
			super(java.util.UUID.randomUUID().toString());
			this.amount = amount;
			this.type = type;
			priority = 0;
		}

		@Override
		public JSONObject serialize() {
			JSONObject json = new JSONObject();
			json.put("version", VERSION);
			json.put("dataUUID", dataUUID);
			json.put("priority", priority);
			json.put("amount", amount);
			json.put("type", type);
			json.put("pullFrom", pullFrom.toArray(new String[0]));
			return json;
		}

		@Override
		public void deserialize(JSONObject data) {
			byte version = (byte) data.getInt("version");
			dataUUID = data.getString("dataUUID");
			priority = data.getInt("priority");
			amount = data.getInt("amount");
			type = (short) data.getInt("type");
			JSONArray pullFromArray = data.getJSONArray("pullFrom");
			for(int i = 0; i < pullFromArray.length(); i++) pullFrom.add(pullFromArray.getString(i));
		}

		@Override
		public boolean equals(Object obj) {
			if(obj instanceof StockManagerSerializableDataEntry) {
				StockManagerSerializableDataEntry data = (StockManagerSerializableDataEntry) obj;
				return data.dataUUID.equals(dataUUID);
			}
			return false;
		}

		@Override
		public String getUUID() {
			return dataUUID;
		}
	}
}
