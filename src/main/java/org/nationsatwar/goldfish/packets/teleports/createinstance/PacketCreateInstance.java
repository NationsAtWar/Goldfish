package org.nationsatwar.goldfish.packets.teleports.createinstance;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class PacketCreateInstance implements IMessage {

	public int prototypeID;
	public int instanceID;
	
	public PacketCreateInstance() {
		
	}
	
	public PacketCreateInstance(int prototypeID, int instanceID) {
		
		this.prototypeID = prototypeID;
		this.instanceID = instanceID;
	}

	@Override
	public void fromBytes(ByteBuf buf) {

		prototypeID = buf.readInt();
		instanceID = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {

		buf.writeInt(prototypeID);
		buf.writeInt(instanceID);
	}
}