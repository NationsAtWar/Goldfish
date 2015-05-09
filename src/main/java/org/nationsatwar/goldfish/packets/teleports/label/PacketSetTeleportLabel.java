package org.nationsatwar.goldfish.packets.teleports.label;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class PacketSetTeleportLabel implements IMessage {
	
	public int prototypeID;
	public int teleportID;
	public String label;
	
	public PacketSetTeleportLabel() {
		
	}
	
	public PacketSetTeleportLabel(int prototypeID, int teleportID, String label) {
		
		this.prototypeID = prototypeID;
		this.teleportID = teleportID;
		this.label = label;
	}

	@Override
	public void fromBytes(ByteBuf buf) {

		prototypeID = buf.readInt();
		teleportID = buf.readInt();
		label = ByteBufUtils.readUTF8String(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		
		buf.writeInt(prototypeID);
		buf.writeInt(teleportID);
		ByteBufUtils.writeUTF8String(buf, label);
	}
}