package org.nationsatwar.goldfish.packets.teleports.label;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class PacketSetTeleportLabel implements IMessage {
	
	public String playerUUID;
	public int prototypeID;
	public int teleportID;
	
	public PacketSetTeleportLabel() {
		
	}
	
	public PacketSetTeleportLabel(String playerUUID, int prototypeID, int teleportID) {
		
		this.playerUUID = playerUUID;
		this.prototypeID = prototypeID;
		this.teleportID = teleportID;
	}

	@Override
	public void fromBytes(ByteBuf buf) {

		playerUUID = ByteBufUtils.readUTF8String(buf);
		prototypeID = buf.readInt();
		teleportID = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		
		ByteBufUtils.writeUTF8String(buf, playerUUID);
		buf.writeInt(prototypeID);
		buf.writeInt(teleportID);
	}
}