package org.nationsatwar.goldfish.packets.teleports.label;

import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import org.nationsatwar.goldfish.prototypes.Prototype;
import org.nationsatwar.goldfish.prototypes.PrototypeManager;
import org.nationsatwar.goldfish.teleports.TeleportsManager;

public class PacketHandlerSetTeleportLabelReceive implements IMessageHandler<PacketSetTeleportLabel, IMessage> {

	@Override
	public IMessage onMessage(PacketSetTeleportLabel message, MessageContext ctx) {
		
		Prototype prototype = PrototypeManager.getPrototype(message.prototypeID);
		TeleportsManager.setLabel(prototype, message.label, message.teleportID);
		
		return null;
	}
}