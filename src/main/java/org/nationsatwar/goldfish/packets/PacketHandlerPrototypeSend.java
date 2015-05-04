package org.nationsatwar.goldfish.packets;

import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import org.nationsatwar.goldfish.prototypes.PrototypeManager;

public class PacketHandlerPrototypeSend implements IMessageHandler<PacketPrototype, IMessage> {

	@Override
	public IMessage onMessage(PacketPrototype message, MessageContext ctx) {
		
		PrototypeManager.addPrototype(message.prototypeName);
		return null;
	}
}