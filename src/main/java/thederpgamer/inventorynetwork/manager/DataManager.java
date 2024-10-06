package thederpgamer.inventorynetwork.manager;

import api.common.GameServer;
import api.network.packets.PacketUtil;
import org.schema.game.common.data.SegmentPiece;
import org.schema.game.common.data.player.PlayerState;
import thederpgamer.inventorynetwork.data.SerializableData;
import thederpgamer.inventorynetwork.network.SendDataPacket;
import thederpgamer.inventorynetwork.network.SyncRequestPacket;

import java.util.Set;

/**
 * [Description]
 *
 * @author TheDerpGamer
 */
public abstract class DataManager<E extends SerializableData> {

	public static final int ADD_DATA = 0;
	public static final int REMOVE_DATA = 1;
	public static final int UPDATE_DATA = 2;

	public static void initialize(boolean client) {
		StockDataManager.initialize(client);
	}

	public void sendDataToAllPlayers(SerializableData data, int type) {
		for(PlayerState player : GameServer.getServerState().getPlayerStatesByName().values()) sendDataToPlayer(player, data, type);
	}

	public void sendDataToPlayer(PlayerState player, SerializableData data, int type) {
		PacketUtil.sendPacket(player, new SendDataPacket(data, type));
	}

	public void sendAllDataToPlayer(PlayerState player) {
		Set<E> cache = getCache(true);
		for(E data : cache) sendDataToPlayer(player, data, ADD_DATA);
	}

	public void requestFromServer() {
		PacketUtil.sendPacketToServer(new SyncRequestPacket());
	}

	public Set<E> getCache(boolean isServer) {
		return isServer ? getServerCache() : getClientCache();
	}

	public abstract E getFromUUID(String uuid);

	public abstract E getFromSegmentPiece(SegmentPiece segmentPiece);

	public abstract Set<E> getServerCache();

	public abstract void addToServerCache(E data);

	public abstract void removeFromServerCache(E data);

	public abstract void updateServerCache(E data);

	public abstract Set<E> getClientCache();

	public abstract void addToClientCache(E data);

	public abstract void removeFromClientCache(E data);

	public abstract void updateClientCache(E data);
}
