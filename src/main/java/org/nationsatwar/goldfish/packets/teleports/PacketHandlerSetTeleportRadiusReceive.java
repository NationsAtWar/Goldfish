package org.nationsatwar.goldfish.packets.teleports;

import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import org.nationsatwar.goldfish.prototypes.Prototype;
import org.nationsatwar.goldfish.prototypes.PrototypeManager;
import org.nationsatwar.goldfish.teleports.TeleportsManager;

public class PacketHandlerSetTeleportRadiusReceive implements IMessageHandler<PacketSetTeleportRadius, IMessage> {

	@Override
	public IMessage onMessage(PacketSetTeleportRadius message, MessageContext ctx) {
		
		Prototype prototype = PrototypeManager.getPrototype(message.prototypeID);
		
		TeleportsManager.setTeleportRadius(prototype, message.teleportRadius, message.teleportID);
		
		return null;
	}
}