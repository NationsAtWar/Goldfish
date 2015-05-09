package org.nationsatwar.goldfish.packets.teleports.messageradius;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class PacketSetMessageRadius implements IMessage {

	public int messageRadius;
	public int prototypeID;
	public int teleportID;
	
	public PacketSetMessageRadius() {
		
	}
	
	public PacketSetMessageRadius(int messageRadius, int prototypeID, int teleportID) {
		
		this.messageRadius = messageRadius;
		this.prototypeID = prototypeID;
		this.teleportID = teleportID;
	}

	@Override
	public void fromBytes(ByteBuf buf) {

		messageRadius = buf.readInt();
		prototypeID = buf.readInt();
		teleportID = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {

		buf.writeInt(messageRadius);
		buf.writeInt(prototypeID);
		buf.writeInt(teleportID);
	}
}