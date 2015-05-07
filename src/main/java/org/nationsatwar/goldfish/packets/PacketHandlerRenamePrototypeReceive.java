package org.nationsatwar.goldfish.packets;

import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import org.nationsatwar.goldfish.prototypes.Prototype;
import org.nationsatwar.goldfish.prototypes.PrototypeManager;

public class PacketHandlerRenamePrototypeReceive implements IMessageHandler<PacketRenamePrototype, IMessage> {

	@Override
	public IMessage onMessage(PacketRenamePrototype message, MessageContext ctx) {
		
		Prototype prototype = PrototypeManager.getPrototype(message.oldPrototypeName);
		prototype.renamePrototype(message.newPrototypeName);
		return null;
	}
}