package org.nationsatwar.goldfish.packets.prototypes.rename;

import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import org.nationsatwar.goldfish.prototypes.PrototypeManager;

public class PacketHandlerRenamePrototypeReceive implements IMessageHandler<PacketRenamePrototype, IMessage> {

	@Override
	public IMessage onMessage(PacketRenamePrototype message, MessageContext ctx) {
		
		PrototypeManager.renamePrototype(message.oldPrototypeName, message.newPrototypeName);	
		return null;
	}
}