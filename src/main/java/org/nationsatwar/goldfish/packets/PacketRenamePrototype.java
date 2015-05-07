package org.nationsatwar.goldfish.packets;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class PacketRenamePrototype implements IMessage {
	
	public String oldPrototypeName;
	public String newPrototypeName;
	
	public PacketRenamePrototype() {
		
	}
	
	public PacketRenamePrototype(String oldPrototypeName, String newPrototypeName) {
		
		this.oldPrototypeName = oldPrototypeName;
		this.newPrototypeName = newPrototypeName;
	}

	@Override
	public void fromBytes(ByteBuf buf) {

		oldPrototypeName = ByteBufUtils.readUTF8String(buf);
		newPrototypeName = ByteBufUtils.readUTF8String(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		
		ByteBufUtils.writeUTF8String(buf, oldPrototypeName);
		ByteBufUtils.writeUTF8String(buf, newPrototypeName);
	}
}