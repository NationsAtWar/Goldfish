package org.nationsatwar.goldfish.packets.prototypes.warp;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class PacketWarpPlayer implements IMessage {
	
	public String playerUUID;
	public String prototypeName;
	
	public PacketWarpPlayer() {
		
	}
	
	public PacketWarpPlayer(String playerUUID, String prototypeName) {
		
		this.playerUUID = playerUUID;
		this.prototypeName = prototypeName;
	}

	@Override
	public void fromBytes(ByteBuf buf) {

		playerUUID = ByteBufUtils.readUTF8String(buf);
		prototypeName = ByteBufUtils.readUTF8String(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		
		ByteBufUtils.writeUTF8String(buf, playerUUID);
		ByteBufUtils.writeUTF8String(buf, prototypeName);
	}
}