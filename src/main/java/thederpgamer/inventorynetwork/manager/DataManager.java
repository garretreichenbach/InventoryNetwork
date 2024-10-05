package thederpgamer.inventorynetwork.manager;

import api.common.GameServer;
import api.network.packets.PacketUtil;
import org.schema.game.common.data.player.PlayerState;
import thederpgamer.inventorynetwork.data.SerializableData;
import thederpgamer.inventorynetwork.network.SendDataPacket;

/**
 * [Description]
 *
 * @author TheDerpGamer
 */
public abstract class DataManager<E extends SerializableData> {

	public static final int ADD_DATA = 0;
	public static final int REMOVE_DATA = 1;
	public static final int UPDATE_DATA = 2;

	private static DataManager<?> instance;

	public static DataManager<?> getInstance() {
		return instance;
	}

	protected DataManager() {
		instance = this;
	}

	public void sendToAll(E data, int type) {
		for(PlayerState player : GameServer.getServerState().getPlayerStatesByName().values()) {
			sendToPlayer(player, data, type);
		}
	}

	public void sendToPlayer(PlayerState player, SerializableData data, int type) {
		PacketUtil.sendPacket(player, new SendDataPacket(data, type));
	}
}
