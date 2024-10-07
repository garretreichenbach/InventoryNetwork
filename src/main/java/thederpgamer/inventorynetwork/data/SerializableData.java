package thederpgamer.inventorynetwork.data;

import api.network.PacketReadBuffer;
import api.network.PacketWriteBuffer;
import org.json.JSONObject;
import thederpgamer.inventorynetwork.data.fleetmanager.FleetManagerData;
import thederpgamer.inventorynetwork.data.inventorynetwork.InventoryNetworkData;
import thederpgamer.inventorynetwork.data.stockmanager.StockManagerData;
import thederpgamer.inventorynetwork.data.supplier.SupplierData;

import java.io.IOException;

/**
 * [Description]
 *
 * @author TheDerpGamer
 */
public abstract class SerializableData {

	public enum DataType {
		INVENTORY_NETWORK_DATA(InventoryNetworkData.class),
		SUPPLIER_DATA(SupplierData.class),
		STOCK_MANAGER_DATA(StockManagerData.class),
		STOCK_MANAGER_DATA_ENTRY(StockManagerData.StockManagerSerializableDataEntry.class),
		FLEET_MANAGER_DATA(FleetManagerData.class);

		private final Class<? extends SerializableData> dataClass;

		DataType(Class<? extends SerializableData> dataClass) {
			this.dataClass = dataClass;
		}

		public Class<? extends SerializableData> getDataClass() {
			return dataClass;
		}
	}

	protected String dataUUID;
	protected DataType dataType;

	protected SerializableData(DataType dataType, String dataUUID) {
		this.dataType = dataType;
		this.dataUUID = dataUUID;
	}

	protected SerializableData(PacketReadBuffer readBuffer) throws IOException {
		deserializeNetwork(readBuffer);
	}

	protected SerializableData(JSONObject data) {
		deserialize(data);
	}

	public boolean equals(Object obj) {
		return obj.getClass() == getClass() && ((SerializableData) obj).dataUUID.equals(dataUUID);
	}

	public String getUUID() {
		return dataUUID;
	}

	public DataType getDataType() {
		return dataType;
	}

	public abstract JSONObject serialize();

	public abstract void deserialize(JSONObject data);

	public abstract void serializeNetwork(PacketWriteBuffer writeBuffer) throws IOException;

	public abstract void deserializeNetwork(PacketReadBuffer readBuffer) throws IOException;
}
