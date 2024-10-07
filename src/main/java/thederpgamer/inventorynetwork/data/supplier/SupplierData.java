package thederpgamer.inventorynetwork.data.supplier;

import api.network.PacketReadBuffer;
import api.network.PacketWriteBuffer;
import org.json.JSONObject;
import org.schema.game.common.controller.SegmentController;
import org.schema.game.common.data.SegmentPiece;
import thederpgamer.inventorynetwork.data.SerializableData;
import thederpgamer.inventorynetwork.utils.EntityUtils;

import java.io.IOException;
import java.util.UUID;

/**
 * A Supplier is used to supply items to an inventory network, via a connected Producer. A Producer is any block that can
 * produce or provide items, such as an inventory block or factory.
 *
 * @author TheDerpGamer
 */
public class SupplierData extends SerializableData {

	private final byte VERSION = 0;
	private long blockIndex;
	private int entityID;
	private long producerIndex;
	private short producerType;

	public SupplierData(long blockIndex, int entityID, long producerIndex, short producerType) {
		super(DataType.SUPPLIER_DATA, UUID.randomUUID().toString());
		this.blockIndex = blockIndex;
		this.entityID = entityID;
		this.producerIndex = producerIndex;
		this.producerType = producerType;
	}

	public SupplierData(PacketReadBuffer readBuffer) throws IOException {
		super(readBuffer);
	}

	public SupplierData(JSONObject data) {
		super(data);
	}

	@Override
	public JSONObject serialize() {
		JSONObject data = new JSONObject();
		data.put("version", VERSION);
		data.put("uuid", getUUID());
		data.put("blockIndex", blockIndex);
		data.put("entityID", entityID);
		data.put("producerIndex", producerIndex);
		data.put("producerType", producerType);
		return data;
	}

	@Override
	public void deserialize(JSONObject data) {
		byte version = (byte) data.getInt("version");
		dataUUID = data.getString("uuid");
		blockIndex = data.getLong("blockIndex");
		entityID = data.getInt("entityID");
		producerIndex = data.getLong("producerIndex");
		producerType = (short) data.getInt("producerType");
	}

	@Override
	public void serializeNetwork(PacketWriteBuffer writeBuffer) throws IOException {
		writeBuffer.writeByte(VERSION);
		writeBuffer.writeString(dataUUID);
		writeBuffer.writeLong(blockIndex);
		writeBuffer.writeInt(entityID);
		writeBuffer.writeLong(producerIndex);
		writeBuffer.writeShort(producerType);
	}

	@Override
	public void deserializeNetwork(PacketReadBuffer readBuffer) throws IOException {
		dataUUID = readBuffer.readString();
		blockIndex = readBuffer.readLong();
		entityID = readBuffer.readInt();
		producerIndex = readBuffer.readLong();
		producerType = readBuffer.readShort();
	}

	public long getBlockIndex() {
		return blockIndex;
	}

	public int getEntityID() {
		return entityID;
	}

	public long getProducerIndex() {
		return producerIndex;
	}

	public short getProducerType() {
		return producerType;
	}

	public SegmentController getSegmentController() {
		return EntityUtils.getByID(entityID);
	}

	public SegmentPiece getSupplierBlock() {
		return getSegmentController().getSegmentBuffer().getPointUnsave(blockIndex);
	}

	public SegmentPiece getProducerBlock() {
		SegmentPiece segmentPiece = getSegmentController().getSegmentBuffer().getPointUnsave(producerIndex);
		if(segmentPiece.getType() != producerType) {
			SupplierDataManager.getInstance().removeData(this, segmentPiece.getSegmentController().isOnServer());
			return null;
		} else return segmentPiece;
	}
}
