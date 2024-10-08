package thederpgamer.inventorynetwork.data.fleets.fleetdeliverytarget;

import api.network.PacketReadBuffer;
import api.network.PacketWriteBuffer;
import org.json.JSONObject;
import org.schema.common.util.linAlg.Vector3i;
import org.schema.game.common.controller.SegmentController;
import org.schema.game.common.data.SegmentPiece;
import thederpgamer.inventorynetwork.data.SerializableData;
import thederpgamer.inventorynetwork.utils.EntityUtils;

import java.io.IOException;
import java.util.UUID;

/**
 * [Description]
 *
 * @author TheDerpGamer
 */
public class FleetDeliveryTargetData extends SerializableData {

	private final byte VERSION = 0;
	private long targetBlockIndex;
	private int entityId;

	public FleetDeliveryTargetData(long targetBlockIndex, int entityId) {
		super(DataType.FLEET_DELIVERY_TARGET_DATA, UUID.randomUUID().toString());
		this.targetBlockIndex = targetBlockIndex;
		this.entityId = entityId;
	}

	public FleetDeliveryTargetData(PacketReadBuffer readBuffer) throws IOException {
		super(readBuffer);
	}

	public FleetDeliveryTargetData(JSONObject data) {
		super(data);
	}

	@Override
	public JSONObject serialize() {
		JSONObject data = new JSONObject();
		data.put("version", VERSION);
		data.put("dataUUID", dataUUID);
		data.put("targetBlockIndex", targetBlockIndex);
		data.put("entityId", entityId);
		return data;
	}

	@Override
	public void deserialize(JSONObject data) {
		byte version = (byte) data.getInt("version");
		dataUUID = data.getString("dataUUID");
		targetBlockIndex = data.getLong("targetBlockIndex");
		entityId = data.getInt("entityId");
	}

	@Override
	public void serializeNetwork(PacketWriteBuffer writeBuffer) throws IOException {
		writeBuffer.writeByte(VERSION);
		writeBuffer.writeString(dataUUID);
		writeBuffer.writeLong(targetBlockIndex);
		writeBuffer.writeInt(entityId);
	}

	@Override
	public void deserializeNetwork(PacketReadBuffer readBuffer) throws IOException {
		byte version = readBuffer.readByte();
		dataUUID = readBuffer.readString();
		targetBlockIndex = readBuffer.readLong();
		entityId = readBuffer.readInt();
	}

	public long getTargetBlockIndex() {
		return targetBlockIndex;
	}

	public SegmentPiece getTargetBlock() {
		return getEntity().getSegmentBuffer().getPointUnsave(targetBlockIndex);
	}

	public int getEntityId() {
		return entityId;
	}

	public SegmentController getEntity() {
		return EntityUtils.getByID(entityId);
	}

	public Vector3i getTargetSector() {
		return getEntity().getSector(new Vector3i());
	}
}
