package org.nationsatwar.goldfish.packets;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class PacketDeletePrototype implements IMessage {
	
	public String prototypeName;
	
	public PacketDeletePrototype() {
		
	}
	
	public PacketDeletePrototype(String prototypeName) {
		
		this.prototypeName = prototypeName;
	}

	@Override
	public void fromBytes(ByteBuf buf) {

		prototypeName = ByteBufUtils.readUTF8String(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		
		ByteBufUtils.writeUTF8String(buf, prototypeName);
	}
}