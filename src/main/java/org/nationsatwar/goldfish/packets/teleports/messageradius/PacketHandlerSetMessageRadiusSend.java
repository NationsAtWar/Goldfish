package org.nationsatwar.goldfish.packets.teleports.messageradius;

import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import org.nationsatwar.goldfish.prototypes.Prototype;
import org.nationsatwar.goldfish.prototypes.PrototypeManager;
import org.nationsatwar.goldfish.teleports.TeleportsManager;

public class PacketHandlerSetMessageRadiusSend implements IMessageHandler<PacketSetMessageRadius, IMessage> {

	@Override
	public IMessage onMessage(PacketSetMessageRadius message, MessageContext ctx) {
		
		Prototype prototype = PrototypeManager.getPrototype(message.prototypeID);
		TeleportsManager.setMessageRadius(prototype, message.messageRadius, message.teleportID, true);
		
		return null;
	}
}