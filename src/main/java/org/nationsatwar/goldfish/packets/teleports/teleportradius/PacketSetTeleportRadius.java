package org.nationsatwar.goldfish.packets.teleports.teleportradius;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class PacketSetTeleportRadius implements IMessage {

	public int teleportRadius;
	public int prototypeID;
	public int teleportID;
	
	public PacketSetTeleportRadius() {
		
	}
	
	public PacketSetTeleportRadius(int teleportRadius, int prototypeID, int teleportID) {
		
		this.teleportRadius = teleportRadius;
		this.prototypeID = prototypeID;
		this.teleportID = teleportID;
	}

	@Override
	public void fromBytes(ByteBuf buf) {

		teleportRadius = buf.readInt();
		prototypeID = buf.readInt();
		teleportID = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {

		buf.writeInt(teleportRadius);
		buf.writeInt(prototypeID);
		buf.writeInt(teleportID);
	}
}