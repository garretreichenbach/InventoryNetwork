package thederpgamer.inventorynetwork.data.job;

import api.network.PacketReadBuffer;
import api.network.PacketWriteBuffer;
import org.json.JSONObject;
import org.schema.common.util.linAlg.Vector3i;
import org.schema.game.common.data.fleet.Fleet;
import thederpgamer.inventorynetwork.data.SerializableData;
import thederpgamer.inventorynetwork.data.fleets.fleetdeliverytarget.FleetDeliveryTargetData;
import thederpgamer.inventorynetwork.data.fleets.fleetdeliverytarget.FleetDeliveryTargetDataManager;
import thederpgamer.inventorynetwork.utils.EntityUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

/**
 * [Description]
 *
 * @author TheDerpGamer
 */
public class JobData extends SerializableData {

	private final byte VERSION = 0;
	private long fleetId;
	private String targetDataUUID;
	private float totalVolume;
	private final HashMap<Short, Integer> cargo = new HashMap<>();

	public JobData(long fleetId, String fleetName, String targetDataUUID, float totalVolume, HashMap<Short, Integer> cargo) {
		super(DataType.FLEET_DELIVERY_JOB_DATA, UUID.randomUUID().toString());
		this.fleetId = fleetId;
		this.fleetName = fleetName;
		this.targetDataUUID = targetDataUUID;
		this.totalVolume = totalVolume;
		this.cargo.putAll(cargo);
	}

	public JobData(PacketReadBuffer readBuffer) throws IOException {
		super(readBuffer);
	}

	public JobData(JSONObject data) {
		super(data);
	}

	@Override
	public JSONObject serialize() {
		JSONObject data = new JSONObject();
		data.put("version", VERSION);
		data.put("dataUUID", dataUUID);
		data.put("fleetId", fleetId);
		data.put("fleetName", fleetName);
		data.put("targetDataUUID", targetDataUUID);
		data.put("totalVolume", totalVolume);
		JSONObject cargoData = new JSONObject();
		for(Short key : cargo.keySet()) cargoData.put(String.valueOf(key), cargo.get(key));
		data.put("cargo", cargoData);
		return data;
	}

	@Override
	public void deserialize(JSONObject data) {
		byte version = (byte) data.getInt("version");
		dataUUID = data.getString("dataUUID");
		fleetId = data.getLong("fleetId");
		fleetName = data.getString("fleetName");
		targetDataUUID = data.getString("targetDataUUID");
		totalVolume = (float) data.getDouble("totalVolume");
		JSONObject cargoData = data.getJSONObject("cargo");
		for(Object key : cargoData.keySet()) cargo.put(Short.parseShort((String) key), cargoData.getInt((String) key));
	}

	@Override
	public void serializeNetwork(PacketWriteBuffer writeBuffer) throws IOException {
		writeBuffer.writeByte(VERSION);
		writeBuffer.writeString(dataUUID);
		writeBuffer.writeLong(fleetId);
		writeBuffer.writeString(fleetName);
		writeBuffer.writeString(targetDataUUID);
		writeBuffer.writeFloat(totalVolume);
		JSONObject cargoData = new JSONObject();
		for(Short key : cargo.keySet()) cargoData.put(String.valueOf(key), cargo.get(key));
		writeBuffer.writeString(cargoData.toString());
	}

	@Override
	public void deserializeNetwork(PacketReadBuffer readBuffer) throws IOException {
		byte version = readBuffer.readByte();
		dataUUID = readBuffer.readString();
		fleetId = readBuffer.readLong();
		fleetName = readBuffer.readString();
		targetDataUUID = readBuffer.readString();
		totalVolume = readBuffer.readFloat();
		JSONObject cargoData = new JSONObject(readBuffer.readString());
		for(Object key : cargoData.keySet()) cargo.put(Short.parseShort((String) key), cargoData.getInt((String) key));
	}

	public long getFleetId() {
		return fleetId;
	}

	public String getFleetName() {
		return fleetName;
	}

	public Fleet getFleet(boolean server) {
		return EntityUtils.getFleetByID(fleetId, server);
	}

	public Vector3i getTargetSector(boolean server) {
		return getTargetData(server).getTargetSector();
	}

	public FleetDeliveryTargetData getTargetData(boolean server) {
		return FleetDeliveryTargetDataManager.getInstance().getFromUUID(targetDataUUID, server);
	}

	public int getDistance(boolean server) {
		Vector3i current = Vector3i.parseVector3iFree(getFleet(server).getFlagShipSector());
		Vector3i target = getTargetSector(server);
		Vector3i diff = new Vector3i(target.x - current.x, target.y - current.y, target.z - current.z);
		return (int) Math.sqrt(diff.x * diff.x + diff.y * diff.y + diff.z * diff.z);
	}

	public float getTotalVolume() {
		return totalVolume;
	}

	public HashMap<Short, Integer> getCargo() {
		return cargo;
	}
}
