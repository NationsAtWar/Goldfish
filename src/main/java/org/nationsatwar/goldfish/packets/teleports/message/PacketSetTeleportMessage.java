package org.nationsatwar.goldfish.packets.teleports.message;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class PacketSetTeleportMessage implements IMessage {
	
	public int prototypeID;
	public int teleportID;
	public String message;
	
	public PacketSetTeleportMessage() {
		
	}
	
	public PacketSetTeleportMessage(int prototypeID, int teleportID, String message) {
		
		this.prototypeID = prototypeID;
		this.teleportID = teleportID;
		this.message = message;
	}

	@Override
	public void fromBytes(ByteBuf buf) {

		prototypeID = buf.readInt();
		teleportID = buf.readInt();
		message = ByteBufUtils.readUTF8String(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		
		buf.writeInt(prototypeID);
		buf.writeInt(teleportID);
		ByteBufUtils.writeUTF8String(buf, message);
	}
}