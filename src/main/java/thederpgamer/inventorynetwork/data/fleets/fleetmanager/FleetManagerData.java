package thederpgamer.inventorynetwork.data.fleets.fleetmanager;

import api.network.PacketReadBuffer;
import api.network.PacketWriteBuffer;
import org.json.JSONArray;
import org.json.JSONObject;
import org.schema.game.common.data.fleet.Fleet;
import org.schema.game.network.objects.remote.FleetCommand;
import thederpgamer.inventorynetwork.InventoryNetwork;
import thederpgamer.inventorynetwork.data.SerializableData;
import thederpgamer.inventorynetwork.data.job.JobData;
import thederpgamer.inventorynetwork.utils.EntityUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
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
	private Set<FleetManagerDataEntry> managedFleets = new HashSet<>();
	private Set<JobData> deliveryJobs = new HashSet<>();

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
		JSONArray fleetArray = new JSONArray();
		for(FleetManagerDataEntry entry : managedFleets) fleetArray.put(entry.serialize());
		json.put("managedFleets", fleetArray);
		JSONArray deliveryArray = new JSONArray();
		for(JobData entry : deliveryJobs) deliveryArray.put(entry.serialize());
		json.put("deliveryJobs", deliveryArray);
		return json;
	}

	@Override
	public void deserialize(JSONObject data) {
		byte version = (byte) data.getInt("version");
		dataUUID = data.getString("dataUUID");
		blockIndex = data.getLong("blockIndex");
		entityID = data.getInt("entityID");
		managedFleets.clear();
		JSONArray fleetArray = data.getJSONArray("managedFleets");
		for(int i = 0; i < fleetArray.length(); i ++) managedFleets.add(new FleetManagerDataEntry(fleetArray.getJSONObject(i)));
		deliveryJobs.clear();
		JSONArray deliveryArray = data.getJSONArray("deliveryJobs");
		for(int i = 0; i < deliveryArray.length(); i ++) deliveryJobs.add(new JobData(deliveryArray.getJSONObject(i)));
	}

	@Override
	public void serializeNetwork(PacketWriteBuffer writeBuffer) throws IOException {
		writeBuffer.writeByte(VERSION);
		writeBuffer.writeString(dataUUID);
		writeBuffer.writeLong(blockIndex);
		writeBuffer.writeInt(entityID);
		writeBuffer.writeShort((short) managedFleets.size());
		for(FleetManagerDataEntry entry : managedFleets) entry.serializeNetwork(writeBuffer);
		writeBuffer.writeShort((short) deliveryJobs.size());
		for(JobData entry : deliveryJobs) entry.serializeNetwork(writeBuffer);
	}

	@Override
	public void deserializeNetwork(PacketReadBuffer readBuffer) throws IOException {
		byte version = readBuffer.readByte();
		dataUUID = readBuffer.readString();
		blockIndex = readBuffer.readLong();
		entityID = readBuffer.readInt();
		managedFleets.clear();
		int fleetCount = readBuffer.readShort();
		for(int i = 0; i < fleetCount; i ++) managedFleets.add(new FleetManagerDataEntry(readBuffer));
		deliveryJobs.clear();
		int deliveryCount = readBuffer.readShort();
		for(int i = 0; i < deliveryCount; i ++) deliveryJobs.add(new JobData(readBuffer));
	}

	public long getBlockIndex() {
		return blockIndex;
	}

	public int getEntityID() {
		return entityID;
	}

	public Set<FleetManagerDataEntry> getFleets() {
		return Collections.unmodifiableSet(managedFleets);
	}

	public void addFleet(FleetManagerDataEntry fleet, boolean server) {
		managedFleets.add(fleet);
		FleetManagerDataManager.getInstance().updateData(this, server);
	}

	public void removeFleet(FleetManagerDataEntry fleet, boolean server) {
		managedFleets.remove(fleet);
		FleetManagerDataManager.getInstance().updateData(this, server);
	}

	public void updateFleet(FleetManagerDataEntry fleet, boolean server) {
		managedFleets.remove(fleet);
		managedFleets.add(fleet);
		FleetManagerDataManager.getInstance().updateData(this, server);
	}

	public Set<JobData> getDeliveryJobs() {
		return Collections.unmodifiableSet(deliveryJobs);
	}

	public void addDeliveryJob(JobData deliveryJob, boolean server) {
		deliveryJobs.add(deliveryJob);
		FleetManagerDataManager.getInstance().updateData(this, server);
	}

	public void removeDeliveryJob(JobData deliveryJob, boolean server) {
		deliveryJobs.remove(deliveryJob);
		FleetManagerDataManager.getInstance().updateData(this, server);
	}

	public void updateDeliveryJob(JobData deliveryJob, boolean server) {
		deliveryJobs.remove(deliveryJob);
		deliveryJobs.add(deliveryJob);
		FleetManagerDataManager.getInstance().updateData(this, server);
	}

	public byte[] getCommand(long fleetID) {
		for(FleetManagerDataEntry entry : managedFleets) {
			if(entry.getFleetID() == fleetID) return entry.getCommand();
		}
		return null;
	}

	public void setCommand(long fleetID, byte[] command) {
		for(FleetManagerDataEntry entry : managedFleets) {
			if(entry.getFleetID() == fleetID) {
				entry.setCommand(command);
				FleetManagerDataManager.getInstance().updateData(this, true);
				return;
			}
		}
	}

	public void recheckFleetCommands() {
		for(FleetManagerDataEntry entry : managedFleets) entry.recheckCommand();
	}

	public static class FleetManagerDataEntry extends SerializableData {

		private final byte VERSION = 0;
		private long fleetID;
		private byte[] command;

		public FleetManagerDataEntry(long fleetID, byte[] command) {
			super(DataType.FLEET_MANAGER_DATA_ENTRY, UUID.randomUUID().toString());
			this.fleetID = fleetID;
			this.command = command;
		}

		public FleetManagerDataEntry(PacketReadBuffer readBuffer) throws IOException {
			super(readBuffer);
		}

		public FleetManagerDataEntry(JSONObject data) {
			super(data);
		}

		@Override
		public JSONObject serialize() {
			JSONObject json = new JSONObject();
			json.put("version", VERSION);
			json.put("dataUUID", dataUUID);
			json.put("fleetID", fleetID);
			json.put("command", command);
			return json;
		}

		@Override
		public void deserialize(JSONObject data) {
			byte version = (byte) data.getInt("version");
			dataUUID = data.getString("dataUUID");
			fleetID = data.getLong("fleetID");
			command = data.getString("command").getBytes(StandardCharsets.UTF_8);
		}

		@Override
		public void serializeNetwork(PacketWriteBuffer writeBuffer) throws IOException {
			writeBuffer.writeByte(VERSION);
			writeBuffer.writeString(dataUUID);
			writeBuffer.writeLong(fleetID);
			writeBuffer.writeByteArray(command);
		}

		@Override
		public void deserializeNetwork(PacketReadBuffer readBuffer) throws IOException {
			byte version = readBuffer.readByte();
			dataUUID = readBuffer.readString();
			fleetID = readBuffer.readLong();
			command = readBuffer.readByteArray();
		}

		public long getFleetID() {
			return fleetID;
		}

		public Fleet getFleet(boolean server) {
			return EntityUtils.getFleetByID(fleetID, server);
		}

		protected byte[] getCommand() {
			return command;
		}

		protected void setCommand(byte[] command) {
			this.command = command;
		}

		protected void setCommand(FleetCommand command) {
			try {
				this.command = command.serializeBytes();
			} catch(Exception exception) {
				InventoryNetwork.getInstance().logException("Failed to set fleet command for fleet " + fleetID, exception);
			}
		}

		protected void recheckCommand() {
			Fleet fleet = getFleet(true);
			if(fleet != null) {
				try {
					byte[] currentCommand = fleet.getCurrentCommandBytes();
					if(currentCommand != command) {
						fleet.setCurrentCommand(command);
						InventoryNetwork.getInstance().logWarning("Fleet command mismatch detected for fleet " + fleetID + "\nThis was most likely caused by a server restart. Attempting to correct...");
					}
				} catch(Exception exception) {
					InventoryNetwork.getInstance().logException("Failed to recheck fleet command for fleet " + fleetID, exception);
				}
			}
		}
	}
}
