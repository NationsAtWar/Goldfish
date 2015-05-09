package org.nationsatwar.goldfish.packets.prototypes.activate;

import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import org.nationsatwar.goldfish.prototypes.PrototypeManager;

public class PacketHandlerActivatePrototypeSend implements IMessageHandler<PacketActivatePrototype, IMessage> {

	@Override
	public IMessage onMessage(PacketActivatePrototype message, MessageContext ctx) {
		
		PrototypeManager.setPrototypeActivation(message.prototypeID, message.activated, true);
		return null;
	}
}