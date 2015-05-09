package org.nationsatwar.goldfish.packets.teleports.message;

import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import org.nationsatwar.goldfish.prototypes.Prototype;
import org.nationsatwar.goldfish.prototypes.PrototypeManager;
import org.nationsatwar.goldfish.teleports.TeleportsManager;

public class PacketHandlerSetTeleportMessageReceive implements IMessageHandler<PacketSetTeleportMessage, IMessage> {

	@Override
	public IMessage onMessage(PacketSetTeleportMessage message, MessageContext ctx) {
		
		Prototype prototype = PrototypeManager.getPrototype(message.prototypeID);
		TeleportsManager.setMessage(prototype, message.message, message.teleportID);
		
		return null;
	}
}