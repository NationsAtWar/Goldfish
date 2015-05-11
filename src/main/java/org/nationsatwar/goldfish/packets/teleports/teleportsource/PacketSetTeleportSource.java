package org.nationsatwar.goldfish.packets.teleports.teleportsource;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class PacketSetTeleportSource implements IMessage {
	
	public String worldName;
	public double posX;
	public double posY;
	public double posZ;
	
	public int prototypeID;
	public int teleportID;
	
	public PacketSetTeleportSource() {
		
	}
	
	public PacketSetTeleportSource(String worldName, double posX, double posY, double posZ, int prototypeID, int teleportID) {
		
		this.worldName = worldName;
		this.posX = posX;
		this.posY = posY;
		this.posZ = posZ;
		
		this.prototypeID = prototypeID;
		this.teleportID = teleportID;
	}

	@Override
	public void fromBytes(ByteBuf buf) {

		worldName = ByteBufUtils.readUTF8String(buf);
		posX = buf.readDouble();		
		posY = buf.readDouble();		
		posZ = buf.readDouble();		
		
		prototypeID = buf.readInt();
		teleportID = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		
		ByteBufUtils.writeUTF8String(buf, worldName);
		buf.writeDouble(posX);
		buf.writeDouble(posY);
		buf.writeDouble(posZ);
		
		buf.writeInt(prototypeID);
		buf.writeInt(teleportID);
	}
}