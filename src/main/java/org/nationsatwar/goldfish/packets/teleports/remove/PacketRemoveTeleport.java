package org.nationsatwar.goldfish.packets.teleports.remove;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class PacketRemoveTeleport implements IMessage {
	
	public int prototypeID;
	public int teleportID;
	public int teleportAmount;
	
	public PacketRemoveTeleport() {
		
	}
	
	public PacketRemoveTeleport(int prototypeID, int teleportID, int teleportAmount) {
		
		this.prototypeID = prototypeID;
		this.teleportID = teleportID;
		this.teleportAmount = teleportAmount;
	}

	@Override
	public void fromBytes(ByteBuf buf) {

		prototypeID = buf.readInt();
		teleportID = buf.readInt();
		teleportAmount = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		
		buf.writeInt(prototypeID);
		buf.writeInt(teleportID);
		buf.writeInt(teleportAmount);
	}
}