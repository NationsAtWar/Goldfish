package org.nationsatwar.goldfish.packets;

import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import org.nationsatwar.goldfish.prototypes.PrototypeManager;

public class PacketHandlerPrototypeReceive implements IMessageHandler<PacketPrototype, IMessage> {

	@Override
	public IMessage onMessage(PacketPrototype message, MessageContext ctx) {
		
		PrototypeManager.addPrototype(message.prototypeName, message.prototypeID);		
		return null;
	}
}