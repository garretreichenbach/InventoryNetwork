package thederpgamer.inventorynetwork.network;

import api.network.Packet;
import api.network.PacketReadBuffer;
import api.network.PacketWriteBuffer;
import org.schema.game.common.data.player.PlayerState;
import thederpgamer.inventorynetwork.InventoryNetwork;
import thederpgamer.inventorynetwork.data.SerializableData;
import thederpgamer.inventorynetwork.data.stockmanager.StockManagerDataManager;

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

	public SendDataPacket() {}

	public SendDataPacket(SerializableData data, int type) {
		this.data = data;
		this.type = type;
		dataType = data.getDataType();
	}

	@Override
	public void readPacketData(PacketReadBuffer packetReadBuffer) {
		try {
			type = packetReadBuffer.readInt();
			data = dataType.getDataClass().getConstructor(PacketReadBuffer.class).newInstance(packetReadBuffer);
		} catch(Exception exception) {
			InventoryNetwork.getInstance().logException("An error occurred while reading data packet", exception);
		}
	}

	@Override
	public void writePacketData(PacketWriteBuffer packetWriteBuffer) throws IOException {
		packetWriteBuffer.writeInt(type);
		data.serializeNetwork(packetWriteBuffer);
	}

	@Override
	public void processPacketOnClient() {
		switch(dataType) {
			case STOCK_MANAGER_DATA:
				StockManagerDataManager.getInstance().handlePacket(data, type, false);
				break;
			default:
				break;
		}
	}

	@Override
	public void processPacketOnServer(PlayerState playerState) {
		switch(dataType) {
			case STOCK_MANAGER_DATA:
				StockManagerDataManager.getInstance().handlePacket(data, type, true);
				break;
			default:
				break;
		}
	}
}
