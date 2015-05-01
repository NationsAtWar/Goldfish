package org.nationsatwar.goldfish.packets;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class PacketPrototype implements IMessage {
	
	public String prototypeName;
	public int prototypeID;
	
	public PacketPrototype() {
		
	}
	
	public PacketPrototype(String prototypeName, int prototypeID) {
		
		this.prototypeName = prototypeName;
		this.prototypeID = prototypeID;
	}

	@Override
	public void fromBytes(ByteBuf buf) {

		prototypeName = ByteBufUtils.readUTF8String(buf);
		prototypeID = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		
		ByteBufUtils.writeUTF8String(buf, prototypeName);
		buf.writeInt(prototypeID);
	}
}