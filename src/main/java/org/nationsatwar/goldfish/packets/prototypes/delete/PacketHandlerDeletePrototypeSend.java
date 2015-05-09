package org.nationsatwar.goldfish.packets.prototypes.delete;

import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import org.nationsatwar.goldfish.prototypes.PrototypeManager;

public class PacketHandlerDeletePrototypeSend implements IMessageHandler<PacketDeletePrototype, IMessage> {

	@Override
	public IMessage onMessage(PacketDeletePrototype message, MessageContext ctx) {
		
		PrototypeManager.deletePrototype(message.prototypeName);		
		return null;
	}
}