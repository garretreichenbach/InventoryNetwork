package thederpgamer.inventorynetwork.network;

import api.network.Packet;
import api.network.PacketReadBuffer;
import api.network.PacketWriteBuffer;
import org.schema.game.common.data.player.PlayerState;
import thederpgamer.inventorynetwork.data.SerializableData;

import java.io.IOException;

/**
 * [Description]
 *
 * @author TheDerpGamer
 */
public class SendDataPacket extends Packet {

	private SerializableData.DataType dataType;
	private SerializableData data;
	private int type;

	public SendDataPacket() {

	}

	public SendDataPacket(SerializableData data, int type) {
		this.data = data;
		this.type = type;
		dataType = data.getDataType();
	}

	@Override
	public void readPacketData(PacketReadBuffer packetReadBuffer) throws IOException {
		data = dataType.getDataClass().newInstance(
	}

	@Override
	public void writePacketData(PacketWriteBuffer packetWriteBuffer) throws IOException {

	}

	@Override
	public void processPacketOnClient() {

	}

	@Override
	public void processPacketOnServer(PlayerState playerState) {

	}
}
