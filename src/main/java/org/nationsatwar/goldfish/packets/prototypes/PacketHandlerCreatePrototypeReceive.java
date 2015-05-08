package org.nationsatwar.goldfish.packets.prototypes;

import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import org.nationsatwar.goldfish.prototypes.PrototypeManager;

public class PacketHandlerCreatePrototypeReceive implements IMessageHandler<PacketCreatePrototype, IMessage> {

	@Override
	public IMessage onMessage(PacketCreatePrototype message, MessageContext ctx) {
		
		PrototypeManager.addPrototype(message.prototypeName, message.prototypeID);		
		return null;
	}
}