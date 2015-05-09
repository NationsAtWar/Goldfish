package org.nationsatwar.goldfish.packets.prototypes.activate;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class PacketActivatePrototype implements IMessage {

	public int prototypeID;
	public boolean activated;
	
	public PacketActivatePrototype() {
		
	}
	
	public PacketActivatePrototype(int prototypeID, boolean activated) {

		this.prototypeID = prototypeID;
		this.activated = activated;
	}

	@Override
	public void fromBytes(ByteBuf buf) {

		prototypeID = buf.readInt();
		activated = buf.readBoolean();
	}

	@Override
	public void toBytes(ByteBuf buf) {

		buf.writeInt(prototypeID);
		buf.writeBoolean(activated);
	}
}